package compiler;

import java.util.HashMap;
import java.util.Map;

public class TabSymbols {
	
	private static TabSymbols instance = new TabSymbols();
	private Map<String, Token> table;
	
	private TabSymbols() {
		table = new HashMap<String, Token>();
	}
	
	public static TabSymbols getInstance() {
		return instance;
	}

}
