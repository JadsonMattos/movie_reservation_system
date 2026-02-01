package com.moviereservation.application.dto.common;

import java.util.List;

public record PageResult<T>(List<T> content, long totalElements, int totalPages, int number) {

    public static <T> PageResult<T> of(List<T> content, long totalElements, int page, int size) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResult<>(content, totalElements, totalPages, page);
    }
}
