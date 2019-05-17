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

	public Lexicon(String fileName) throws IOException {
		this.fileName = fileName;
		fLoader = new FileLoader(fileName);
	}

	public Token nextToken() throws IOException {
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
			case '$':
				token = process_RELOP();
				break;
			case '\"':
				token = process_LITERAL();
				break;
			case '{':
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
				errors.addErro("Caracter inválido: ", lexeme.toString(), line, column);
				this.nextToken();
		}
		return token;
	}

	private Token process_LITERAL() throws IOException {
		lexeme.append(character);
		try {
			while (true) {
				character = fLoader.getNextChar();
				if (character == '\"') {
					lexeme.append(character);
					return new Token(TokenType.LITERAL, lexeme.toString(), line, column);
				}
				lexeme.append(character);
			}
		} catch (EOFException e) {
			lexeme.append(character);
			errors.addErro("Finalizado durante literal, faltando fechar aspas.", lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	private Token process_ID() throws IOException {
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

	private Token process_NUM_INT() throws IOException {
		lexeme.append(character);
		try {
			char lastCharacter;
			boolean validaE = true;
			boolean validaPlus = true;
			int contadorE= 0;
			int contadorPlus = 0;
			boolean codicao = true;
			while (codicao) {
				lastCharacter = character;
				character = fLoader.getNextChar();
				if (!Character.isDigit(character) && character!= 'E' && character != '+') {
					if (character == '.') {
						return process_NUM_FLOAT();
					}
					fLoader.resetLastChar();
					if(contadorE > 1 || contadorPlus > 1 || validaE == false || validaPlus == false){
						lexeme.append(character);
						errors.addErro("Caracter inválido, formato de numero cientifico incorreto", lexeme.toString(), line, column);
						codicao = false;
						return  this.nextToken();
					}
					return new Token(TokenType.NUM_INT, lexeme.toString(), line, column);

				} else {

					if(character == 'E' && Character.isDigit(lastCharacter)){
						contadorE++;
					}
					if(character == '+' && lastCharacter == 'E'){
						char numero = fLoader.getNextChar();
						if(Character.isDigit(numero)){
							fLoader.resetLastChar();
							contadorPlus++;

						}
						else{
							validaPlus = false;
						}
					}
					if(character == 'E' && !Character.isDigit(lastCharacter)){
						validaE = false;
					}
					if(character == '+' && lastCharacter != 'E'){
						validaPlus = false;
					}


					lexeme.append(character);

				}
			}

			return  this.nextToken();
		} catch (EOFException e) {
			return new Token(TokenType.NUM_INT, lexeme.toString(), line, column);
		}
	}

	private Token process_NUM_FLOAT() throws IOException {
		lexeme.append(character);
		try {
			char lastCharacterFloat;
			boolean validaFloatE = true;
			boolean validaFloatPlus = true;
			boolean validaFloatVirgula = true;
			int contadorFloatE= 0;
			int contadorFloatPlus = 0;
			int contatorFloatVirgula = 0;
			while (true) {
				lastCharacterFloat = character;
				character = fLoader.getNextChar();
				if (!Character.isDigit(character) && character != ',' && character != 'E' && character != '+') {
					if (character == '.') {
						lexeme.append(character);
						errors.addErro("Caracter inválido, ponto já	 informado no número float.", lexeme.toString(), line, column);
						return this.nextToken();
					}
					fLoader.resetLastChar();
					if(contadorFloatE > 1 || contadorFloatPlus > 1 || contatorFloatVirgula > 1 || validaFloatE == false || validaFloatPlus == false || validaFloatVirgula == false){
						lexeme.append(character);
						errors.addErro("Caracter inválido, formato de numero cientifico com ponto flutuante incorreto", lexeme.toString(), line, column);
						return  this.nextToken();
					}
					return new Token(TokenType.NUM_FLOAT, lexeme.toString(), line, column);
				} else {
					if(character == 'E' && Character.isDigit(lastCharacterFloat)){
						contadorFloatE++;
					}
					if(character == '+' && lastCharacterFloat == 'E'){
						char proximonumero = fLoader.getNextChar();
						if(Character.isDigit(proximonumero)){
							fLoader.resetLastChar();
							contadorFloatPlus++;
						}
						else{
							validaFloatPlus = false;
						}
					}
					if(character == ',' &&  Character.isDigit(lastCharacterFloat)){
						char proximonumero = fLoader.getNextChar();
						if(Character.isDigit(proximonumero)){
							fLoader.resetLastChar();
							contadorFloatPlus++;

						}
						else{
							validaFloatVirgula = false;
						}
					}

					if(character == 'E' && !Character.isDigit(lastCharacterFloat)){
						validaFloatE = false;
					}
					if(character == '+' && lastCharacterFloat != 'E'){
						validaFloatPlus = false;
					}

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
				return  new Token(TokenType.ASSIGN, lexeme.toString(), line, column);
			} else {
				lexeme.append(character);
				errors.addErro("Caracter inválido.", lexeme.toString(), line, column);
				return this.nextToken();
			}
		} catch (Exception e) {
			lexeme.append(character);
			errors.addErro("Finalizado durante atribuição.", lexeme.toString(), line, column);
			return token_EOF();
		}
	}
	
	private Token process_RELOP() throws IOException {
		lexeme.append(character);
		try {
			 while (true) {
				character = fLoader.getNextChar();
				if ((character == 'l' || character == 'g' || character == 'e' || character == 'd') && (lexeme.charAt(lexeme.length() - 1) == '$')) {
					lexeme.append(character);
				} else if ((character == 't' || character == 'e' || character == 'q' || character == 'f') 
						&& (lexeme.charAt(lexeme.length() - 1) == 'l' || lexeme.charAt(lexeme.length() - 1) == 'g' || 
							lexeme.charAt(lexeme.length() - 1) == 'e' || lexeme.charAt(lexeme.length() - 1) == 'd')) {
					if ((character == 't' || character == 'e')
							&& (lexeme.charAt(lexeme.length() - 1) != 'l' && lexeme.charAt(lexeme.length() - 1) != 'g')) {
						lexeme.append(character);
						errors.addErro("Caracter inválido para operador logico.", lexeme.toString(), line, column);
						return this.nextToken();
					} else if (character == 'q' && lexeme.charAt(lexeme.length() - 1) != 'e') {
						lexeme.append(character);
						errors.addErro("Caracter inválido para operador logico.", lexeme.toString(), line, column);
						return this.nextToken();
					} else if (character == 'f' && lexeme.charAt(lexeme.length() - 1) != 'd') {
						lexeme.append(character);
						errors.addErro("Caracter inválido para operador logico.", lexeme.toString(), line, column);
						return this.nextToken();
					}
					lexeme.append(character);
					return new Token(TokenType.RELOP, lexeme.toString(), line, column);
				} else {
					lexeme.append(character);
					errors.addErro("Caracter inválido para operador logico.", lexeme.toString(), line, column);
					return this.nextToken();
				}
			}
		} catch (EOFException e) {
			errors.addErro("finalizado durante relacionamento de operação logica", lexeme.toString(), line, column);
			return token_EOF();
		}
	}

	private void ignoreComments() throws IOException {
		character = fLoader.getNextChar();
		try {
			if (character == '#') {
				while (true) {
					character = fLoader.getNextChar();
					if (character == '#') {
						character = fLoader.getNextChar();
							if (character == '}') {
								break;
						} else {
							fLoader.resetLastChar();
						}
					}
				}
			} else {
				errors.addErro("formatado inválido, esperado {# #}.", "comentários", line, column);
			}
		} catch (EOFException e) {
			errors.addErro("Finalizado durante declaração de comentário", "comentários", line, column);
		}
	}

	// RETORNA UM TOKEN DE FIM DE ARQUIVO
	private Token token_EOF(){
		return tabSymbols.addID("EOF", line, column);
	}

}
