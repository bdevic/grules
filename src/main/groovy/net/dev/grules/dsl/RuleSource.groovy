package net.dev.grules.dsl

interface RuleSource {
	String getName()
	void setRuleConfiguration(RuleConfiguration config)
	List<Rule> getRules(Map<String, Object> variables)
}
