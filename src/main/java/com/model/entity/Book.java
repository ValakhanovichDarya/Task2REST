package com.model.entity;


public class Book extends BaseEntity{
    private String name;
    private int numberOfPages;
    private Author author;

    public Book() {
    }

    public Book(String name, int numberOfPages, Author author) {
        this.name = name;
        this.numberOfPages = numberOfPages;
        this.author = author;
    }

    public Book(long id, String name, int numberOfPages, Author author) {
        super(id);
        this.name = name;
        this.numberOfPages = numberOfPages;
        this.author = author;
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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", numberOfPages=" + numberOfPages +
                ", author=" + author +
                '}';
    }
}
