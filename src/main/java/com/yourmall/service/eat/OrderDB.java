package com.yourmall.service.eat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

public class OrderDB {
	private final static String FILE_PREFIX = "EatingOrder";
	
	public static void Store(OrderManager om, String date, String dir) throws IOException {
		Set<Entry<String, Set<Order>>> dateOrders = om.getDateOrders();
		for (Entry<String, Set<Order>> entry : dateOrders) {
			String eattingDate = entry.getKey();
			if (date != null && eattingDate.compareTo(date) < 0) {
				continue;
			}
			
			if (entry.getValue().size() <= 0) {
				continue;
			}
			
			String fileName = dir + FILE_PREFIX + eattingDate;
			FileWriter fw = new FileWriter(fileName);
			BufferedWriter bw = new BufferedWriter(fw);
			for (Order order : entry.getValue()) {
				bw.write(order.getName());
				bw.newLine();
			}
			bw.close();
		}
	}
	
	public static void Restore(OrderManager om, String date, String dir) throws IOException {		
		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(FILE_PREFIX);
			}
		});
		for (File file : listOfFiles) {
			String fileName = file.getName();
			String eattingDate = fileName.substring(FILE_PREFIX.length());
			if (date != null && eattingDate.compareTo(date) < 0) {
				continue;
			}
			
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String eatterName;
			while ((eatterName = br.readLine()) != null) {
				if (!eatterName.isEmpty()) {
					om.add(new Order(eatterName, eattingDate));
				}
			}
			br.close();
		}
	}
}
