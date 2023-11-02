package hr.fer.oprpp1.custom.scripting.lexer;


/**
 * Enumeration to follow {@link SmartScriptLexer} states
 * 
 * @author Marko Kremer
 *
 */
public enum SmartScriptLexerState {
	STANDARD, FOR_TAG, ECHO_TAG, TAG_START, TAG_INSIDE, 
}
