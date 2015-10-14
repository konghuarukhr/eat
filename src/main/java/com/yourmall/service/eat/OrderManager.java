package com.yourmall.service.eat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class OrderManager {
	private final static OrderComparator OC = new OrderComparator();
	private final static OrderComparatorRD OCRD = new OrderComparatorRD();
	private Map<String, Order> allOrders;
	private SortedMap<String, Set<Order>> personalOrders;
	private SortedMap<String, Set<Order>> dateOrders;
	
	public OrderManager() {
		allOrders = new HashMap<String, Order>();
		personalOrders = new TreeMap<String, Set<Order>>();
		dateOrders = new TreeMap<String, Set<Order>>();
	}
	
	public Boolean contains(Order order) {
		String eatterName = order.getName();
		String eattingDate = order.getDate();
		
		String key = eatterName + '@' + eattingDate;
		return allOrders.containsKey(key);
	}
	
	public void add(Order order) {
		String eatterName = order.getName();
		String eattingDate = order.getDate();
		
		String key = eatterName + '@' + eattingDate;
		if (allOrders.containsKey(key)) {
			return;
		} else {
			allOrders.put(key, order);
		}
		
		Set<Order> byName;
		if ((byName = personalOrders.get(eatterName)) == null) {
			byName = new HashSet<Order>();
			personalOrders.put(eatterName, byName);
		}
		byName.add(order);

		Set<Order> byDate;
		if ((byDate = dateOrders.get(eattingDate)) == null) {
			byDate = new HashSet<Order>();
			dateOrders.put(eattingDate, byDate);
		}
		byDate.add(order);
	}
	
	public void remove(Order order) {
		String eatterName = order.getName();
		String eattingDate = order.getDate();
		
		String key = eatterName + '@' + eattingDate;
		if (!allOrders.containsKey(key)) {
			return;
		} else {
			order = allOrders.remove(key);
		}
		
		Set<Order> byName = personalOrders.get(eatterName);
		byName.remove(order);
		if (byName.isEmpty()) {
			personalOrders.remove(eatterName);
		}
		
		Set<Order> byDate = dateOrders.get(eattingDate);
		byDate.remove(order);
		if (byDate.isEmpty()) {
			dateOrders.remove(eattingDate);
		}
	}
	
	public Set<Order> getPersonalOrders(String eatterName) {
		return personalOrders.get(eatterName);
	}
	
	public SortedSet<Order> getDateOrders(String eattingDate) {
		SortedSet<Order> retOrders = new TreeSet<Order>(OC);
		Set<Order> orders = dateOrders.get(eattingDate);
		if (orders != null) {
			retOrders.addAll(orders);
		}
		return retOrders;
	}
	
	public Set<Entry<String, Set<Order>>> getPersonalOrders() {
		return personalOrders.entrySet();
	}
	
	public Set<Entry<String, Set<Order>>> getDateOrders() {
		return dateOrders.entrySet();
	}
	
	public SortedSet<Order> getDateOrdersBeforeRD(String eattingDate) {
		SortedSet<Order> retOrders = new TreeSet<Order>(OCRD);
		Set<Entry<String, Set<Order>>> orders = dateOrders.entrySet();
		for (Entry<String, Set<Order>> order : orders) {
			if (order.getKey().compareTo(eattingDate) < 0) {
				retOrders.addAll(order.getValue());
			}
		}
		return retOrders;
	}
	
	public SortedSet<Order> getDateOrdersAfter(String eattingDate) {
		SortedSet<Order> retOrders = new TreeSet<Order>(OC);
		Set<Entry<String, Set<Order>>> orders = dateOrders.entrySet();
		for (Entry<String, Set<Order>> order : orders) {
			if (order.getKey().compareTo(eattingDate) > 0) {
				retOrders.addAll(order.getValue());
			}
		}
		return retOrders;
	}
}
