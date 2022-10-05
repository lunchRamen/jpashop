package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member){
        em.persist(member); // persist = 영속성매니저에 member객체를 넣는다. 나중에 트랜잭션 커밋시점에 저장.
    }

    public Member findOne(Long id){
         return em.find(Member.class, id); // jpa의 find메서드 사용.(단건 조회)
    }

    public List<Member> findAll(){
        //inline = cmd + opt + n = return값으로 바로.
        //모두 가져오는거같은 경우, 장고처럼 Member.objects.all()이 되는 방식이 아니라
        //JPql query(다 가져오는거니까 간단) + 모델클래스 .getResultList();형태로 가져온다.
        //SQL과 거의 동일하지만, 테이블을 엔티티클래스로 대체한다고 생각하면 됨.
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class)
                .setParameter("name",name)
                .getResultList();// 이것도 JPql을 쓴다.
    }
}
