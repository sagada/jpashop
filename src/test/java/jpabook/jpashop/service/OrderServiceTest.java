package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static jpabook.jpashop.domain.OrderStatus.ORDER;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    private Book createBook(String name, int orderPrice, int stockQuantity)
    {
        Book book = new Book();
        book.setName(name);
        book.setAuthor("김영한");
        book.setPrice(orderPrice);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember()
    {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문() throws Exception
    {
        // given
        Member member = createMember();

        Book book = createBook("시골", 10000, 10);

        int orderCount = 2;
        // when

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order order = orderRepository.findOne(orderId);

        Assertions.assertThat(order.getStatus()).isEqualTo(ORDER);
        Assertions.assertThat(order.getOrderItems().size()).isEqualTo(1);
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품_주문_재고_수량_초과() throws Exception
    {
        // given
        Member member = createMember();
        Item item = createBook("JPA 읽자", 30000, 10);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), item.getId(), orderCount);

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception
    {
        // given
        Member member = createMember();
        Book book = createBook("JDAS", 10000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancel(orderId);

        // then
        Assertions.assertThat(orderRepository.findOne(orderId).getStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    public void 주문테스트_딜리버리_테스트() throws Exception
    {
        // given
        Member member = createMember();
        Item item = createBook("JPA 읽자", 30000, 10);

        int orderCount = 11;

        Long orderId = orderService.order(member.getId(), item.getId(), 3);
        Order order = orderRepository.findOne(orderId);

        Assertions.assertThat(order.getDelivery().getAddress()).isEqualTo(member.getAddress());
    }


 }