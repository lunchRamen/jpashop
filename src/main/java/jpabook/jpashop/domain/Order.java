package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간.

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태를 가지고 있는 값. Django ORM의 Choice같은거.


    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        //현재 Order의 orderItems에 입력받은 orderItem을 넣고
        //orderItem에도 지금껄 넣는다.
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    protected Order(){}

    //create 메서드
    /*
    Order의 createOrder를 호출하면,
    order에 대한 모든 연관된 테이블 및 자기자신의 컬럼값들 세팅을 맞추고 order return
    여기서, create메서드는 static으로 작성한 이유?
    -> 서순이 바뀌었기때문
    주문 생성은, 주문 자체를 만드는 것이기때문에 Order객체가 생성되기 전엔 접근이 불가능하다.
    이걸 가능하게 하려고 static 메서드를 씀.
    */
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem: orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비즈니스 로직
    //주문취소 -> 주문취소를 누를때, 재고를 다시 올려줘야함.
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송이 완료되어 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems){
            orderItem.cancel(); //orderItem들에 대해서 재고를 올려주는 메서드.
        }
    }

    //조회 로직 = (각 주문의) 전체 주문 가격
    public int getTotalPrice(){
        int totalPrice = 0;

        for (OrderItem orderItem : orderItems){
            totalPrice+=orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
