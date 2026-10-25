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

    static double leerDecimal(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = sc.nextLine().trim().replace(',', '.');
            try {
                double v = Double.parseDouble(linea);
                if (Double.isNaN(v) || Double.isInfinite(v))
                    throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.out.println("  Error: solo se permiten números.");
            }
        }
    }
}