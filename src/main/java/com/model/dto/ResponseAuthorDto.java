package com.model.dto;

import java.util.List;

public class ResponseAuthorDto {
    private long id;
    private String name;
    private List<ResponseBookDto> books;

    public ResponseAuthorDto() {
    }

    public ResponseAuthorDto(long id, String name, List<ResponseBookDto> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ResponseBookDto> getBooks() {
        return books;
    }

    public void setBooks(List<ResponseBookDto> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "ResponseAuthorDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}



