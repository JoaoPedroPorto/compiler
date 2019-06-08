package compiler;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class FirstFollow {
	
	private static FirstFollow instance = new FirstFollow();
	
	private Map<NonTerm, List<TokenType>> first = new HashMap<>();
	private Map<NonTerm, List<TokenType>> follow = new HashMap<>();
	
	public static FirstFollow getInstance() {
        return instance;
    }
	
	private FirstFollow() {
		 List<TokenType> list;
		 
		 // FIRST S
		 list = new ArrayList<>();
		 list.add(TokenType.PROGRAM);
		 first.put(NonTerm.S, list);
		 
		 // FIRST CMDS
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 list.add(TokenType.IF);
		 list.add(TokenType.FOR);
		 list.add(TokenType.WHILE);
		 list.add(TokenType.ID);
		 first.put(NonTerm.CMDS, list);
		 // FOLLOW CMDS
		 list = new ArrayList<>();
		 list.add(TokenType.END);
		 follow.put(NonTerm.CMDS, list);
		 
		 // FIRST CMD
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 list.add(TokenType.IF);
		 list.add(TokenType.ID);
		 list.add(TokenType.FOR);
		 list.add(TokenType.WHILE);
		 first.put(NonTerm.CMD, list);
		// FOLLOW CMD
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 list.add(TokenType.IF);
		 list.add(TokenType.FOR);
		 list.add(TokenType.WHILE);
		 list.add(TokenType.ID);
		 list.add(TokenType.END);
		 list.add(TokenType.END_PROG);
		 list.add(TokenType.ELSE);
		 follow.put(NonTerm.CMD, list);
		 
		 // FIRST DECL
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 first.put(NonTerm.DECL, list);
		 // FOLLOW DECL
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 list.add(TokenType.IF);
		 list.add(TokenType.FOR);
		 list.add(TokenType.WHILE);
		 list.add(TokenType.ID);
		 list.add(TokenType.END);
		 list.add(TokenType.END_PROG);
		 list.add(TokenType.ELSE);
		 follow.put(NonTerm.DECL, list);
		 
		 // FIRST COND
		 list = new ArrayList<>();
		 list.add(TokenType.IF);
		 first.put(NonTerm.COND, list);
		 // FOLLOW COND
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 list.add(TokenType.IF);
		 list.add(TokenType.FOR);
		 list.add(TokenType.WHILE);
		 list.add(TokenType.ID);
		 list.add(TokenType.END);
		 list.add(TokenType.END_PROG);
		 list.add(TokenType.ELSE);
		 follow.put(NonTerm.COND, list);
		 
		 // FIRST CNDB
		 list = new ArrayList<>();
		 list.add(TokenType.ELSE);
		 first.put(NonTerm.CNDB, list);
		// FOLLOW CNDB
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 list.add(TokenType.IF);
		 list.add(TokenType.FOR);
		 list.add(TokenType.WHILE);
		 list.add(TokenType.ID);
		 list.add(TokenType.END);
		 list.add(TokenType.END_PROG);
		 list.add(TokenType.ELSE);
		 follow.put(NonTerm.CNDB, list);
		 
		 // FIRST ATRIB
 		 list = new ArrayList<>();
		 list.add(TokenType.ID);
		 first.put(NonTerm.ATRIB, list);
		 // FOLLOW ATRIB
		 list = new ArrayList<>();
		 list.add(TokenType.DECLARE);
		 list.add(TokenType.IF);
		 list.add(TokenType.FOR);
		 list.add(TokenType.WHILE);
		 list.add(TokenType.ID);
		 list.add(TokenType.END);
		 list.add(TokenType.END_PROG);
		 list.add(TokenType.ELSE);
		 follow.put(NonTerm.ATRIB, list);
		 
		 // FIRST EXP
		 list = new ArrayList<>();
		 list.add(TokenType.LOGIC_VAL);
		 list.add(TokenType.ID);
		 list.add(TokenType.NUM_INT);
		 list.add(TokenType.NUM_FLOAT);
		 list.add(TokenType.L_PAR);
		 list.add(TokenType.LITERAL);
		 first.put(NonTerm.EXP, list);
		 // FOLLOW EXP
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.EXP, list);
		 
		 // FIRST FID
		 list = new ArrayList<>();
		 list.add(TokenType.LOGIC_OP);
		 list.add(TokenType.ARIT_AS);
		 list.add(TokenType.ARIT_MD);
		 first.put(NonTerm.FID, list);
		 // FOLLOW FID
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FID, list);
		 
		 // FIRST FOPNUM
		 list = new ArrayList<>();
		 list.add(TokenType.L_PAR);
		 list.add(TokenType.ID);
		 list.add(TokenType.NUM_INT);
		 list.add(TokenType.NUM_FLOAT);
		 first.put(NonTerm.FOPNUM, list);
		 // FOLLOW FOPNUM
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FOPNUM, list);
		 
		 // FIRST FEXPNUM1
		 list = new ArrayList<>();
		 list.add(TokenType.RELOP);
		 first.put(NonTerm.FEXPNUM1, list);
		 // FOLLOW FEXPNUM1
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FEXPNUM1, list);
		 
		 // FIRST FNUMINT
		 list = new ArrayList<>();
		 list.add(TokenType.ARIT_AS);
		 list.add(TokenType.ARIT_MD);
		 first.put(NonTerm.FNUMINT, list);
		 // FOLLOW FNUMINT
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FNUMINT, list);
		 
		 // FIRST FOPNUM1
		 list = new ArrayList<>();
		 list.add(TokenType.L_PAR);
		 list.add(TokenType.ID);
		 list.add(TokenType.NUM_INT);
		 list.add(TokenType.NUM_FLOAT);
		 first.put(NonTerm.FOPNUM1, list);
		 // FOLLOW FOPNUM1
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FOPNUM1, list);
		 
		 // FIRST FEXPNUM2
		 list = new ArrayList<>();
		 list.add(TokenType.RELOP);
		 first.put(NonTerm.FEXPNUM2, list);
		 // FOLLOW FEXPNUM2
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FEXPNUM2, list);
		 
		 // FIRST FNUMFLOAT
		 list = new ArrayList<>();
		 list.add(TokenType.ARIT_AS);
		 list.add(TokenType.ARIT_MD);
		 first.put(NonTerm.FNUMFLOAT, list);
		 // FOLLOW FNUMFLOAT
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FNUMFLOAT, list);
		 
		 // FIRST FOPNUM2
		 list = new ArrayList<>();
		 list.add(TokenType.L_PAR);
		 list.add(TokenType.ID);
		 list.add(TokenType.NUM_INT);
		 list.add(TokenType.NUM_FLOAT);
		 first.put(NonTerm.FOPNUM2, list);
		 // FOLLOW FOPNUM2
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FOPNUM2, list);
		 
		 // FIRST FEXPNUM3
		 list = new ArrayList<>();
		 list.add(TokenType.RELOP);
		 first.put(NonTerm.FEXPNUM3, list);
		 // FOLLOW FEXPNUM3
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FEXPNUM3, list);
		 
		 // FIRST FLPAR
		 list = new ArrayList<>();
		 list.add(TokenType.L_PAR);
		 list.add(TokenType.ID);
		 list.add(TokenType.NUM_INT);
		 list.add(TokenType.NUM_FLOAT);
		 first.put(NonTerm.FLPAR, list);
		 // FOLLOW FLPAR
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FLPAR, list);
		 
		 // FIRST FEXPNUM
		 list = new ArrayList<>();
		 list.add(TokenType.R_PAR);
		 first.put(NonTerm.FEXPNUM, list);
		 // FOLLOW FEXPNUM
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FEXPNUM, list);
		 
		 // FIRST FRPAR
		 list = new ArrayList<>();
		 list.add(TokenType.RELOP);
		 first.put(NonTerm.FRPAR, list);
		 // FOLLOW FRPAR
		 list = new ArrayList<>();
		 list.add(TokenType.TERM);
		 follow.put(NonTerm.FRPAR, list);

		// FIRST FID1
		list = new ArrayList<>();
		list.add(TokenType.RELOP);
		list.add(TokenType.LOGIC_OP);
		list.add(TokenType.ARIT_AS);
		list.add(TokenType.ARIT_MD);
		first.put(NonTerm.FID1, list);
		// FOLLOW FID1
		list = new ArrayList<>();
		list.add(TokenType.TERM);
		list.add(TokenType.R_PAR);
		follow.put(NonTerm.FID1, list);

		// FIRST FVALLOG
		list = new ArrayList<>();
		list.add(TokenType.LOGIC_OP);
		first.put(NonTerm.FVALLOG, list);
		// FOLLOW FVALLOG
		list = new ArrayList<>();
		list.add(TokenType.TERM);
		list.add(TokenType.R_PAR);
		follow.put(NonTerm.FVALLOG, list);

		// FIRST XEXPNUM
		list = new ArrayList<>();
		list.add(TokenType.ARIT_AS);
		list.add(TokenType.ARIT_MD);
		first.put(NonTerm.XEXPNUM, list);
		// FOLLOW XEXPNUM
		list = new ArrayList<>();
		list.add(TokenType.RELOP);
		list.add(TokenType.TO);
		list.add(TokenType.BEGIN);
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.ID);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.TERM);
		list.add(TokenType.R_PAR);
		follow.put(NonTerm.XEXPNUM, list);

		// FIRST OPNUM
		list = new ArrayList<>();
		list.add(TokenType.ARIT_AS);
		list.add(TokenType.ARIT_MD);
		first.put(NonTerm.OPNUM, list);
		// FOLLOW OPNUM
		list = new ArrayList<>();
		list.add(TokenType.L_PAR);
		list.add(TokenType.ID);
		list.add(TokenType.NUM_INT);
		list.add(TokenType.NUM_FLOAT);
		follow.put(NonTerm.OPNUM, list);

		// FIRST VAL
		list = new ArrayList<>();
		list.add(TokenType.ID);
		list.add(TokenType.NUM_INT);
		list.add(TokenType.NUM_FLOAT);
		first.put(NonTerm.VAL, list);
		// FOLLOW VAL
		list = new ArrayList<>();
		list.add(TokenType.ARIT_AS);
		list.add(TokenType.ARIT_MD);
		list.add(TokenType.RELOP);
		list.add(TokenType.TO);
		list.add(TokenType.BEGIN);
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.ID);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.TERM);
		list.add(TokenType.R_PAR);
		follow.put(NonTerm.VAL, list);

		// FIRST REP
		list = new ArrayList<>();
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		first.put(NonTerm.REP, list);
		// FOLLOW REP
		list = new ArrayList<>();
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.ID);
		list.add(TokenType.END);
		list.add(TokenType.END_PROG);
		list.add(TokenType.ELSE);
		follow.put(NonTerm.REP, list);

		// FIRST REPF
		list = new ArrayList<>();
		list.add(TokenType.FOR);
		first.put(NonTerm.REPF, list);
		// FOLLOW REPF
		list = new ArrayList<>();
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.ID);
		list.add(TokenType.END);
		list.add(TokenType.END_PROG);
		list.add(TokenType.ELSE);
		follow.put(NonTerm.REPF, list);

		// FIRST EXPNUM
		list = new ArrayList<>();
		list.add(TokenType.L_PAR);
		list.add(TokenType.ID);
		list.add(TokenType.NUM_INT);
		list.add(TokenType.NUM_FLOAT);
		first.put(NonTerm.EXPNUM, list);
		// FOLLOW EXPNUM
		list = new ArrayList<>();
		list.add(TokenType.RELOP);
		list.add(TokenType.TO);
		list.add(TokenType.BEGIN);
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.ID);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.TERM);
		list.add(TokenType.R_PAR);
		follow.put(NonTerm.EXPNUM, list);

		// FIRST REPW
		list = new ArrayList<>();
		list.add(TokenType.WHILE);
		first.put(NonTerm.REPW, list);
		// FOLLOW REPW
		list = new ArrayList<>();
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.ID);
		list.add(TokenType.END);
		list.add(TokenType.END_PROG);
		list.add(TokenType.ELSE);
		follow.put(NonTerm.REPW, list);

		// FIRST EXPLO
		list = new ArrayList<>();
		list.add(TokenType.LOGIC_VAL);
		list.add(TokenType.ID);
		list.add(TokenType.NUM_INT);
		list.add(TokenType.NUM_FLOAT);
		list.add(TokenType.L_PAR);
		first.put(NonTerm.EXPLO, list);
		// FOLLOW EXPLO
		list = new ArrayList<>();
		list.add(TokenType.TERM);
		list.add(TokenType.R_PAR);
		follow.put(NonTerm.EXPLO, list);

		// FIRST BLOCO
		list = new ArrayList<>();
		list.add(TokenType.BEGIN);
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.ID);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		first.put(NonTerm.BLOCO, list);
		// FOLLOW BLOCO
		list = new ArrayList<>();
		list.add(TokenType.DECLARE);
		list.add(TokenType.IF);
		list.add(TokenType.FOR);
		list.add(TokenType.WHILE);
		list.add(TokenType.ID);
		list.add(TokenType.END);
		list.add(TokenType.END_PROG);
		list.add(TokenType.ELSE);
		follow.put(NonTerm.BLOCO, list);
	}
	
	public Map<NonTerm, List<TokenType>> getFirst() {
		return first;
	}
	public Map<NonTerm, List<TokenType>> getFollow() {
		return follow;
	}
	
	public boolean isInFirst(NonTerm nonTerm, Token token) {
        if (first.get(nonTerm).contains(token.getType()))
            return true;
        return false;
    }
    public boolean isInFollow(NonTerm nonTerm, Token token) {
        if (follow.get(nonTerm).contains(token.getType()))
            return true;
        return false;
    }

}
