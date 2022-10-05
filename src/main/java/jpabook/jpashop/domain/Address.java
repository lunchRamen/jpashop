package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embeddable;

/*
* //Embeddable은 이 클래스 자체가 JPA의 내장타입으로
* 쓸 수 있게끔 하는 어노테이션
* cmd + n = 단축키.
* */
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address(){}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
