package compiler;

public class Compiler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (args.length != 1) { // EXCEPTION
			System.out.println("ERRO: Arquivo não foi encontrado...");
			return;
		}
		Syntactic syntactic = new Syntactic(args[0]);
		syntactic.process();
	}

}
