package compiler;

import java.io.EOFException;
import java.io.IOException;

/*
 * 
 * Arabianos
 * 
 * Rafaela Penteado da Cunha
 * João Pedro Porto
 * Diego Fortunato
 * Ulisses Maia
 * João Matos
 * Daniel Dos Anjos Barros
 *
 */

public class Lexicon {

	@SuppressWarnings("unused")
	private String fileName;
	private FileLoader fLoader;
	private StringBuilder lexeme;
	private char character;
	private long line;
	private long column;
	private Token token;
	private TabSymbols tabSymbols = TabSymbols.getInstance();
	private ErrorHandler errors = ErrorHandler.getInstance();
	private Token buffer;

	public Lexicon(String fileName) throws IOException {
		this.fileName = fileName;
		fLoader = new FileLoader(fileName);
	}

	public Token nextToken() throws IOException {
		
		if (buffer != null) {
			Token token = buffer;
			buffer = null;
			return token;
		}
		
		lexeme = new StringBuilder();
		line = fLoader.getLine();
		column = fLoader.getColumn();

		// CASO SEJA O FIM DO ARQUIVO, RETORNA TOKEN EOF
		try {
			character = fLoader.getNextChar();
		} catch (Exception ex) {
			return  token_EOF();
		}

		// VERIFICA SE O CARACTER GERADO CONTER ESPACO
		// CASO SEJA ELE PEDE NOVO TOKEN ATE QUE ESSA CONDICAO NAO SEJA MAIS VERDADE
		// TAMBEM RETORNA TOKEN EOF CASO O PROXIMO TOKEN SEJA O FIM DO ARQUIVO
		while (Character.isWhitespace(character)) {
			try {
				character = fLoader.getNextChar();
			} catch (Exception e) {
				return  token_EOF();
			}
		}

		// VERIFICA O CARACTER OBTIDO E RETONA O TOKEN GERADO AO FINAL, CASO O MESMO SEJA UM CARACTER VALIDO
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
				token = processAssign();
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
			case '$':
				token = processRelop();
				break;
			case '\"':
				token = processLiteral();
				break;
			case '\'':
				token = processLiteral();
				break;
			case '{':
				ignoreComments();
				token = this.nextToken();
				break;
			default:
				if (Character.isDigit(character)) {
					token = processNumIntAndNumFloat();
					break;
				}
				if (Character.isLetter(character) || character == '_') {
					token = processId();
					break;
				}
				lexeme.append(character);
				errors.addErro("Caracter inválido: ", lexeme.toString(), line, column);
				this.nextToken();
		}
		return token;
	}
	private Token processLiteral() throws IOException {
		try {
			lexeme.append(character);
			boolean hasLiteral = false;
			if (lexeme.charAt(lexeme.length() - 1) == '\'') {
				lexeme.append(character);
				errors.addErro("Erro Literal: Caractere' \' ' nao e aceito para literal.", lexeme.toString(), line, column);
				return this.nextToken();
			}
			do {
				character = fLoader.getNextChar();
				if (character == '\"')
					hasLiteral = true;
			} while(character != '\"');
			if (hasLiteral) {
				lexeme.append(character);
				return new Token(TokenType.LITERAL, lexeme.toString(), line, column);
			}
			lexeme.append(character);
			errors.addErro("Erro Literal: Faltando fechar aspas com '\"'.", lexeme.toString(), line, column);
			return this.nextToken();
		} catch (EOFException e) {
			lexeme.append(character);
			errors.addErro("Finalizado durante validacao de literal. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}
	
	private Token processId() throws IOException {
		try {
			do {
				lexeme.append(character);
				character = fLoader.getNextChar();
			} while(Character.isLetter(character) || Character.isDigit(character) || character == '_');
			fLoader.resetLastChar();
		} catch (EOFException e) {
		}
		return tabSymbols.addID(lexeme.toString(), line, column);
	}
	
	private Token processNumIntAndNumFloat() throws IOException {
		TokenType tokenType = TokenType.NUM_INT;
		try {
			while (Character.isDigit(character)) {
				lexeme.append(character);
				character = fLoader.getNextChar();
			}
			if (character == '.') {
				tokenType = TokenType.NUM_FLOAT;
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (!Character.isDigit(character)) {
					lexeme.append(character);
					errors.addErro("Erro Literal Numerico Decimal: Espera 'digito' depois do '.'", lexeme.toString(), line, column);
					return this.nextToken();
				}
				while (Character.isDigit(character)) {
					lexeme.append(character);
					character = fLoader.getNextChar();
				}
			}
			if (character == 'E' || character == 'e') {
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (character != '-' && character != '+') {
					lexeme.append(character);
					errors.addErro("Erro Literal Numerico: Espera ['-', '+'] depois do ['E', 'e']", lexeme.toString(), line, column);
					return this.nextToken();
				}
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (!Character.isDigit(character)) {
					lexeme.append(character);
					errors.addErro("Erro Literal Numerico: Espera 'digito' depois do ['-', '+']", lexeme.toString(), line, column);
					return this.nextToken();
				}
				while (Character.isDigit(character)) {
					lexeme.append(character);
					character = fLoader.getNextChar();
				}
			}
			if (Character.isLetter(character)) {
				lexeme.append(character);
				errors.addErro("Erro Literal Numerico: Caractere '" + character + "' nao e valido", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (EOFException e) {
			try {
				Double.parseDouble(lexeme.toString());
			} catch (Exception e1) {
				errors.addErro("Erro Literal Numerico: Nao e possivel gerar um numero com a seguinte sequencia '" + lexeme.toString() + "'", lexeme.toString(), line, column);
				return this.nextToken();
			}
		}
		return new Token(tokenType, lexeme.toString(), line, column);
	}

	private Token processAssign() throws IOException {
		try {
			lexeme.append(character);
			character = fLoader.getNextChar();
			if (character != '-') {
				lexeme.append(character);
				errors.addErro("Erro Atribuicao: Espera '-'.", lexeme.toString(), line, column);
				return this.nextToken();
			}
			lexeme.append(character);
			return  new Token(TokenType.ASSIGN, lexeme.toString(), line, column);
		} catch (Exception e) {
			errors.addErro("Finalizado durante atribuicao. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	private Token processRelop() throws IOException {
		try {
			lexeme.append(character);
			character = fLoader.getNextChar();
			if ((character == 'l' || character == 'g' || character == 'e' || character == 'd')) {
				lexeme.append(character);
				character = fLoader.getNextChar();
				if (character == 't' || character == 'e' || character == 'q' || character == 'f') {
					if ((character == 't' || character == 'e')
							&& (lexeme.charAt(lexeme.length() - 1) != 'l' && lexeme.charAt(lexeme.length() - 1) != 'g')) {
						lexeme.append(character);
						errors.addErro("Erro Operador Logico: Espera [ 'l', 'g' ].", lexeme.toString(), line, column);
						return this.nextToken();
					} else if (character == 'q' && lexeme.charAt(lexeme.length() - 1) != 'e') {
						lexeme.append(character);
						errors.addErro("Erro Operador Logico: Espera [ 'e' ].", lexeme.toString(), line, column);
						return this.nextToken();
					} else if (character == 'f' && lexeme.charAt(lexeme.length() - 1) != 'd') {
						lexeme.append(character);
						errors.addErro("Erro Operador Logico: Espera [ 'd' ].", lexeme.toString(), line, column);
						return this.nextToken();
					}
					lexeme.append(character);
					return new Token(TokenType.RELOP, lexeme.toString(), line, column);
				} else {
					lexeme.append(character);
					errors.addErro("Erro Operador Logico: Espera [ 't', 'e', 'q', 'f' ]", lexeme.toString(), line, column);
					return this.nextToken();
				}
			} else {
				lexeme.append(character);
				errors.addErro("Erro Operador Logico: Espera [ 'l', 'g', 'e', 'd' ].", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (EOFException e) {
			errors.addErro("Finalizado durante validacao de operação logica. Problema: " + e.getMessage(), lexeme.toString(), line, column);
			return token_EOF();
		}
	}
	
	public void storeToken(Token token) {
		this.buffer = token; 
	}
	

	private void ignoreComments() throws IOException {
		try {
			character = fLoader.getNextChar();
			boolean hasFence = false;
			if (character != '#') {
				errors.addErro("Erro Comentario: Espera {# #}.", "comentarios", line, column);
				return;
			}
			do {
				character = fLoader.getNextChar();
				if (character == '#')
					hasFence = true;
				lexeme.append(character);
			} while(character != '#');
			if (hasFence) {
				character = fLoader.getNextChar();
				if (character == '}') {
					return;
				}
				fLoader.resetLastChar();
				errors.addErro("Erro Comentario: Espera {# #}.", "comentarios", line, column);
			}
			fLoader.resetLastChar();
			errors.addErro("Erro Comentario: Espera {# #}.", "comentarios", line, column);
		} catch (EOFException e) {
			errors.addErro("Finalizado durante validacao de comentario", "comentarios", line, column);
		}
	}

	// RETORNA UM TOKEN DE FIM DE ARQUIVO
	private Token token_EOF(){
		return new Token(TokenType.EOF, "");
	}

}
