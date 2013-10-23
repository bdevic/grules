package net.dev.grules.dsl

import groovy.util.logging.Slf4j

import org.codehaus.groovy.control.CompilerConfiguration

abstract class RuleDslScript extends Script {
	List<Rule> rules = []

	def rule(Closure closure) {
		def newRule = new Rule()
		rules << newRule

		closure.delegate = newRule
		closure.resolveStrategy = Closure.DELEGATE_FIRST

		closure()
	}
}

@Slf4j
class ScriptRuleSource implements RuleSource {
	String name
	GroovyCodeSource script

	public ScriptRuleSource(String name, GroovyCodeSource script) {
		this.name = name
		this.script = script
	}

	public void setRuleConfiguration(RuleConfiguration config) {
	}

	public List<Rule> getRules(Map<String, Object> variables) {
		log.debug "Compiling rule script '$name'..."
		def config = new CompilerConfiguration()
		config.scriptBaseClass = RuleDslScript.name
		def script = new GroovyShell(config).parse(script)

		script.binding = new Binding()
		for (def variable : variables) {
			script.binding.setProperty(variable.key, variable.value)
		}

		log.debug "Executing script '$name'..."
		script.run();

		return script.rules;
	}
}
