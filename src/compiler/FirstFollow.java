package compiler;

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
		 // FIRST DECL
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
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
	}
	
	public Map<NonTerm, List<TokenType>> getFirst() {
		return first;
	}
	public Map<NonTerm, List<TokenType>> getFollow() {
		return follow;
	}
	
	
	public boolean isInFirst(NonTerm nonTerm, Token tk) {
        if (first.get(nonTerm).contains(tk.getType()))
            return true;
        return false;
    }

    public boolean isInFollow(NonTerm nonTerm, Token tk) {
        if (follow.get(nonTerm).contains(tk.getType()))
            return true;
        return false;
    }
	
	
	
	
	
/*
    private FirstFollow(){}


    public static First getInstance() {
        return instance;
    }


    public boolean isFirst_S(Token token){
        if(token.getType() == TokenType.PROGRAM)
            return true;

        return false;
    }

    public boolean isFirst_Cmds(Token token){
        if(token.getType() == TokenType.DECLARE ||
                token.getType() == TokenType.IF ||
                token.getType() == TokenType.FOR ||
                token.getType() == TokenType.WHILE ||
                token.getType() == TokenType.ID)
            return true;

        return false;
    }

    public boolean isFirst_Cmd(Token token){
        if(token.getType() == TokenType.DECLARE ||
                token.getType() == TokenType.IF ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.FOR ||
                token.getType() == TokenType.WHILE)
            return true;

        return false;
    }

    public boolean isFirst_Decl(Token token){
        if(token.getType() == TokenType.DECLARE)
            return true;

        return false;
    }

    public boolean isFirst_Cond(Token token){
        if(token.getType() == TokenType.IF)
            return true;

        return false;
    }

    public boolean isFirst_Cndb(Token token){
        if(token.getType() == TokenType.ELSE)
            return true;

        return false;
    }

    public boolean isFirst_Atrib(Token token){
        if(token.getType() == TokenType.ID)
            return true;

        return false;
    }

    public boolean isFirst_Exp(Token token){
        if(token.getType() == TokenType.LOGIC_VAL ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT ||
                token.getType() == TokenType.L_PAR ||
                token.getType() == TokenType.LITERAL)
            return true;

        return false;
    }

    public boolean isFirst_Fid(Token token){
        if(token.getType() == TokenType.LOGIC_OP ||
                token.getType() == TokenType.ARIT_AS ||
                token.getType() == TokenType.ARIT_MD)
            return true;

        return false;
    }

    public boolean isFirst_Fopnum(Token token){
        if(token.getType() == TokenType.L_PAR ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT)
            return true;

        return false;
    }

    public boolean isFirst_Fexpnum1(Token token){
        if(token.getType() == TokenType.RELOP)
            return true;

        return false;
    }

    public boolean isFirst_Fnumint(Token token){
        if(token.getType() == TokenType.ARIT_AS ||
                token.getType() == TokenType.ARIT_MD)
            return true;

        return false;
    }

    public boolean isFirst_Fopnum1(Token token){
        if(token.getType() == TokenType.L_PAR ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT)
            return true;

        return false;
    }

    public boolean isFirst_Fexpnum2(Token token){
        if(token.getType() == TokenType.RELOP)
            return true;

        return false;
    }

    public boolean isFirst_Fnumfloat(Token token){
        if(token.getType() == TokenType.ARIT_AS ||
                token.getType() == TokenType.ARIT_MD)
            return true;

        return false;
    }

    public boolean isFirst_Fopnum2(Token token){
        if(token.getType() == TokenType.L_PAR ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT)
            return true;

        return false;
    }

    public boolean isFirst_Fexpnum3(Token token){
        if(token.getType() == TokenType.RELOP)
            return true;

        return false;
    }

    public boolean isFirst_Flpar(Token token){
        if(token.getType() == TokenType.L_PAR ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT)
            return true;

        return false;
    }

    public boolean isFirst_Fexpnum(Token token){
        if(token.getType() == TokenType.R_PAR)
            return true;

        return false;
    }

    public boolean isFirst_Frpar(Token token){
        if(token.getType() == TokenType.RELOP)
            return true;

        return false;
    }

    public boolean isFirst_Fid1(Token token){
        if(token.getType() == TokenType.RELOP ||
                token.getType() == TokenType.LOGIC_OP ||
                token.getType() == TokenType.ARIT_AS ||
                token.getType() == TokenType.ARIT_MD)
            return true;

        return false;
    }

    public boolean isFirst_Fvallog(Token token){
        if(token.getType() == TokenType.LOGIC_OP)
            return true;

        return false;
    }

    public boolean isFirst_Xexpnum(Token token){
        if(token.getType() == TokenType.ARIT_AS ||
                token.getType() == TokenType.ARIT_MD)
            return true;

        return false;
    }


    public boolean isFirst_Opnum(Token token){
        if(token.getType() == TokenType.ARIT_AS ||
                token.getType() == TokenType.ARIT_MD)
            return true;

        return false;
    }

    public boolean isFirst_Val(Token token){
        if(token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT)
            return true;

        return false;
    }

    public boolean isFirst_Rep(Token token){
        if(token.getType() == TokenType.FOR||
                token.getType() == TokenType.WHILE)
            return true;

        return false;
    }

    public boolean isFirst_Repf(Token token){
        if(token.getType() == TokenType.FOR)
            return true;

        return false;
    }

    public boolean isFirst_Expnum(Token token){
        if(token.getType() == TokenType.L_PAR ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT)
            return true;

        return false;
    }

    public boolean isFirst_Repw(Token token){
        if(token.getType() == TokenType.WHILE)
            return true;

        return false;
    }

    public boolean isFirst_Explo(Token token){
        if(token.getType() == TokenType.LOGIC_VAL ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.NUM_INT ||
                token.getType() == TokenType.NUM_FLOAT ||
                token.getType() == TokenType.L_PAR)
            return true;

        return false;
    }

    public boolean isFirst_Bloco(Token token){
        if(token.getType() == TokenType.BEGIN ||
                token.getType() == TokenType.DECLARE ||
                token.getType() == TokenType.IF ||
                token.getType() == TokenType.ID ||
                token.getType() == TokenType.FOR ||
                token.getType() == TokenType.WHILE)
            return true;

        return false;
    }*/
}
