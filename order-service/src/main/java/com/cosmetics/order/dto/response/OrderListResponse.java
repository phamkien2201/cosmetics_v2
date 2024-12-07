package com.cosmetics.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderListResponse {
    private List<OrderResponse> orders;
    private int totalPages;
}