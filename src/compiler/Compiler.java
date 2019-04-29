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

public class Compiler {

	public static void main(String[] args) throws IOException {
	/*	if (args.length != 1) { // EXCEPTION
			System.out.println("ERRO: Arquivo não foi encontrado...");
			return;
		}*/
	String file = "C:\\git\\Compliler\\compiler\\file.txt";
		// CHAMA O ANALISADOR SINTATICO PASSADO O ARQUIVO A SER ANALISADO
		Syntactic syntactic = new Syntactic(file);
		// CHAMA A FUNCAO DE PROCESSAR DO ANALISADOR SINTATICO
		syntactic.process();
	}

}
