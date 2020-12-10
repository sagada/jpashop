package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch)
    {
        //language=JPAQL
        String jpql = "select o From Order o join o.member m"; boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status"; }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name"; }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class) .setMaxResults(1000); //최대 1000건
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName()); }
        return query.getResultList();
    }

    public List<Order> findAllWithMemberDelivery()
    {
        return em
                .createQuery("SELECT o FROM Order o join fetch o.member m join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery2(int offset, int limit)
    {
        return em
                .createQuery("SELECT o FROM Order o join fetch o.member m join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    public List<SimpleOrderQueryDto> findOrderDtos()
    {
        return em
                .createQuery(
                        "SELECT new jpabook.jpashop.repository.SimpleOrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " FROM Order o join o.member m join o.delivery d", SimpleOrderQueryDto.class)
                .getResultList();
    }

    public List<Order> findAllWithItem() {

        // order id 가 같은 값이면 하나로 취급
        // distinct : db Query에 distinct 날려주고 엔티티 아이디가 중복일 경우 하나 걸름
        // 단점 : 1:N fetch join 하는 순간 페이징 불가능.....

        return em.createQuery(
                "SELECT " +
                            "distinct o " +
                        "FROM " +
                        "   Order o " +
                        "       join fetch o.member m " +
                        "       join fetch o.delivery d " +
                        "       join fetch o.orderItems oi " +
                        "       join fetch oi.item i"
                , Order.class).getResultList();
    }
}
