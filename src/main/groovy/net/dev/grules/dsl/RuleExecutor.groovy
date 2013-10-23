package net.dev.grules.dsl

import groovy.util.logging.Slf4j
import net.dev.grules.dsl.exception.RuleExecutionException

@Slf4j
class RuleExecutor {
	RulePackage rulePackage

	public RuleExecutor(RulePackage rulePackage) {
		super();
		this.rulePackage = rulePackage;
	}

	def execute(Map<String, Object> variables) {
		log.debug "Retrieving rules from the associated rule package"
		def rules = rulePackage.getRules(variables)

		Rule currentRule;
		try {
			for (def rule : rules) {
				currentRule = rule;

				log.trace "Executing rule '$rule.name'"

				if(rule.whenExpression != null && rule.whenExpression()) {
					log.trace "Rule '$rule.name' was satisfied, executing then clause"

					rule.thenExpression();
				}
			}
		}
		catch(Exception e) {
			throw new RuleExecutionException(currentRule?.name, "Exception executing rule clause", e)
		}
	}
}
