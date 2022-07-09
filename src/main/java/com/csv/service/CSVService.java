package com.csv.service;

import com.csv.modal.Response;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CSVService {
ResponseEntity<Response> save(MultipartFile file);
ResponseEntity<InputStreamResource> download();
    ResponseEntity<Response> getAllCars();
}
