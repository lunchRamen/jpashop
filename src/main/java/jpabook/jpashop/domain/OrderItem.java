package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice;//주문 가격
    private int count;//주문 수량

    protected OrderItem(){}

    //생성 메서드
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //비즈니스 로직
    public void cancel() {
        getItem().addStock(count); //자기 자신의 아이템을 가져온 다음, 재고를 count만큼 늘려준다.
        //-> OrderItem의 경우 주문한 상품들의 로그가 남는곳이니까, Item에 접근해서(getItem) -> Item객체가됨
        //Item의 addStock메서드를 통해 재고를 늘려주는 메서드를 만든 것.
    }

    //조회 로직
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
