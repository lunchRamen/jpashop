package jpabook.jpashop.repository;

import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderSearch {

    private String memberName; //회원 이름
    private OrderStatus orderStatus; //주문 상태
    //위의 두 멤버로 검색을 때렸을때 각 상황에 맞게끔 검색결과가 나와야함.

}
