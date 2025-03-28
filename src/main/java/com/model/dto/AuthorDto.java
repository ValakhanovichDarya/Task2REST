package com.model.dto;

import java.util.Objects;

public class AuthorDto {
    private long id;
    private String name;

    public AuthorDto() {
    }

    public AuthorDto(String name) {
        this.name = name;
    }

    public AuthorDto(long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDto authorDto = (AuthorDto) o;
        return id == authorDto.id &&
                Objects.equals(name, authorDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}