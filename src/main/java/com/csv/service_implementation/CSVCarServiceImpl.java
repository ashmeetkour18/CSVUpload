package com.csv.service_implementation;

import com.csv.entity.Car;
import com.csv.exceptions.CSVException;
import com.csv.modal.CarDto;
import com.csv.modal.CommonResponse;
import com.csv.repository.AdminRepository;
import com.csv.repository.CarRepository;
import com.csv.service.CSVCarService;
import com.csv.utility.CSVUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CSVCarServiceImpl implements CSVCarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Value("${FILE_NAME}")
    private String fileName;

    public ResponseEntity<CommonResponse> saveCarDetails(MultipartFile file) {
        List<CarDto> carDtos = CSVUtility.csvToCarDto(file);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Car> cars =
                    carRepository.saveAll(carDtos.stream().map(car -> objectMapper.convertValue(car, Car.class)).toList());
            return new ResponseEntity<>(CommonResponse.builder().message("Records inserted : " + cars.size()).statusCode(HttpStatus.OK.value()).data(cars).build(), HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {
            throw new CSVException("Could not insert the records due to duplicate Registration Number");
        }
    }

    public ResponseEntity<InputStreamResource> download() {
        List<Car> cars = carRepository.findAllByDeleteStatus(false);
        InputStreamResource file = new InputStreamResource(CSVUtility.byteArrayInputStreamToCSV(cars));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName).contentType(MediaType.parseMediaType("application/csv")).body(file);

    }


}