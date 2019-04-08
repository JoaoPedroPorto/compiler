package compiler;

import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private List<ErrorReport> listErros = new ArrayList<ErrorReport>();
    private static ErrorHandler instance = new ErrorHandler();

    private ErrorHandler() { }

    public static ErrorHandler getInstance() {
        return instance;
    }

    public void addErro(String descripition, String lexeme, long lin, long col) {
        listErros.add(ErrorReport.Create(descripition, lexeme, lin, col));
    }

    public void errorReport() {
        if (listErros.size() == 0) {
            System.out.println("Nenhum erro encontrado no processamento!");
            return;
        }
        System.out.println(" ---------------------------------------------------------");
        System.out.println("|                      ERROS GERADOS                      |");
        System.out.println(" ---------------------------------------------------------");

        for (ErrorReport error : listErros) {
            System.out.println(error.toString());
        }
    }
}
