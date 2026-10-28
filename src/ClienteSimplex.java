import java.util.Scanner;

public class ClienteSimplex {

    static void ejecutar(Scanner sc) {
        System.out.println("===== MÉTODO SIMPLEX =====");
        do {
            resolverUnProblema(sc);
        } while (EntradaSimplex.leerSiNo(sc, "\n¿Resolver otro problema? (s/n): "));
        System.out.println("\nPrograma terminado.");
    }

    private static void resolverUnProblema(Scanner sc) {
        boolean maximizar;
        double[] c;
        double[][] A;
        double[] b;

        while (true) {
            System.out.println("\nTipo de problema: 1 = Maximizar, 2 = Minimizar");
            int tipo = EntradaSimplex.leerEntero(sc, "Elige (1 o 2): ", 1, 2);
            maximizar = (tipo == 1);

            int n = EntradaSimplex.leerEntero(sc, "Número de variables de decisión: ", 1, 20);
            int m = EntradaSimplex.leerEntero(sc, "Número de restricciones: ", 1, 20);

            c = new double[n];
            System.out.println("\nFunción objetivo " + (maximizar ? "Z máx" : "Z mín") + ":");
            for (int j = 0; j < n; j++)
                c[j] = EntradaSimplex.leerDecimal(sc, "  Coeficiente de x" + (j + 1) + ": ");

            A = new double[m][n];
            b = new double[m];
            System.out.println("\nRestricciones (todas de tipo <=):");
            for (int i = 0; i < m; i++) {
                System.out.println("Restricción " + (i + 1) + ":");
                for (int j = 0; j < n; j++)
                    A[i][j] = EntradaSimplex.leerDecimal(sc, "  Coeficiente de x" + (j + 1) + ": ");
                b[i] = EntradaSimplex.leerDecimal(sc, "  Lado derecho (<=): ");
            }

            System.out.println("\n--- Problema capturado ---");
            System.out.println((maximizar ? "Maximizar" : "Minimizar") + " Z = " + PresentacionSimplex.expresion(c));
            System.out.println("Sujeto a:");
            for (int i = 0; i < m; i++)
                System.out.println("  " + PresentacionSimplex.expresion(A[i]) + " <= " + PresentacionSimplex.formato(b[i]));
            System.out.println("  xj >= 0");

            if (EntradaSimplex.leerSiNo(sc, "\n¿Los datos son correctos? (s/n): "))
                break;
            System.out.println("\nVolvamos a capturar el problema.");
        }

        if (!maximizar && todosNoNegativos(c))
            System.out.println("\nNota: minimizando con restricciones <= y costos >= 0, "
                    + "la solución es trivial (todas las x valen 0).");

        boolean verPasos = EntradaSimplex.leerSiNo(sc, "¿Mostrar las tablas paso a paso? (s/n): ");
        ResultadoSimplex resultado = SimplexSolver.resolver(maximizar, c, A, b);
        PresentacionSimplex.imprimirResultado(resultado, verPasos);
    }

    private static boolean todosNoNegativos(double[] c) {
        for (double v : c)
            if (v < 0)
                return false;
        return true;
    }
}