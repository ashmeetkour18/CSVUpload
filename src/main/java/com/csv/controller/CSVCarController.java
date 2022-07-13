package com.csv.controller;

import com.csv.exceptions.CSVException;
import com.csv.modal.CommonResponse;
import com.csv.service_implementation.CSVCarServiceImpl;
import com.csv.utility.CSVUtility;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
/**
 * Controller for bulk onboard cars and download car details in a csv file
 *
 * @author Ashmeet Hora
 *
 */

@RestController
@RequestMapping("api/v1/cars")
public class CSVCarController {

    @Autowired
    private CSVCarServiceImpl csvCarService;
    @Value("${FILE_TYPE}")
   private String fileType;


    @Operation(summary = "This is to onboard bulk cars")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CommonResponse> uploadCarDetailsFile(@RequestParam("file") MultipartFile file) {
        if (CSVUtility.hasCSVFormat(file,fileType)) {
            return csvCarService.saveCarDetails(file);
        }
        throw new CSVException("Please upload a csv file!");
    }

    @Operation(summary = "This is to download details of all the cars in a csv file")
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() {
        return csvCarService.download();
    }
}
