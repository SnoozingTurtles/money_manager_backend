package com.thesnoozingturtle.moneymanagerrestapi.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface ImageService {
    String uploadImage(MultipartFile image);
    InputStream downloadImage(String imageName) throws FileNotFoundException;
}
