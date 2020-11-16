package jpabook.jpashop.domain;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    COMP("COMP");

    private String nm;

    DeliveryStatus(String nm) {
        this.nm = nm;
    }
    // READY, COMP
}
