package com.model.dto;

public class ResponseBookDto {

    private long id;
    private String name;
    private int numberOfPages;

    public ResponseBookDto() {
    }

    public ResponseBookDto(String name, int numberOfPages) {
        this.name = name;
        this.numberOfPages = numberOfPages;
    }

    public ResponseBookDto(long id, String name, int numberOfPages) {
        this.id = id;
        this.name = name;
        this.numberOfPages = numberOfPages;
    }

    public long getId() {
        return id;
    }

    public void setId(long idl) {
        this.id = idl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "name='" + name + '\'' +
                ", numberOfPages=" + numberOfPages +
                '}';
    }
}

