package com.model.dto;

import java.util.Objects;

public class BookDto {

    private long id;
    private String name;
    private int numberOfPages;
    private AuthorDto authorDto;

    public BookDto() {
    }

    public BookDto(String name, int numberOfPages) {
        this.name = name;
        this.numberOfPages = numberOfPages;
    }

    public BookDto(String name, int numberOfPages, AuthorDto authorDto) {
        this.name = name;
        this.numberOfPages = numberOfPages;
        this.authorDto = authorDto;
    }

    public BookDto(long id, String name, int numberOfPages, AuthorDto authorDto) {
        this.id = id;
        this.name = name;
        this.numberOfPages = numberOfPages;
        this.authorDto = authorDto;
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

    public AuthorDto getAuthorDto() {
        return authorDto;
    }

    public void setAuthorDto(AuthorDto authorDto) {
        this.authorDto = authorDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDto bookDto = (BookDto) o;
        return id == bookDto.id &&
                numberOfPages == bookDto.numberOfPages &&
                Objects.equals(name, bookDto.name) &&
                Objects.equals(authorDto, bookDto.authorDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, numberOfPages, authorDto);
    }

    @Override
    public String toString() {
        return "BookDto{" +
                "name='" + name + '\'' +
                ", numberOfPages=" + numberOfPages +
                '}';
    }
}
