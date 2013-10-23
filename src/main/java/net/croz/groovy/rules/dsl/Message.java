package net.croz.groovy.rules.dsl;

import java.util.List;

public class Message {
	
	
	public Message(String name) {
		super();
		this.name = name;
	}
	
	private List<MessageItem> items;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	public List<MessageItem> getItems() {
		return items;
	}
	
	public void setItems(List<MessageItem> items) {
		this.items = items;
	}
	
}
