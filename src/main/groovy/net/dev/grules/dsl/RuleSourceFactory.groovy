package net.dev.grules.dsl

class RuleSourceFactory {
	static String DEFAULT_ENCODING ="UTF-8";

	static RuleSource fromFileScript(File file) {
		return new ScriptRuleSource(file.toURI().toURL().toExternalForm(), new GroovyCodeSource(file, DEFAULT_ENCODING))
	}

	static RuleSource fromClassPathScript(String path)  {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream( path );
		if ( is == null ) {
			is = Class.class.getClassLoader().getSystemClassLoader().getResourceAsStream( path );
		}

		if ( is == null ) {
			throw new FileNotFoundException( "'" + path + "' cannot be opened because it does not exist" );
		}

		return fromReader(path, new BufferedReader(new InputStreamReader(is, DEFAULT_ENCODING)));
	}

	static RuleSource fromString(String name, String script) {
		return new ScriptRuleSource(name, new GroovyCodeSource(script, name, GroovyShell.DEFAULT_CODE_BASE));
	}

	static RuleSource fromReader(String name, Reader reader) {
		return new ScriptRuleSource(name, new GroovyCodeSource(reader, name, GroovyShell.DEFAULT_CODE_BASE));
	}
}
