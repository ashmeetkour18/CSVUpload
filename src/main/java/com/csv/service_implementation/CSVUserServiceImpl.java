package com.csv.service_implementation;

import com.csv.entity.User;
import com.csv.exceptions.CSVException;
import com.csv.modal.CommonResponse;
import com.csv.modal.UserDto;
import com.csv.repository.RoleRepository;
import com.csv.repository.UserRepository;
import com.csv.service.CSVUserService;
import com.csv.utility.CSVUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CSVUserServiceImpl implements CSVUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<CommonResponse> saveUserDetails(MultipartFile file) {
        List<UserDto> usersDto = CSVUtility.csvToUserDto(file);

        try {
            List<User> users = userRepository.saveAll(usersDto.stream().map(userDto -> {
                User user = User.builder().firstName(userDto.getFirstName()).lastName(userDto.getLastName()).address(userDto.getAddress()).email(userDto.getEmail()).mobileNo(userDto.getMobileNumber()).build();
                roleRepository.findByRoleName(userDto.getRole()).ifPresentOrElse(user::setRole, () -> {
                    throw new CSVException("Invalid file : User Role doesn't exist for user name " + userDto.getFirstName() +
                            " with email" +
                            " :" + userDto.getEmail());
                });
                return user;


            }).toList());
            return new ResponseEntity<>(CommonResponse.builder().message("Records inserted : " + users.size()).statusCode(HttpStatus.OK.value()).data(users).build(), HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            throw new CSVException("Could not insert the records due to duplicate emails");
        }

    }
}
