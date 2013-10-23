package net.dev.grules.dsl

import groovy.util.logging.Slf4j
import net.dev.grules.dsl.exception.RuleValidationException

import org.apache.commons.lang.StringUtils

@Slf4j
class RulePackage {
	List<RuleSource> sources = []
	private RuleConfiguration config = new RuleConfiguration();

	void setRuleConfiguration(RuleConfiguration config) {
		this.config = config;
	}

	List<Rule> getRules(Map<String, Object> variables) {
		assert sources != null && sources.size() > 0, "Package not initialized with any sources"
		log.debug "Retrieving rules from $sources.size rule sources"

		def packageRules = []

		for (def source : sources) {
			log.trace "Retrieving rules from source $source.name"

			def sourceRules = source.getRules(variables)

			log.trace "Validating retrieved rules"
			validate(source, sourceRules, packageRules)

			packageRules.addAll sourceRules
		}

		log.trace "Extending rules from defined base rules"
		extendRules(packageRules)

		log.trace "Ignoring rules not active for the current date"
		packageRules = excludeInactiveRule(packageRules)

		return packageRules
	}

	private void validate(RuleSource source, List<Rule> sourceRules, List<Rule> packageRules) {
		for (Rule rule : sourceRules) {
			def errors = []
			if(StringUtils.isBlank(rule.name)) {
				errors << "Mandatory field 'name' is not defined"
			}

			if(rule.whenExpression == null) {
				errors << "Mandatory clause 'when' is not defined"
			}

			if(rule.thenExpression == null ) {
				errors << "Mandatory clause 'then' is not defined"
			}

			if(rule.activeFrom != null && rule.activeTo != null && rule.activeFrom > rule.activeTo) {
				errors << "Date 'activeFrom' is defined after date 'activeTo'"
			}

			if(packageRules.count { it.name == rule.name } > 0) {
				errors << "Rule $rule.name is already defined in a previous script"
			}

			if(sourceRules.count { it.name == rule.name } > 1) {
				errors << "Rule $rule.name is already defined in the current script"
			}

			if(!errors.empty) {
				throw new RuleValidationException(rule.name, source.name, errors)
			}
		}
	}

	private void extendRules(List<Rule> packageRules) {
		def inheritenceChain = [:]
		def packageRulesMap = [:]
		for (Rule rule : packageRules) {
			packageRulesMap.put(rule.name, rule)
		}

		for (Rule rule : packageRules) {
			// TODO: implement circular extension validation, multipass

			if(StringUtils.isNotBlank(rule.extend)) {
				Rule baseRule = packageRulesMap[rule.extend]

				if(baseRule == null) {
					throw new RuleValidationException(rule.name, "", [
						"Base rule ($rule.extend) on rule '$rule.name' not found"
					])
				}

				rule.oldWhen = rule.whenExpression
				rule.oldThen = rule.thenExpression


				rule.when { baseRule.whenExpression(); rule.oldWhen.call(); }
				rule.then {baseRule.thenExpression(); rule.oldThen.call(); }
			}
		}
	}

	private List<Rule> excludeInactiveRule(List<Rule> packageRules) {
		return packageRules.findAll {
			def isActive = (it.activeFrom == null || it.activeFrom <= config.currentDate) && (it.activeTo == null || it.activeTo >= config.currentDate)
			if(!isActive) log.trace "Rule $it.name is not active for current date($config.currentDate), excluding from calculation..."

			isActive
		}
	}

	RulePackage addSource(RuleSource source) {
		sources << source
		this
	}
}
