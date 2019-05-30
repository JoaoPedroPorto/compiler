package compiler;

public  class First {

    private First(){}

    private static First instance = new First();

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
    }
}
