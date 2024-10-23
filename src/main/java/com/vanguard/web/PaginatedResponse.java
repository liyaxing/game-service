package com.vanguard.web;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class PaginatedResponse<T> {
    private List<T> items;

    private Page page;

    public record Page(Integer size, Integer number, Integer totalPages, Long totalElements) {
    }
}
