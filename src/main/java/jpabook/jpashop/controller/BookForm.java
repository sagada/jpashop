package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {

    private String name;
    private Long id;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

}
