package com.codesoom.assignment.Dto;

public final class CreateProductDto {
    private String title;

    public CreateProductDto() {
    }

    public CreateProductDto(final String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
