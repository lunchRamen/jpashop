package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //스프링 컨테이너환경에서 테스트를 돌린다.
@Transactional //테스트코드에 작성시, 클래스 메모리 해제되면 다 롤백한다.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given 이런게 주어졌을때
        Member member = new Member();
        member.setName("kim");
        //when 회원가입 실행하면
        Long savedId = memberService.join(member);

        //then 회원가입이 완료되어야한다.
        assertEquals(member,memberRepository.findOne(savedId));
     }


     @Test
     public void 중복_회원_예외() throws Exception{
         //given
         Member member = new Member();
         member.setName("kim");

         Member member1 = new Member();
         member1.setName("kim");

         //when
         Long savedId = memberService.join(member);
         try{
             Long savedId1 = memberService.join(member1);
         } catch (IllegalStateException e){
             return;
         }

         //then
      }
}