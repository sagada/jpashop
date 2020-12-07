package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
@Log4j2
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init()
    {
        log.info("InitService init");
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1()
        {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address("서울", "관악구", "12번가"));
            em.persist(member);

            Book book = new Book();
            book.setName("JPA1 BOOK1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);
            Book book2 = new Book();
            book2.setName("JPA2 BOOK2");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            OrderItem item1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem item2 = OrderItem.createOrderItem(book2, 20000, 2);
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, item1, item2);
            em.persist(order);
        }

        public void dbInit2()
        {
            Member member = new Member();
            member.setName("userB");
            member.setAddress(new Address("인천", "남동", "11번가"));
            em.persist(member);

            Book book = new Book();
            book.setName("InteliJ BOOK1");
            book.setPrice(10000);
            book.setStockQuantity(100);
            em.persist(book);

            Book book2 = new Book();
            book2.setName("InteliJ BOOK2");
            book2.setPrice(20000);
            book2.setStockQuantity(100);
            em.persist(book2);

            OrderItem item1 = OrderItem.createOrderItem(book, 10000, 1);
            OrderItem item2 = OrderItem.createOrderItem(book2, 20000, 2);
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, item1, item2);
            em.persist(order);
        }
    }
}

