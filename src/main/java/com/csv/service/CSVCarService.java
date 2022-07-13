package com.csv.service;

import com.csv.modal.CommonResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CSVCarService {
    ResponseEntity<CommonResponse> saveCarDetails(MultipartFile file);

    ResponseEntity<InputStreamResource> download();

}
