package net.dev.grules.dsl

class Rule {
	private String name
	private String description
	private Date activeFrom
	private Date activeTo
	private String extend
	private Map<String, Object> storage = [:]

	Closure whenExpression
	Closure thenExpression

	def propertyMissing(String name, value) {
		storage[name] = value
	}
	def propertyMissing(String name) {
		storage[name]
	}


	def name(String name) {
		this.name = name;
	}

	def description(String description) {
		this.description = description;
	}

	def activeFrom(Date activeFrom) {
		this.activeFrom = activeFrom;
	}

	def activeFrom(String activeFrom) {
		this.activeFrom = Date.parse(RuleConfiguration.dateFormat, activeFrom)
	}

	def activeTo(Date activeTo) {
		this.activeTo = activeTo;
	}

	def activeTo(String activeTo) {
		this.activeTo = Date.parse(RuleConfiguration.dateFormat, activeTo)
	}


	def extend(String extend) {
		this.extend = extend
	}

	def when(Closure closure) {
		this.whenExpression = closure;
	}

	def then(Closure closure) {
		this.thenExpression = closure;
	}
}




