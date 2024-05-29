package com.mmdevelopement.crm.domain.financial.controller;

import com.mmdevelopement.crm.domain.financial.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/financial/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


}
