package compiler;

import java.io.IOException;

public class Syntactic {
	
	private Lexicon lexicon;
	
	public Syntactic(String fileName) throws IOException {
		this.lexicon = new Lexicon(fileName);
	}
	
	public void process() throws IOException {
		Token token = lexicon.nextToken();
		token.printToken();
		while (token.getType() != TokenType.EOF) {
			token = lexicon.nextToken();
			token.printToken();
		}

		TabSymbols.getInstance().printTabSymbols();

		ErrorHandler.getInstance().errorReport();
	}

}
