package com.thesnoozingturtle.moneymanagerrestapi.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile image);
}
