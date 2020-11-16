package jpabook.jpashop.domain;

import lombok.Getter;

@Getter
public enum OrderStatus {

    ORDER("ORDER"),
    CANCEL("CANCEL");

    OrderStatus(String nm) {
        this.nm = nm;
    }

    private String nm;

}
