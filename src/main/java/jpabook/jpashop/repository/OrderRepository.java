package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class,id);
    }
    //동적쿼리.
    //request의 데이터에 따라
    //-> 그냥 order의 데이터 다 가져올지
    //-> filter를 memberName에 걸어서 가져올지
    //-> filter를 orderStatus에 걸어서 가져올지 정해야하기때문.
    //-> JPql을 request로 온 파라미터에 맞게끔 동적바인딩해서 return해주면 되는데 쌉노가다 + 비효율성
    //-> JPA Criteria로 대체.
//    public List<Order> findAllByCriteria(OrderSearch orderSearch){
//        return em.createQuery("select o from Order o join o.member m"+
//                "where o.status = :status"+
//                "and m.name like :name"
//                ,Order.class).setParameter("status", orderSearch.getOrderStatus())
//                .setParameter("name",orderSearch.getMemberName())
//                .setMaxResults(1000)
//                .getResultList();
//    }
}
