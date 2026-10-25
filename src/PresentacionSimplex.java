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

    static void imprimir(double[][] t, int[] base, String[] nombre, String titulo) {
        int m = base.length;
        System.out.println("\n--- " + titulo + " ---");
        System.out.printf("%-6s", "Base");
        for (String s : nombre)
            System.out.printf("%9s", s);
        System.out.printf("%9s%n", "Sol");
        for (int i = 0; i < m; i++) {
            System.out.printf("%-6s", nombre[base[i]]);
            for (double v : t[i])
                System.out.printf("%9.2f", v);
            System.out.println();
        }
        System.out.printf("%-6s", "Z");
        for (double v : t[m])
            System.out.printf("%9.2f", v);
        System.out.println();
    }

    static String formato(double v) {
        if (Math.abs(v - Math.rint(v)) < 1e-9)
            return String.valueOf((long) Math.rint(v));
        return String.format("%.4f", v);
    }
}