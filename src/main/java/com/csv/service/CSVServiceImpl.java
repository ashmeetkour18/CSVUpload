package com.csv.service;

import com.csv.Repository.CarRepository;
import com.csv.entity.Car;
import com.csv.exceptions.CSVException;
import com.csv.modal.CarDto;
import com.csv.modal.Response;
import com.csv.utility.CSVUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService {
    @Autowired
    CarRepository carRepository;

    @Value("${fileName}")
    private String fileName;

    public ResponseEntity<Response> save(MultipartFile file) {
        try {
            List<CarDto> cars = CSVUtility.csvToDatabase(file.getInputStream());
            ModelMapper mapper = new ModelMapper();
            List<Car> carList = new ArrayList<>();
            cars.forEach(car -> carList.add(mapper.map(car, Car.class)));
            List<Car> data = carRepository.saveAll(carList);
            return new ResponseEntity<>(Response.builder().message("Records inserted : " + data.size()).statusCode(HttpStatus.OK.value()).data(data).build(), HttpStatus.OK);
        } catch (IOException e) {
            throw new CSVException("Unable to store the data");
        }
    }

    public ResponseEntity<InputStreamResource> download() {
        List<Car> cars = carRepository.findAll();
        InputStreamResource file = new InputStreamResource(CSVUtility.databaseToCSV(cars));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);

    }

    public ResponseEntity<Response> getAllCars() {
        List<Car> carList = carRepository.findAll();
        if (!carList.isEmpty()) {
            return new ResponseEntity<>(Response.builder().data(carList).statusCode(HttpStatus.OK.value()).message(carList.size() + " Records found").build()
                    , HttpStatus.OK);
        }
        return new ResponseEntity<>(Response.builder().data(new ArrayList<>()).statusCode(HttpStatus.NO_CONTENT.value()).message("No records found").build()
                , HttpStatus.NO_CONTENT);
    }
}