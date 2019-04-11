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

public enum TokenType {
	NUM_INT,
	NUM_FLOAT,
	LITERAL,
	ID,
	RELOP,
	ADDSUB,
	MULTDIV,
	ATTRIB,
	TERM,
	L_PAR,
	R_PAR,
	LOGIC_VAL,
	LOGIC_OP,
	TYPE,
	PROGRAM,
	END_PROG,
	BEGIN,
	END,
	IF,
	THEN,
	ELSE,
	FOR,
	WHILE,
	DECLARE,
	TO,
	EOF
}
