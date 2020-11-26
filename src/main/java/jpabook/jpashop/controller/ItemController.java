package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private  final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model)
    {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form)
    {
        Book book= new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/";
    }

    @GetMapping("/items")
    public String list(Model model)
    {
        List<Item> itemList = itemService.findItems();
        model.addAttribute("items", itemList);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable(value = "itemId") Long itemId, Model model)
    {
        Book book = (Book) itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setStockQuantity(book.getStockQuantity());
        form.setPrice(book.getPrice());
        form.setName(book.getName());
        form.setAuthor(book.getAuthor());
        form.setIsbn(book.getIsbn());
        form.setId(book.getId());
        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form, Model model){
        Book book = new Book();
        book.setIsbn(form.getIsbn());
        book.setAuthor(form.getAuthor());
        book.setStockQuantity(form.getStockQuantity());
        book.setPrice(form.getPrice());
        book.setName(form.getName());
        book.setId(form.getId());

        itemService.saveItem(book);

        model.addAttribute("form", form);
        return "redirect:/items";
    }
}
