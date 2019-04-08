package compiler;

import java.io.IOException;

public class Compiler {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if (args.length != 1) { // EXCEPTION
			System.out.println("ERRO: Arquivo não foi encontrado...");
			return;
		}
		Syntactic syntactic = new Syntactic(args[0]);
		syntactic.process();
	}

}
