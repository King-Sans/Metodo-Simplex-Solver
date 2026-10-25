public class SimplexSolver {

    private static final double EPS = 1e-9;

    static void resolver(boolean maximizar, double[] c, double[][] A, double[] b) {
        int m = A.length; // restricciones
        int n = c.length; // variables de decisión
        int cols = n + m + 1; // variables + holguras + solución

        for (double v : b) {
            if (v < 0) {
                System.out.println("Este programa requiere lados derechos (b) >= 0.");
                return;
            }
        }

        // columnas
        String[] nombre = new String[n + m];
        for (int j = 0; j < n; j++)
            nombre[j] = "x" + (j + 1);
        for (int i = 0; i < m; i++)
            nombre[n + i] = "s" + (i + 1);

        // tabla inicial
        double[][] t = new double[m + 1][cols];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                t[i][j] = A[i][j];
            t[i][n + i] = 1; // variable de holgura
            t[i][cols - 1] = b[i];

        }

        for (int j = 0; j < n; j++)
            t[m][j] = maximizar ? -c[j] : c[j];

        int[] base = new int[m]; // variable básica de cada fila
        for (int i = 0; i < m; i++)
            base[i] = n + i;

        PresentacionSimplex.imprimir(t, base, nombre, "Tabla inicial");

        int iteracion = 0;
        while (true) {
            // Columna pivote: el valor más negativo de la fila Z
            int col = -1;
            double menor = -EPS;
            for (int j = 0; j < cols - 1; j++) {
                if (t[m][j] < menor) {
                    menor = t[m][j];
                    col = j;
                }
            }
            if (col == -1)
                break; // ya es óptima

            // Fila pivote: menor cociente positivo (solución / columna pivote)
            int fila = -1;
            double mejor = Double.MAX_VALUE;
            for (int i = 0; i < m; i++) {
                if (t[i][col] > EPS) {
                    double razon = t[i][cols - 1] / t[i][col];
                    if (razon < mejor - EPS) {
                        mejor = razon;
                        fila = i;
                    }
                }
            }
            if (fila == -1) {
                System.out.println("\nEl problema es NO ACOTADO (Z puede crecer sin límite).");
                return;
            }

            iteracion++;
            System.out.println("\nIteración " + iteracion + ": entra " + nombre[col]
                    + ", sale " + nombre[base[fila]]
                    + ", pivote = " + PresentacionSimplex.formato(t[fila][col]));

            // 3. Operaciones de fila (eliminación de Gauss-Jordan)
            double pivote = t[fila][col];
            for (int j = 0; j < cols; j++)
                t[fila][j] /= pivote;
            for (int i = 0; i <= m; i++) {
                if (i != fila) {
                    double factor = t[i][col];
                    if (Math.abs(factor) > EPS) {
                        for (int j = 0; j < cols; j++)
                            t[i][j] -= factor * t[fila][j];
                    }
                }
            }
            base[fila] = col;
            PresentacionSimplex.imprimir(t, base, nombre, "Tabla " + iteracion);
        }

        // Resultado final
        double[] x = new double[n];
        for (int i = 0; i < m; i++) {
            if (base[i] < n)
                x[base[i]] = t[i][cols - 1];
        }
        double z = maximizar ? t[m][cols - 1] : -t[m][cols - 1];

        System.out.println("\n===== SOLUCIÓN ÓPTIMA =====");
        for (int j = 0; j < n; j++) {
            System.out.println("x" + (j + 1) + " = " + PresentacionSimplex.formato(x[j]));
        }
        System.out.println("Z = " + PresentacionSimplex.formato(z));
    }
}