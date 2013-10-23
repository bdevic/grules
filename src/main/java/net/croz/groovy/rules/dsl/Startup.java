package net.croz.groovy.rules.dsl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import net.dev.grules.dsl.RuleConfiguration;
import net.dev.grules.dsl.RuleExecutor;
import net.dev.grules.dsl.RulePackage;
import net.dev.grules.dsl.RuleSourceFactory;
import net.dev.grules.dsl.validation.ValidationContext;

public class Startup {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Message msg = new Message("CD4001");
		msg.setItems(new ArrayList<MessageItem>());
		msg.getItems().add(new MessageItem(BigDecimal.valueOf(0)));
		msg.getItems().add(new MessageItem(BigDecimal.valueOf(10)));

		RuleConfiguration.setDateFormat("dd/MM/yyyy");
		RulePackage rulePackage = new RulePackage();
		rulePackage.addSource(RuleSourceFactory
				.fromClassPathScript("scripts\\Rules.groovy"));
		HashMap<String, Object> variables = new HashMap<String, Object>();
		variables.put("message", msg);
		variables.put("context", new ValidationContext());
		new RuleExecutor(rulePackage).execute(variables);
	}

}
