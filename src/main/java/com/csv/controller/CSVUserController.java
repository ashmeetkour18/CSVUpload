package com.csv.controller;

import com.csv.exceptions.CSVException;
import com.csv.modal.CommonResponse;
import com.csv.service_implementation.CSVUserServiceImpl;
import com.csv.utility.CSVUtility;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller to onboard bulk users
 *
 * @author Ashmeet Hora
 */
@RestController
@RequestMapping("api/v1/users")
public class CSVUserController {
    @Autowired
    private CSVUserServiceImpl csvUserService;

    @Value("${FILE_TYPE}")
    private String fileType;

    @Operation(summary = "This is to onboard bulk users")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CommonResponse> uploadUserDetailsFile(@RequestParam("file") MultipartFile file) {

        if (CSVUtility.hasCSVFormat(file, fileType)) {
            return csvUserService.saveUserDetails(file);
        }
        throw new CSVException("Please upload a csv file!");
    }
}
