package com.csv.service_implementation;

import com.csv.entity.User;
import com.csv.exceptions.CSVException;
import com.csv.modal.CommonResponse;
import com.csv.modal.UserDto;
import com.csv.repository.AdminRepository;
import com.csv.repository.RoleRepository;
import com.csv.service.CSVUserService;
import com.csv.utility.CSVUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class CSVUserServiceImpl implements CSVUserService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public ResponseEntity<CommonResponse> saveUserDetails(MultipartFile file) {
        List<UserDto> usersDto = CSVUtility.csvToUserDto(file);
        final List<User> users = new ArrayList<>();
        usersDto.forEach(userDto -> {
            if (adminRepository.existsByEmailAndDeleteStatus(userDto.getEmail(), true)) {
                adminRepository.updateIfUserExist(userDto.getEmail());
                User user = User.builder().firstName(userDto.getFirstName()).lastName(userDto.getLastName()).address(userDto.getAddress()).email(userDto.getEmail()).mobileNo(userDto.getMobileNumber()).build();
                users.add(user);
            }
        });
        try {


            List<User> usersList = adminRepository.saveAll(usersDto.stream().filter(userDto -> !("ROLE_ADMIN".equals(userDto.getRole())) && !adminRepository.existsByEmail(userDto.getEmail())).filter(CSVUtility.distinctByKey(UserDto::getEmail)).map(userDto -> {
                User user = User.builder().firstName(userDto.getFirstName()).lastName(userDto.getLastName()).address(userDto.getAddress()).email(userDto.getEmail()).mobileNo(userDto.getMobileNumber()).build();
                roleRepository.findByRoleName(userDto.getRole()).ifPresentOrElse(user::setRole, () -> {
                    throw new CSVException("Invalid file : User Role doesn't exist for user name " + userDto.getFirstName() + " with email" + " :" + userDto.getEmail());
                });
                return user;


            }).toList());
            usersList.addAll(users);
            return new ResponseEntity<>(CommonResponse.builder().message("Records inserted : " + usersList.size()).statusCode(HttpStatus.OK.value()).data(usersList).build(), HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            throw new CSVException("Could not insert the records due to duplicate emails");
        }

    }
}
