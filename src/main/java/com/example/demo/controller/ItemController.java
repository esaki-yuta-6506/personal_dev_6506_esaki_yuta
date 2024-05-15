package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Item;
import com.example.demo.entity.Review;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.ShopRepository;

@Controller
public class ItemController {

	@Autowired
	ItemRepository itemRepository;

	@Autowired
	ShopRepository shopRepository;

	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ReviewRepository reviewRepository;

	@GetMapping("/items")
	public String index(
			@RequestParam(name = "categoryId", defaultValue = "") Integer categoryId,
			@RequestParam(name = "keyword", defaultValue = "") String keyword,
			@RequestParam(name = "minprice", defaultValue = "") Integer minprice,
			@RequestParam(name = "maxprice", defaultValue = "") Integer maxprice,
			Model model) {

		List<Item> items = new ArrayList<Item>();
		String msg = "";

		if (minprice != null && maxprice != null && maxprice < minprice) {
			msg = "最高金額には最低金額よりも大きい値を入力してください";
			items = itemRepository.findAll();
		} else {
			items = findItems(categoryId, keyword, minprice, maxprice);
		}

		String url = "";
		
		for(Item item : items) {
			item.setUrl();
		}
		
		if(keyword != null) {
			url += "&keyword=" + keyword;
		}
		if(minprice != null) {
			url += "&minprice=" + minprice;
		}
		if(maxprice != null) {
			url += "&minprice=" + maxprice;
		}
		
		if(items.size() == 0)
			msg = "条件に当てはまる商品はありませんでした";
		
		model.addAttribute("msg", msg);
		model.addAttribute("categoryId", categoryId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("minprice", minprice);
		model.addAttribute("maxprice", maxprice);
		model.addAttribute("items", items);
		model.addAttribute("url", url);
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("shops", shopRepository.findAll());

		return "items";
	}
	
	@GetMapping("/items/{id}")
	public String view(
			@PathVariable("id")Integer id,
			Model model) {
		
		Item item = itemRepository.findOneById(id);
		List<Review> reviews = reviewRepository.findByItemId(id);
		
		model.addAttribute("item", item);
		model.addAttribute("reviews", reviews);
		
		return "itemDetail";
	}

	private List<Item> findItems(Integer categoryId, String keyword, Integer minprice, Integer maxprice) {
		List<Item> items;
		if (categoryId != null) {
			if (keyword != null) {
				if (minprice != null) {
					if (maxprice != null) {
						items = itemRepository
								.findByCategoryIdAndNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqual(
										categoryId, keyword, minprice, maxprice);
					} else {
						items = itemRepository.findByCategoryIdAndNameContainingAndPriceGreaterThanEqual(categoryId,
								keyword, minprice);
					}

				} else {
					if (maxprice != null) {
						items = itemRepository.findByCategoryIdAndNameContainingAndPriceLessThanEqual(categoryId,
								keyword, maxprice);
					} else {
						items = itemRepository.findByCategoryIdAndNameContaining(categoryId, keyword);
					}
				}
			} else {

				if (minprice != null) {
					if (maxprice != null) {
						items = itemRepository.findByCategoryIdAndPriceGreaterThanEqualAndPriceLessThanEqual(categoryId,
								minprice, maxprice);
					} else {
						items = itemRepository.findByCategoryIdAndPriceGreaterThanEqual(categoryId, minprice);
					}

				} else {
					if (maxprice != null) {
						items = itemRepository.findByCategoryIdAndPriceLessThanEqual(categoryId, maxprice);
					} else {
						items = itemRepository.findByCategoryId(categoryId);
					}
				}
			}
		} else {
			if (keyword != null) {
				if (minprice != null) {
					if (maxprice != null) {
						items = itemRepository.findByNameContainingAndPriceGreaterThanEqualAndPriceLessThanEqual(
								keyword, minprice, maxprice);
					} else {
						items = itemRepository.findByNameContainingAndPriceGreaterThanEqual(keyword, minprice);
					}

				} else {
					if (maxprice != null) {
						items = itemRepository.findByNameContainingAndPriceLessThanEqual(keyword, maxprice);
					} else {
						items = itemRepository.findByNameContaining(keyword);
					}
				}
			} else {

				if (minprice != null) {
					if (maxprice != null) {
						items = itemRepository.findByPriceGreaterThanEqualAndPriceLessThanEqual(minprice, maxprice);
					} else {
						items = itemRepository.findByPriceGreaterThanEqual(minprice);
					}

				} else {
					if (maxprice != null) {
						items = itemRepository.findByPriceLessThanEqual(maxprice);
					} else {
						items = itemRepository.findAll();
					}
				}
			}
		}
		return items;
	}
}
