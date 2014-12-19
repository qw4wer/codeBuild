package com.qw4wer.bilibili.entity;

import java.lang.String;
import java.sql.Date;
public class View {
	private String id;
	private String count;
	private Date money;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getCount() {
		return count;
	}

	public void setMoney(Date money) {
		this.money = money;
	}

	public Date getMoney() {
		return money;
	}

}
