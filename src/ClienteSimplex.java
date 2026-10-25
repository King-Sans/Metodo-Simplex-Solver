import java.util.Scanner;

public class ClienteSimplex {

    static void ejecutar(Scanner sc) {
        System.out.println("===== MÉTODO SIMPLEX =====");
        System.out.println("Tipo de problema: 1 = Maximizar, 2 = Minimizar");
        int tipo = EntradaSimplex.leerEntero(sc, "Elige (1 o 2): ", 1, 2);
        boolean maximizar = (tipo == 1);

        int n = EntradaSimplex.leerEntero(sc, "Número de variables de decisión: ", 1, 20);
        int m = EntradaSimplex.leerEntero(sc, "Número de restricciones: ", 1, 20);

        double[] c = new double[n];
        System.out.println("\nFunción objetivo " + (maximizar ? "Z máx" : "Z mín") + ":");
        for (int j = 0; j < n; j++) {
            c[j] = EntradaSimplex.leerDecimal(sc, "  Coeficiente de x" + (j + 1) + ": ");
        }

        double[][] A = new double[m][n];
        double[] b = new double[m];
        System.out.println("\nRestricciones (todas de tipo <=):");
        for (int i = 0; i < m; i++) {
            System.out.println("Restricción " + (i + 1) + ":");
            for (int j = 0; j < n; j++) {
                A[i][j] = EntradaSimplex.leerDecimal(sc, "  Coeficiente de x" + (j + 1) + ": ");
            }
            b[i] = EntradaSimplex.leerDecimal(sc, "  Lado derecho (<=): ");
        }

        System.out.println("\n--- Problema capturado ---");
        System.out.println((maximizar ? "Maximizar" : "Minimizar") + " Z = " + PresentacionSimplex.expresion(c));
        System.out.println("Sujeto a:");
        for (int i = 0; i < m; i++) {
            System.out.println("  " + PresentacionSimplex.expresion(A[i]) + " <= " + PresentacionSimplex.formato(b[i]));
        }
        System.out.println("  xj >= 0");

        SimplexSolver.resolver(maximizar, c, A, b);
    }
}