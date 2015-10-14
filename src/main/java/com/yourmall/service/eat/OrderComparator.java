package com.yourmall.service.eat;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
	@Override
	public int compare(Order o1, Order o2) {
		if (o1.getDate().equals(o2.getDate())) {
			return o1.getName().compareTo(o2.getName());
		} else {
			return o1.getDate().compareTo(o2.getDate());
		}
	}
}
