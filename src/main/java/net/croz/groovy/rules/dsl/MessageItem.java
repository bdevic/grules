package net.croz.groovy.rules.dsl;

import java.math.BigDecimal;

public class MessageItem {
	
	
	public MessageItem(BigDecimal amount) {
		super();
		this.amount = amount;
	}

	public BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
}
