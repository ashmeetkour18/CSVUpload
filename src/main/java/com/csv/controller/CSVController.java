package com.csv.controller;

import com.csv.exceptions.CSVException;
import com.csv.utility.CSVUtility;
import com.csv.modal.Response;
import com.csv.service.CSVServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class CSVController {

    @Autowired
    CSVServiceImpl csvService;

    @PostMapping("/car/upload")
    public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file) {
      
        if (CSVUtility.hasCSVFormat(file)) {
            try {
                return csvService.save(file);
            } catch (Exception e) {
                throw new CSVException("could not upload the file : "+file.getOriginalFilename());
            }
        }
        throw new CSVException("Please upload a csv file!");
    }

    @GetMapping("/cars")
    public ResponseEntity<Response> getAllCars() {
        try {
            return csvService.getAllCars();
        } catch (Exception e) {
            throw new CSVException(e.getMessage());
        }

    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile() {
        return csvService.download();
    }
}
