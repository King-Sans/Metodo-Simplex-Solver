import java.util.Scanner;

public class EntradaSimplex {

    static int leerEntero(Scanner sc, String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(linea);
                if (v >= min && v <= max)
                    return v;
                System.out.println("  Error: escribe un número entero entre " + min + " y " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Error: solo se permiten números enteros.");
            }
        }
    }

    /** Acepta decimales (2.5 o 2,5) y fracciones (1/2). */
    static double leerDecimal(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim().replace(',', '.');
            try {
                double v;
                int barra = linea.indexOf('/');
                if (barra >= 0) {
                    double num = Double.parseDouble(linea.substring(0, barra).trim());
                    double den = Double.parseDouble(linea.substring(barra + 1).trim());
                    if (den == 0) {
                        System.out.println("  Error: el denominador no puede ser 0.");
                        continue;
                    }
                    v = num / den;
                } else {
                    v = Double.parseDouble(linea);
                }
                if (Double.isNaN(v) || Double.isInfinite(v))
                    throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.out.println("  Error: escribe un número (ejemplos: 5, 2.5, 1/2).");
            }
        }
    }

    static boolean leerSiNo(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim().toLowerCase();
            if (linea.equals("s") || linea.equals("si") || linea.equals("sí"))
                return true;
            if (linea.equals("n") || linea.equals("no"))
                return false;
            System.out.println("  Error: responde s o n.");
        }
    }
}