package compiler;

import java.util.ArrayList;
import java.util.List;

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

public class ErrorHandler {
    private List<ErrorReport> listErros = new ArrayList<ErrorReport>();
    private static ErrorHandler instance = new ErrorHandler();

    private ErrorHandler() { }

    public static ErrorHandler getInstance() {
        return instance;
    }

    // ADICIONA O ERRO GERADO NA LISTA DE ERROS
    public void addErro(String descripition, String lexeme, long lin, long col, String type) {
        listErros.add(ErrorReport.Create(descripition, lexeme, lin, col, type));
    }

    // PRINTA MENSAGEM QUE NAO HA ERROS OU LISTA OS ERROS GERADOS
    public void errorReport() {
        if (listErros == null || listErros.isEmpty()) {
            System.out.println("Nenhum erro foi encontrado no processamento...");
            return;
        }
        System.out.println("\n\n-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("                                                                   ERROS GERADOS                                                                   ");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%5s %15s %20s %50s %70s", "#", "POS(x,y)", "LEXEMA", "MENSAGEM", "DERIVAÇÃO");
        System.out.println("\n\n-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n\n");
        int i = 1;
        for (ErrorReport error : listErros) {
        	System.out.format("%5s %15s %20s %100s %20s",i, "(" + error.getLine() + ", " + error.getColumn() + ")", (error.getLexeme() != null && !error.getLexeme().isEmpty()) ? error.getLexeme() : "Fim do arquivo", error.getDescription(), error.getType());
            System.out.println("\n\n#---------------------------------------------------------------------------------------------------------------------------------------------------------------------#\n");
            i++;
        }
    }
    
}
