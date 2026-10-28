public class SimplexSolver {

    private static final double EPS = 1e-9;
    private static final int MAX_ITERACIONES = 1000;

    static ResultadoSimplex resolver(boolean maximizar, double[] c, double[][] A, double[] b) {
        int m = A.length; // restricciones
        int n = c.length; // variables de decisión
        int cols = n + m + 1; // variables + holguras + solución

        ResultadoSimplex r = new ResultadoSimplex();
        r.maximizar = maximizar;
        r.n = n;
        r.m = m;

        for (double v : b) {
            if (v < 0) {
                r.estado = ResultadoSimplex.Estado.ENTRADA_INVALIDA;
                r.mensaje = "Este programa requiere lados derechos (b) >= 0.";
                return r;
            }
        }

        // columnas
        String[] nombre = new String[n + m];
        for (int j = 0; j < n; j++)
            nombre[j] = "x" + (j + 1);
        for (int i = 0; i < m; i++)
            nombre[n + i] = "s" + (i + 1);
        r.nombre = nombre;

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

        r.pasos.add(new ResultadoSimplex.Paso("Tabla inicial", null, t, base));

        int iteracion = 0;
        int sinMejora = 0;
        while (true) {
            if (iteracion >= MAX_ITERACIONES) {
                r.estado = ResultadoSimplex.Estado.LIMITE_ITERACIONES;
                r.mensaje = "Se alcanzó el límite de " + MAX_ITERACIONES + " iteraciones sin llegar al óptimo.";
                r.iteraciones = iteracion;
                return r;
            }

            boolean bland = sinMejora >= n + m;
            if (bland)
                r.usoBland = true;

            int col = -1;
            if (bland) {
                for (int j = 0; j < cols - 1; j++) {
                    if (t[m][j] < -EPS) {
                        col = j;
                        break;
                    }
                }
            } else {
                double menor = -EPS;
                for (int j = 0; j < cols - 1; j++) {
                    if (t[m][j] < menor) {
                        menor = t[m][j];
                        col = j;
                    }
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
                    if (fila == -1 || razon < mejor - EPS) {
                        mejor = razon;
                        fila = i;
                    } else if (Math.abs(razon - mejor) <= EPS) {
                        r.degenerada = true;
                        if (bland && base[i] < base[fila])
                            fila = i;
                    }
                }
            }
            if (fila == -1) {
                r.estado = ResultadoSimplex.Estado.NO_ACOTADO;
                r.iteraciones = iteracion;
                return r;
            }

            iteracion++;
            String msg = "Iteración " + iteracion + ": entra " + nombre[col]
                    + ", sale " + nombre[base[fila]]
                    + ", pivote = " + PresentacionSimplex.formato(t[fila][col])
                    + (bland ? "  (regla de Bland)" : "");

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

            for (int i = 0; i <= m; i++)
                for (int j = 0; j < cols; j++)
                    if (Math.abs(t[i][j]) < EPS)
                        t[i][j] = 0;

            base[fila] = col;

            double zActual = t[m][cols - 1];
            double zAnterior = r.pasos.get(r.pasos.size() - 1).tabla[m][cols - 1];
            if (iteracion == 1 || zActual > zAnterior + EPS)
                sinMejora = 0;
            else
                sinMejora++;

            r.pasos.add(new ResultadoSimplex.Paso("Tabla " + iteracion, msg, t, base));
        }

        r.estado = ResultadoSimplex.Estado.OPTIMO;
        r.iteraciones = iteracion;
        r.x = new double[n];
        r.holguras = new double[m];
        r.preciosSombra = new double[m];

        boolean[] esBasica = new boolean[n + m];
        for (int i = 0; i < m; i++) {
            esBasica[base[i]] = true;
            double valor = t[i][cols - 1];
            if (valor <= EPS)
                r.degenerada = true;
            if (base[i] < n)
                r.x[base[i]] = valor;
            else
                r.holguras[base[i] - n] = valor;
        }

        r.z = maximizar ? t[m][cols - 1] : -t[m][cols - 1];
        for (int i = 0; i < m; i++)
            r.preciosSombra[i] = maximizar ? t[m][n + i] : -t[m][n + i];

        for (int j = 0; j < n + m; j++) {
            if (!esBasica[j] && Math.abs(t[m][j]) < EPS) {
                r.optimosMultiples = true;
                break;
            }
        }
        return r;
    }
}