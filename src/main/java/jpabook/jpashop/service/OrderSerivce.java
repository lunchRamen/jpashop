package jpabook.jpashop.service;


import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderSerivce {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional //데이터를 변경하는거라.
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 설정 -> 여기선 간단하게 주문자의 회원가입 배송정보를 이용.
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품 생성
        //지금은 모델단에 비즈니스로직(createOrderItem)을 통해, 파라미터로 받은 인자로 orderItem을 만드는데,
        //생성자를 통해 똑같은 로직을 만들 수 있음(객체 생성 후 직접 set)
        //이걸 막기위해서(for 유지보수) 다른로직으로 객체생성 막기위해 protected 생성자를 만드는게 낫다.
        //이렇게 코드를 "제약"하는 스타일로 코딩해나가는게, 미래를 생각한 코딩방식이다.
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
//        OrderItem orderItem1 = new OrderItem();

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        /*
        * 여기서, order만 save해주면 되는 이유.
        * FK들에 어노테이션으로 다 cascade를 걸어줬기때문.
        * 하나의 테이블 데이터값을 저장했을때, FK로 연결된 테이블의 데이터도 종속적으로 저장한다.
        * 그렇다면, 언제 CASCADE를 걸어주는게 좋을까?
        * -> 해당 FK 테이블의 관계가 종속적이고, 다른 테이블의 참조를 "받고"있지 않는 상태(하는것도 따져봐야겠찌만 그나마 나음)
        * -> 또한, FK들을 가지고 있는 모델이 단방향으로 참조 받지 않는 private한 모델인 경우
        * -> Member와는 1:1매핑 관계(양방향)이고, Delivery와 Item(OrderItem)과는 단방향관계라서 설정해도 탈이 없는 것.
        * */
        orderRepository.save(order);
        return order.getId();

    }

    //취소
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }





}
