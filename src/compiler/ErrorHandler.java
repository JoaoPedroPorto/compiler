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
    public void addErro(String descripition, String lexeme, long lin, long col) {
        listErros.add(ErrorReport.Create(descripition, lexeme, lin, col));
    }

    // PRINTA MENSAGEM QUE NAO HA ERROS OU LISTA OS ERROS GERADOS
    public void errorReport() {
        if (listErros == null || listErros.isEmpty() || listErros.size() == 0) {
            System.out.println("Nenhum erro foi encontrado no processamento!");
            return;
        }
        System.out.println(" ---------------------------------------------------------");
        System.out.println("|                      ERROS GERADOS                      |");
        System.out.println(" ---------------------------------------------------------");
        System.out.println("\n");
        for (ErrorReport error : listErros) {
            System.out.println(error.toString());
        }
    }
    
}
