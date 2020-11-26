package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Log4j2
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item)
    {
        if (item.getId() == null)
        {
            System.out.println("em.persist(item)");
            log.debug("em.persist(item)");
            em.persist(item);

        }
        else
        {
            System.out.println("em.merge(item)");
            log.debug("em.merge(item)");
            em.merge(item); // update 비슷함
       }
    }

    public Item findOne(Long id)
    {
        return em.find(Item.class, id);
    }

    public List<Item> findAll()
    {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
