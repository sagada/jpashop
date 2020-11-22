package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String zipcode;
    private String street;

    public Member toEntity()
    {
        Member member = new Member();
        member.setAddress(Address.builder()
                .city(city)
                .street(street)
                .zipcode(zipcode)
                .build());
        member.setName(name);
        return member;
    }
}
