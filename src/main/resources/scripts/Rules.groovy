package net.croz

net.croz.groovy.rules.dsl.Message message = message
net.dev.grules.dsl.validation.ValidationContext context = context

rule {
	name "NP000"
	description "Only message CD4001 filter"

	when {
		message.name == "CD4001"
	}
	
	then {}
}

rule {
	extend "NP000"
	name "NP001"
	description "All items must have an amount larger than zero"

	when {
		$store = message.items[1].amount
		message.items.count { it.amount <= 0 } > 0
	}

	then { 
		println "Rule NP001 not satisfied"
		context.fail name, "message.items.amount", "amount.zero"
	}
}