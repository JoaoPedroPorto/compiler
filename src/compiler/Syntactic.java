package compiler;

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

public class Syntactic {
	
	private Lexicon lexicon;
	
	// INTANCIA O ANALISADOR LEXICO E PASSA NO CONSTRUTOR O ARQUIVO A SER VERIFICADO
	public Syntactic(String fileName) throws IOException {
		this.lexicon = new Lexicon(fileName);
	}
	
	public void process() throws IOException {
		// SOLICITA O PROXIMO TOKEN PARA O ANALISADOR LEXICO
		Token token = lexicon.nextToken();
		// PRINTA O TOKEN GERADO
		token.printToken();
		// CASO O 1 TOKEN GERADO PELO LEXICO NAO SEJA O FIM DO ARQUIVO O ANALISADOR SINTATICO 
		// FICA PEDINDO TOKENS PARA O LEXICO ATE QUE CHEGUE UM TOKEN QUE SEJA O FIM DO ARQUIVO
		while (token.getType() != TokenType.EOF) {
			// SOLICITA O PROXIMO TOKEN PARA O ANALISADOR LEXICO
			token = lexicon.nextToken();
			// PRINTA O TOKEN GERADO
			token.printToken();
		}

		// PRINTA A TABELA DE SIMBOLOS
		TabSymbols.getInstance().printTabSymbols();

		// PRINTA OS ERROS GERADOS NA VERIFICACAO, CASO HAJA
		ErrorHandler.getInstance().errorReport();
	}

}
