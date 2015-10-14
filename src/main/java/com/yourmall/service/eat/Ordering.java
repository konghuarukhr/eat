package com.yourmall.service.eat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

public class Ordering {	
	public static String add(OrderManager om, String eatterName, String eattingDate, Properties props) {
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String nowStr = df.format(now.getTime());
		
		int comp = nowStr.compareTo(eattingDate);
		if (comp < 0) {
			om.add(new Order(eatterName, eattingDate));
			return null;
		} else if (comp > 0) {
			return "今天几号了？";
		} else {
			return add(om, eatterName, props);
		}
	}
	
	public static String remove(OrderManager om, String eatterName, String eattingDate, Properties props) {
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String nowStr = df.format(now.getTime());
		
		int comp = nowStr.compareTo(eattingDate);
		if (comp < 0) {
			om.remove(new Order(eatterName, eattingDate));
			return null;
		} else if (comp > 0) {
			return "你都吃过了！还想取消？";
		} else {
			return remove(om, eatterName, props);
		}
	}
	
	public static String add(OrderManager om, String eatterName, Properties props) {
		int deadline = Integer.parseInt(props.getProperty("deadline"));
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.HOUR_OF_DAY) >= deadline) {
			return "订餐邮件已发送，请在每天" + deadline + "点前订餐";
		}
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String eattingDate = df.format(now.getTime());
		om.add(new Order(eatterName, eattingDate));
		return null;
	}
	
	public static String remove(OrderManager om, String eatterName, Properties props) {
		int deadline = Integer.parseInt(props.getProperty("deadline"));
		Calendar now = Calendar.getInstance();
		if (now.get(Calendar.HOUR_OF_DAY) >= deadline) {
			return "订餐邮件已发送，请在每天" + deadline + "点前取消订餐";
		}
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String eattingDate = df.format(now.getTime());
		om.remove(new Order(eatterName, eattingDate));
		return null;
	}
}
