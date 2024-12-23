package com.cosmetics.product.controller;

import com.cosmetics.product.dto.ApiResponse;
import com.cosmetics.product.dto.request.ProductRequest;
import com.cosmetics.product.dto.response.ProductListResponse;
import com.cosmetics.product.dto.response.ProductResponse;
import com.cosmetics.product.entity.Product;
import com.cosmetics.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {

    ProductService productService;

    @PostMapping("/create-product")
    @Operation(summary = "Tạo sản phẩm")
    ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @GetMapping("/get-all-products")
    @Operation(summary = "Lấy danh sách sản phẩm")
    public ApiResponse<ProductListResponse> getProducts(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit",required = false) Integer limit,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "sortDirection", required = false, defaultValue = "desc") String sortDirection
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by(direction, sortBy));
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ApiResponse.<ProductListResponse>builder()
                .result(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                        .build())
                .build();
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Lấy sản phẩm bằng id")
    public ApiResponse<ProductResponse> getProductById(
            @PathVariable("productId") String productId) {
        ProductResponse product = productService.getProductById(productId);
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(productId))
                .build();
    }

    @PutMapping("/update-product/{productId}")
    @Operation(summary = "Update sản phẩm bằng id")
    ApiResponse<ProductResponse> updateProduct(@PathVariable String productId, @RequestBody ProductRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(productId, request))
                .build();
    }

    @DeleteMapping("/delete-product/{productId}")
    @Operation(summary = "Xóa sản phẩm")
    public void deleteProductById(
            @PathVariable String productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Lấy danh sách sản phẩm theo danh mục")
    public ApiResponse<ProductListResponse> findProductsByCategoryId(
            @PathVariable String categoryId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit",required = false) Integer limit,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        if (sortBy == null) {
            sortBy = "createdAt";
        }
        if (sortDirection == null) {
            sortDirection = "desc";
        }

        ProductListResponse productListResponse = productService.findProductsByCategoryId(categoryId, page, limit, sortBy, sortDirection);
        return ApiResponse.<ProductListResponse>builder()
                .result(productListResponse)
                .build();
    }

    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Lấy danh sách sản phẩm theo thương hiệu")
    public ApiResponse<ProductListResponse> findProductsByBrandId(
            @PathVariable String brandId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "sortDirection", required = false) String sortDirection
    ) {
        if (page == null) {
            page = 0;
        }
        if (limit == null) {
            limit = 20;
        }
        if (sortBy == null) {
            sortBy = "createdAt";
        }
        if (sortDirection == null) {
            sortDirection = "desc";
        }
        ProductListResponse productListResponse = productService.findProductsByBrandId(brandId, page, limit, sortBy, sortDirection);
        return ApiResponse.<ProductListResponse>builder()
                .result(productListResponse)
                .build();
    }

    @PutMapping("/{id}/quantity")
    public ApiResponse<Void> updateProductQuantity(
            @PathVariable("id") String productId,
            @RequestParam("newQuantity") Float newQuantity) {
        productService.updateProductQuantity(productId, newQuantity);
        return  ApiResponse.<Void>builder().build();
    }
}