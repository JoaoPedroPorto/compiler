package compiler;

import java.io.EOFException;
import java.io.IOException;

public class Lexicon {

	private String fileName;
	private FileLoader fLoader;
	private StringBuilder lexeme;
	private char character;
	private long line;
	private long column;
	private Token token;
	private TabSymbols tabSymbols = TabSymbols.getInstance();
	private ErrorHandler errors = ErrorHandler.getInstance();

	public Lexicon(String fileName) throws IOException {
		this.fileName = fileName;
		fLoader = new FileLoader(fileName);
	}

	public Token nextToken() throws IOException {
		lexeme = new StringBuilder();
		line = fLoader.getLine();
		column = fLoader.getColumn();

		try{
			character = fLoader.getNextChar();

		}catch (Exception ex){
			return  token_EOF();
		}


		while (Character.isWhitespace(character)) {
			try {
				character = fLoader.getNextChar();
			} catch (Exception e) {
				return  token_EOF();
			}
		}

		switch (character) {
			case '+':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '-':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '*':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '/':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '<':
				token = process_ATTRIB();
				break;
			case ';':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '(':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case ')':
				lexeme.append(character);
				token = tabSymbols.addID(lexeme.toString(), line, column);
				break;
			case '&':
				token = process_RELOP();
				break;
			case '\'':
				token = process_LITERAL();
				break;
			case '#':
				ignoreComments();
				token = this.nextToken();
				break;
			default:
				if (Character.isDigit(character)) {
					token = process_NUM_INT();
					break;
				}
				if (Character.isLetter(character) || character == '_') {
					token = process_ID();
					break;
				}
				lexeme.append(character);
				errors.addErro("Caracter invalido", lexeme.toString(), line, column);
				this.nextToken();
		}
		return token;
	}

	private Token process_LITERAL() throws IOException {
		lexeme.append(character);
		try {
			while (true) {

				character = fLoader.getNextChar();

				if (character == '\'') {
					lexeme.append(character);
					return new Token(TokenType.LITERAL, lexeme.toString(), line, column);
				}
				lexeme.append(character);
			}

		} catch (EOFException e) {
			lexeme.append(character);
			errors.addErro("Finalizado durante literal, faltando fechar aspa.", lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	private Token process_ID() throws IOException{
		lexeme.append(character);
		try {

			while (true) {

				character = fLoader.getNextChar();

				if (!Character.isLetter(character) && !Character.isDigit(character) && character != '_') {
					fLoader.resetLastChar();
					return tabSymbols.addID(lexeme.toString(), line, column);
				}
					lexeme.append(character);
			}
		} catch (EOFException e) {
			return tabSymbols.addID(lexeme.toString(), line, column);

		}
	}

	private Token process_NUM_INT() throws IOException{
		lexeme.append(character);
		try {
			while (true) {
				character = fLoader.getNextChar();

				if (!Character.isDigit(character)) {
					if (character == '.') {
						return process_NUM_FLOAT();
					}
					fLoader.resetLastChar();
					return  new Token(TokenType.NUM_INT, lexeme.toString(), line, column);
				} else {
					lexeme.append(character);
				}
			}
		} catch (EOFException e) {
			return new Token(TokenType.NUM_INT, lexeme.toString(), line, column);
		}
	}

	private Token process_NUM_FLOAT() throws IOException {
		lexeme.append(character);
		try {

			while (true) {
				character = fLoader.getNextChar();

				if (!Character.isDigit(character)) {
					if (character == '.') {
						lexeme.append(character);
						errors.addErro("Caracter invalido, ponto ja informado no numero float.", lexeme.toString(), line, column);
						return this.nextToken();
					}
					fLoader.resetLastChar();
					return new Token(TokenType.NUM_FLOAT, lexeme.toString(), line, column);
				} else {
					lexeme.append(character);
				}

			}
		} catch (EOFException e) {
			return new Token(TokenType.NUM_FLOAT, lexeme.toString(), line, column);
		}
	}

	private Token process_ATTRIB() throws IOException {
		lexeme.append(character);
		try {

			character = fLoader.getNextChar();

			if (character == '-') {
				lexeme.append(character);
				return  new Token(TokenType.ATTRIB, lexeme.toString(), line, column);
			} else {
				lexeme.append(character);
				errors.addErro("Caracter invalido.", lexeme.toString(), line, column);
				return this.nextToken();

			}
		} catch (Exception e) {
			lexeme.append(character);
			errors.addErro("Finizalidado durante atribuição.", lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	private Token process_RELOP() throws IOException {
		lexeme.append(character);
		try {
			while (true) {
				character = fLoader.getNextChar();

				if (((character == '<' || character == '>' || character == '=') && (lexeme.charAt(lexeme.length() - 1) == '&'))
						||
						((character == '=') && ((lexeme.charAt(lexeme.length() - 1) == '<') || (lexeme.charAt(lexeme.length() - 1) == '>')))
						||
						((character == '>') && (lexeme.charAt(lexeme.length() - 1) == '<'))
				) {
					lexeme.append(character);
				} else if ((character == '&') && (lexeme.charAt(lexeme.length() - 1) == '<' || lexeme.charAt(lexeme.length() - 1) == '>' || lexeme.charAt(lexeme.length() - 1) == '=')) {
					lexeme.append(character);
					return new Token(TokenType.RELOP, lexeme.toString(), line, column);

				} else {
					lexeme.append(character);
					errors.addErro("Caracter invalido para relacionamento.", lexeme.toString(), line, column);
					return this.nextToken();
				}
			}
		} catch (EOFException e) {
			errors.addErro("finalizado durante relacionamento de operação", lexeme.toString(), line, column);
			return token_EOF();

		}
	}

	private void ignoreComments() throws IOException{
		character = fLoader.getNextChar();
		try {

			if (character == '{') {
				while (true) {
					character = fLoader.getNextChar();
					if (character == '}') {
						character = fLoader.getNextChar();
						if (character == '#') {
							break;
						} else {
							fLoader.resetLastChar();
						}
					}

				}

			} else {
				errors.addErro("Caracter invalido, esperado {.", "comentarios", line, column);
			}
		} catch (EOFException e) {
			errors.addErro("Finalizado durante durante declaracao de comentario", "comentarios", line, column);
		}
	}

	private Token token_EOF(){
		return tabSymbols.addID("EOF", line, column);
	}

}
