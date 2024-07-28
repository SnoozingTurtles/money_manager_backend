package com.thesnoozingturtle.moneymanagerrestapi.controller;

import com.thesnoozingturtle.moneymanagerrestapi.dto.ExpenseDto;
import com.thesnoozingturtle.moneymanagerrestapi.dto.IncomeDto;
import com.thesnoozingturtle.moneymanagerrestapi.payload.ApiResponse;
import com.thesnoozingturtle.moneymanagerrestapi.service.ExpenseService;
import com.thesnoozingturtle.moneymanagerrestapi.service.ImageService;
import com.thesnoozingturtle.moneymanagerrestapi.service.IncomeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/users")
public class ImageController {
    private final ImageService imageService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public ImageController(ImageService imageService, ExpenseService expenseService, IncomeService incomeService) {
        this.imageService = imageService;
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    @PostMapping("/{userId}/expenses/{expenseId}/images")
    public ResponseEntity<ApiResponse> uploadExpenseImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable String userId,
                                                   @PathVariable String expenseId) {
        ExpenseDto expenseById = expenseService.getExpenseById(userId, expenseId);
        String imageName = imageService.uploadImage(image);
        expenseById.setImageName(imageName);
        expenseService.updateExpense(userId, expenseId, expenseById.getCategory().getId().toString(),
                expenseById);
        return ResponseEntity.ok(ApiResponse.builder()
                                .success(true).message("Image uploaded successfully!").build());
    }
    @PostMapping("/{userId}/incomes/{incomeId}/images")
    public ResponseEntity<ApiResponse> uploadIncomeImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable String userId,
                                                   @PathVariable String incomeId) {
        IncomeDto incomeById = incomeService.getIncomeById(userId, incomeId);
        String imageName = imageService.uploadImage(image);
        incomeById.setImageName(imageName);
        incomeService.updateIncome(userId, incomeId, incomeById.getCategory().getId().toString(), incomeById);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Image uploaded successfully!").build());
    }

    @PreAuthorize(value = "@userSecurity.hasUserId(authentication, #userId)")
    @GetMapping("/{userId}/images/{imageName}")
    public void downloadImage(@PathVariable String userId,
                              @PathVariable String imageName,
                              HttpServletResponse response) throws IOException {
        InputStream inputStream = imageService.downloadImage(imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }

}
