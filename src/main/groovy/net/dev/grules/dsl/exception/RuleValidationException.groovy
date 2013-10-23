package net.dev.grules.dsl.exception

import groovy.transform.Immutable;

@Immutable
class RuleValidationException extends RuntimeException {
	String ruleName;
	String scriptName;
	List<String> errors;
}
