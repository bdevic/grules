package net.dev.grules.dsl.validation

import java.util.Map;

class ValidationError {
	String errorCode
	String errorPointer
	String defaultMessage
	Object[] errorArgs
}

class ValidationContext {
	private def storage = [:]
	private def errors = Collections.synchronizedList( new ArrayList<ValidationError>() )
	
	def propertyMissing(String name, value) { storage[name] = value }
	def propertyMissing(String name) { storage[name] }

	String getStatus() {
		errors.isEmpty() ? "OK" : "INVALID"
	}

	List<ValidationError> getErrors() {
		Collections.unmodifiableList( errors )
	}

	void fail( final String errorCode ) {
		fail( errorCode, null, null )
	}

	void fail( final String errorCode, final String errorPointer, final String defaultMessage, final Object...errorArgs ) {
		errors << new ValidationError( errorCode: errorCode, errorPointer: errorPointer, defaultMessage: defaultMessage, errorArgs: errorArgs )
	}

	boolean hasErrors() {
		errors.size() > 0
	}
}
