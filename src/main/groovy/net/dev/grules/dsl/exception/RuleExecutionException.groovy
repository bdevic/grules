package net.dev.grules.dsl.exception

class RuleExecutionException extends RuntimeException {
	String ruleName
	public RuleExecutionException(String ruleName, String message, Throwable innerException) {
		super(message, innerException);
		this.ruleName = ruleName;
	}
}
