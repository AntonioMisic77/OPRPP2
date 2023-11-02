package hr.fer.oprpp1.custom.scripting.lexer;

public class LexerTestMain {

	public static void main(String[] args) {
		SmartScriptLexer lexer;
		lexer = new SmartScriptLexer("A tag follows {$= \"J \\\n \\\"L\\\" S\"$}. ");
		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();
		lexer.nextToken();

	}

}
