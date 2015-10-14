package com.yourmall.service.eat;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class PeriodTask implements ServletContextListener {
	private ScheduledExecutorService sched;
	private OrderManager om;
	private Properties props;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		ctx.log("Task started");
		
		String dir = ctx.getRealPath("/");
		
		sched = Executors.newSingleThreadScheduledExecutor();
		om = new OrderManager();
		props = new Properties();
		try {
			InputStream stream = ctx.getResourceAsStream("config.properties");
			if (stream == null) {
				throw new IOException("no resource exists at the specified path");
			}
			props.load(stream);
		} catch (IOException e) {
			ctx.log("Get config failed!", e);
		}
		
		ctx.setAttribute("om", om);
		ctx.setAttribute("props", props);
		
		try {
			OrderDB.Restore(om, null, dir);
		} catch (IOException e) {
			ctx.log("Restore orders failed!", e);
		}
		
        sched.schedule(new DoTask(sched, om, props, ctx), DoTask.calcDelay(props.getProperty("deadline")), TimeUnit.MILLISECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ServletContext ctx = sce.getServletContext();
		String dir = ctx.getRealPath("/");
		
		sched.shutdownNow();
		try {
			Calendar now = Calendar.getInstance();
			DateFormat df = new SimpleDateFormat("yyyyMMdd");
			String nowStr = df.format(now.getTime());
			OrderDB.Store(om, nowStr, dir);
		} catch (IOException e) {
			ctx.log("Store orders failed!", e);
		}
		
		ctx.log("Task stoped");
	}
}

class DoTask implements Runnable {
	private final static String NEWLINE = "\r\n";
	
	private ScheduledExecutorService sched;
	private OrderManager om;
	private Properties props;
	private ServletContext ctx;
	
	public DoTask(ScheduledExecutorService sched, OrderManager om, Properties props, ServletContext ctx) {
		this.sched = sched;
		this.om = om;
		this.props = props;
		this.ctx = ctx;
	}
	
	@Override
	public void run() {
		String dir = ctx.getRealPath("/");
		
		Calendar now = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		String nowStr = df.format(now.getTime());
		
		ctx.log("Do period task on " + nowStr);
		
		Set<Order> orders = om.getDateOrders(nowStr);
		if (orders != null && orders.size() > 0) {
			StringBuilder sb = new StringBuilder(props.getProperty("begin_text", ""));
			sb.append(NEWLINE);
			for (Order order : orders) {
				sb.append(order.getName());
				sb.append(NEWLINE);
			}
			sb.append(props.getProperty("end_text", ""));
			try {
				String from = props.getProperty("from", null);
				String to = props.getProperty("to");
				String cc = props.getProperty("cc", null);
				SendMail.doSend(from, to.split(","), (cc == null ? null : cc.split(",")), props.getProperty("subject"), sb.toString());
				ctx.log("send from: " + (from == null ? "null" : from));
				ctx.log("send to: " + to);
				ctx.log("send cc: " + (cc == null ? "null" : cc));
				ctx.log("send text: " + sb.toString());
			} catch (AddressException e) {
				ctx.log("Send email failed!", e);
			} catch (MessagingException e) {
				ctx.log("Send email failed!", e);
			}
		}
		
		try {
			OrderDB.Store(om, nowStr, dir);
		} catch (IOException e) {
			ctx.log("Store orders failed!", e);
		}
		
		long delay = calcDelay(props.getProperty("deadline"));
		ctx.log("do next task after " + delay + " ms");
		sched.schedule(new DoTask(sched, om, props, ctx), delay, TimeUnit.MILLISECONDS);
	}
	
	public static long calcDelay(String deadline) {
		Calendar now = Calendar.getInstance();

		Calendar sendTime = (Calendar) now.clone();
		sendTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(deadline));
		sendTime.set(Calendar.MINUTE, 0);
		sendTime.set(Calendar.SECOND, 0);
		sendTime.set(Calendar.MILLISECOND, 0);
		
		if (now.after(sendTime)) {
			sendTime.add(Calendar.DAY_OF_MONTH, 1);
			return sendTime.getTimeInMillis() - now.getTimeInMillis();
		} else {
			return sendTime.getTimeInMillis() - now.getTimeInMillis();
		}
	}
}
