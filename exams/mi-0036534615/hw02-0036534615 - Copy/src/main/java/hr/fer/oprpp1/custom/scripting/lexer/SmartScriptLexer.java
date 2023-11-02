package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

/**
 * 
 * Lexer class designed to return tokens and next tokens of type {@link SmartScriptToken}
 * @author Marko Kremer
 *
 */
public class SmartScriptLexer {

	/**
	 * Input data type char array
	 */
	private char[] data;
	
	/**
	 * Instance of {@link SmartScriptToken}, represents the last generated token
	 */
	private SmartScriptToken token;
	
	/**
	 * Current state of the lexer
	 */
	private SmartScriptLexerState currentState;
	
	/**
	 * int that represents current position in the data array
	 */
	private int currentIndex;
	
	
	/**
	 * Constructor for {@link SmartScriptLexer}, creates character array, sets default state, and currentIndex to 0
	 * 
	 * @param documentBody of type {@link String}
	 */
	public SmartScriptLexer(String documentBody) {
		this.data = documentBody.toCharArray();
		currentState = SmartScriptLexerState.STANDARD;
		currentIndex = 0;
	}
	
	/**
	 * 
	 * @return next generated token type {@link SmartScriptToken}
	 * @throws SmartScriptParserException when is called and the current token is {@link SmartScriptTokenType} EOF, or an illegal escape "\" has happened
	 */
	public SmartScriptToken nextToken() {
		
		if(token != null && token.getType() == SmartScriptTokenType.EOF) {
			throw new SmartScriptParserException();
		}
		
		String tokenString = "";
		
		
		SmartScriptTokenType currentTokenType = null;
		
		boolean firstQuotationMark = true; 
		boolean firstSlash = true;
		
		while(currentIndex < data.length) {
			char currentChar = data[currentIndex];
			boolean startOfTag = currentChar == '{' && currentIndex + 1 < data.length && data[currentIndex+1] == '$';
			boolean endOfTag = currentChar == '$' && currentIndex + 1 < data.length && data[currentIndex+1] == '}';
			if(startOfTag && firstSlash) {
				if(tokenString == "") {
					token = new SmartScriptToken(SmartScriptTokenType.START_TAG, "{$");
					currentIndex += 2;
					currentState =  SmartScriptLexerState.TAG_INSIDE;
					return token;
				} else {
					token = new SmartScriptToken(SmartScriptTokenType.TEXT, tokenString);
					return token;
				}
			} else if(startOfTag) {
				throw new SmartScriptParserException();
			} else if(currentState == SmartScriptLexerState.TAG_INSIDE) {
				
				if(endOfTag && tokenString != "") {
					token = new SmartScriptToken(currentTokenType, tokenString);
					return token;
				} else if(endOfTag) {
					token = new SmartScriptToken(SmartScriptTokenType.END_TAG, "$}");
					currentState = SmartScriptLexerState.STANDARD;
					currentIndex += 2;
					return token;
				} else if(currentChar == ' ' && !firstQuotationMark) {
					tokenString += currentChar;
					currentIndex++;
				} else if(currentChar == ' ' && tokenString == "") {
					currentIndex++;
				} else if(currentChar == ' ') {
					token = new SmartScriptToken(currentTokenType, tokenString);
					currentIndex++;
					return token;
				}	else if((currentChar == 'n' || currentChar == 'r' || currentChar == 't' || currentChar == '\"') && !firstQuotationMark && !firstSlash) {
						firstSlash = true;
						switch(currentChar) {
						case 'n':
							tokenString += "\\n";
							break;
						case 'r':
							tokenString += "\\r";
							break;
						case 't':
							tokenString += "\\t";
							break;
						case '\"':
							tokenString += "\\\"";
							break;
							
						} 
						currentIndex++;
				} else if(Character.isLetter(currentChar) && !firstQuotationMark) {
					currentTokenType = SmartScriptTokenType.STRING;
					tokenString += currentChar;
					currentIndex++;
				} else if(Character.isLetter(currentChar) && tokenString == "" && currentTokenType != SmartScriptTokenType.FUNCTION) {
					currentTokenType = SmartScriptTokenType.VARIABLE;
					tokenString += currentChar;
					currentIndex++;
				} else if(Character.isLetter(currentChar) && (currentTokenType == SmartScriptTokenType.INTEGER || currentTokenType == SmartScriptTokenType.DOUBLE)) {
					token = new SmartScriptToken(currentTokenType, tokenString);
					return token;
				} else if(Character.isLetter(currentChar)) {
					tokenString += currentChar;
					currentIndex++;
				} else if(Character.isDigit(currentChar) && !firstQuotationMark) {
					currentTokenType = SmartScriptTokenType.STRING;
					tokenString+= currentChar;
					currentIndex++;
				} else if(Character.isDigit(currentChar) && (tokenString == "" || currentTokenType == SmartScriptTokenType.INTEGER || currentTokenType == SmartScriptTokenType.DOUBLE)) {
					if(currentIndex+2<data.length && data[currentIndex+1] == '.' && Character.isDigit(data[currentIndex+2])) {
						currentTokenType = SmartScriptTokenType.DOUBLE;
						tokenString += currentChar;
						tokenString += data[++currentIndex];
						tokenString += data[++currentIndex];
					} else {
						currentTokenType = SmartScriptTokenType.INTEGER;
						tokenString += currentChar;
					}
					currentIndex++; 
				} else if(Character.isDigit(currentChar) && currentIndex+2<data.length && data[currentIndex+1] == '.' && Character.isDigit(data[currentIndex+2])) {
					currentTokenType = SmartScriptTokenType.DOUBLE;
					tokenString += currentChar;
					tokenString += data[++currentIndex];
					tokenString += data[++currentIndex];
					currentIndex++;
				} else if(Character.isDigit(currentChar) && currentTokenType == SmartScriptTokenType.VARIABLE) {
					tokenString += currentChar;
					currentIndex++;
				} else if(currentChar == '-' && currentIndex+1 < data.length && Character.isDigit(data[currentIndex+1])) {
					if(tokenString == "" ) {
						currentTokenType = SmartScriptTokenType.INTEGER;
						tokenString += currentChar;
						tokenString += data[++currentIndex];
						currentIndex++;
					} else {
						token = new SmartScriptToken(currentTokenType, tokenString);
						return token;
					}
				} else if((currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/' || currentChar == '^' || currentChar == '=') && tokenString == "") {
					currentTokenType = SmartScriptTokenType.OPERATOR;
					token = new SmartScriptToken(SmartScriptTokenType.OPERATOR, String.valueOf(currentChar));
					currentIndex++;
					return token;
				} else if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/' || currentChar == '^' || currentChar == '=') {
					token = new SmartScriptToken(currentTokenType, tokenString);
					return token;
				} else if(currentChar == '@') {
					currentTokenType = SmartScriptTokenType.FUNCTION;
					currentIndex++;
				} else if(currentChar == '\"' && firstQuotationMark && tokenString == "") {
					firstQuotationMark = false;
					tokenString += "\"";
					currentTokenType = SmartScriptTokenType.STRING;
					currentIndex++;
				} else if(currentChar == '\"' && firstQuotationMark) {
					token = new SmartScriptToken(currentTokenType, tokenString);
					return token;
				} else if(currentChar == '\"' && !firstQuotationMark && !firstSlash) {
					firstSlash = true;
					tokenString += currentChar;
					currentIndex++;
				}
				else if(currentChar == '\"' && !firstQuotationMark) {
					firstQuotationMark = true;
					tokenString += "\"";
					token = new SmartScriptToken(SmartScriptTokenType.STRING, tokenString);
					currentIndex++;
					return token;
				} else if(currentChar == '\\' && firstQuotationMark) {
					throw new SmartScriptParserException();
				} else if(currentChar == '\\' && !firstQuotationMark ) {
					if(firstSlash) {
						firstSlash = false;
						currentIndex++;
					} else {
						firstSlash = true;
					}
				} else if(!firstSlash) {
					throw new SmartScriptParserException();
				}
				else {
					tokenString += currentChar;
					currentIndex++;
				} 
			} else if(currentChar == '\\') {
				currentChar = data[++currentIndex];
				if(currentChar != '\\' && currentChar != '{') {
					throw new SmartScriptParserException();
				} else {
					tokenString += "\\" + currentChar;
					currentIndex++;
				}
			}
			else {
				tokenString += currentChar;
				currentIndex++;
			}
		}
		if(tokenString == "") {
			token = new SmartScriptToken(SmartScriptTokenType.EOF, null);
		} else{ 
			token = new SmartScriptToken(SmartScriptTokenType.TEXT, tokenString);
		}
		
		return token;

	}
	
	/**
	 * 
	 * @return current token type {@link SmartScriptToken}
	 */
	public SmartScriptToken getToken() {
		return token;
	}
}
