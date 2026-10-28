public class PresentacionSimplex {

    static String expresion(double[] k) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < k.length; j++) {
            if (j > 0)
                sb.append(k[j] < 0 ? " - " : " + ");
            else if (k[j] < 0)
                sb.append("-");
            sb.append(formato(Math.abs(k[j]))).append("x").append(j + 1);
        }
        return sb.toString();
    }

    static void imprimir(double[][] t, int[] base, String[] nombre, String titulo, String etiquetaZ) {
        int m = base.length;
        System.out.println("\n--- " + titulo + " ---");
        System.out.printf("%-6s", "Base");
        for (String s : nombre)
            System.out.printf("%9s", s);
        System.out.printf("%9s%n", "Sol");
        for (int i = 0; i < m; i++) {
            System.out.printf("%-6s", nombre[base[i]]);
            for (double v : t[i])
                System.out.printf("%9.2f", celda(v));
            System.out.println();
        }
        System.out.printf("%-6s", etiquetaZ);
        for (double v : t[m])
            System.out.printf("%9.2f", celda(v));
        System.out.println();
    }

    private static double celda(double v) {
        return Math.abs(v) < 0.005 ? 0.0 : v;
    }

    static void imprimirResultado(ResultadoSimplex r, boolean verPasos) {
        if (r.estado == ResultadoSimplex.Estado.ENTRADA_INVALIDA) {
            System.out.println("\n" + r.mensaje);
            return;
        }

        if (verPasos) {
            if (!r.maximizar)
                System.out.println("\nNota: al minimizar, el programa maximiza W = -Z; la fila W de las tablas es -Z.");
            String etiqueta = r.maximizar ? "Z" : "W";
            for (ResultadoSimplex.Paso p : r.pasos) {
                if (p.mensaje != null)
                    System.out.println("\n" + p.mensaje);
                imprimir(p.tabla, p.base, r.nombre, p.titulo, etiqueta);
            }
        }

        switch (r.estado) {
            case NO_ACOTADO:
                System.out.println("\nEl problema es NO ACOTADO (Z puede "
                        + (r.maximizar ? "crecer" : "decrecer") + " sin límite).");
                return;
            case LIMITE_ITERACIONES:
                System.out.println("\n" + r.mensaje);
                return;
            default:
                break;
        }

        System.out.println("\n===== SOLUCIÓN ÓPTIMA =====");
        for (int j = 0; j < r.n; j++)
            System.out.println("x" + (j + 1) + " = " + formato(r.x[j]));
        System.out.println("Z = " + formato(r.z));
        System.out.println("Iteraciones: " + r.iteraciones);

        System.out.println("\n--- Holguras (lo que sobra de cada restricción) ---");
        for (int i = 0; i < r.m; i++)
            System.out.println("s" + (i + 1) + " = " + formato(r.holguras[i])
                    + (r.holguras[i] == 0 ? "  (restricción activa: se usa todo)" : ""));

        System.out.println("\n--- Precios sombra (cambio en Z por 1 unidad más del lado derecho) ---");
        for (int i = 0; i < r.m; i++)
            System.out.println("Restricción " + (i + 1) + ": " + formato(r.preciosSombra[i]));
        System.out.println("(Solo es válido para cambios pequeños, mientras la base no cambie.)");

        if (r.optimosMultiples)
            System.out.println("\nAviso: hay variables no básicas con 0 en la fila Z, "
                    + "así que podrían existir otras soluciones óptimas con el mismo Z.");
        if (r.degenerada)
            System.out.println("Aviso: el problema presentó degeneración "
                    + "(empate en la razón mínima o una variable básica vale 0).");
        if (r.usoBland)
            System.out.println("Aviso: se activó la regla de Bland para evitar ciclado.");
    }

    static String formato(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-9)
            return String.valueOf((long) Math.rint(v));
        return String.format("%.4f", v);
    }
}