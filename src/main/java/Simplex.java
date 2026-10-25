
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Simplex {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            ClienteSimplex.ejecutar(sc);
        } catch (NoSuchElementException e) {
            System.out.println("\nSe cerró la entrada de datos. Programa terminado.");
        }
    }
}
