package com.csv.service;

import com.csv.modal.CommonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface CSVUserService {
    ResponseEntity<CommonResponse> saveUserDetails(MultipartFile file);
}
