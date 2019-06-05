package compiler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;



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
		Path path = Paths.get((!Application.PLATFORM.equals(PlatformEnum.WINDOWS)) ? File.FILE_LINUX : File.FILE_WINDOWS);
		if (!Files.exists(path)) { // EXCEPTION
			System.out.println("ERRO: Arquivo não foi encontrado...");
			return;
		}
		// CHAMA O ANALISADOR SINTATICO PASSADO O ARQUIVO A SER ANALISADO
		Syntactic syntactic = new Syntactic(path.toString());
		// CHAMA A FUNCAO DE PROCESSAR DO ANALISADOR SINTATICO
		syntactic.process();
	}

}

/*
 * RECUPERACAO DE ERROS 
 * 
 * FOI FEITO TRATATIVA DE RECUPERACAO DE ERRO NO DERIVAS(), ONDE SE O PROXIMO TOKEN NAO SEJA PROGRAM PROCURAMOS UM TOKEN ATE QUE SEJA UM TERM E CHAMAMOS DERIVA BLOCO PARA O PROCESSO NAO TERMINAR.
 * 
 */
