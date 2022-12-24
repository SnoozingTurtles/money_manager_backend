package com.thesnoozingturtle.moneymanagerrestapi.service.impl;

import com.thesnoozingturtle.moneymanagerrestapi.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${project.image}")
    private String path;

    @Override
    public String uploadImage(MultipartFile image) {
        try {
            //Get image name
            String imageName = image.getOriginalFilename();

            //Generating random name for image to avoid overwriting conflicts
            String randomId = UUID.randomUUID().toString();
            String newImageName = randomId + imageName.substring(imageName.lastIndexOf("."));

            //Full path
            String fullPath = path + File.separator + newImageName;

            //Create folder if not created
            File f = new File(path);
            if(!f.exists()) {
                f.mkdir();
            }

            //Get InputStream
            InputStream imageStream = image.getInputStream();

            //Copy the files to the folder
            Files.copy(imageStream, Paths.get(fullPath));
            return imageName;
        } catch(Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
