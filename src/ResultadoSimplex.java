import java.util.ArrayList;
import java.util.List;

public class ResultadoSimplex {

    enum Estado { OPTIMO, NO_ACOTADO, ENTRADA_INVALIDA, LIMITE_ITERACIONES }

    static class Paso {
        final String titulo;
        final String mensaje;
        final double[][] tabla;
        final int[] base;

        Paso(String titulo, String mensaje, double[][] tabla, int[] base) {
            this.titulo = titulo;
            this.mensaje = mensaje;
            this.tabla = new double[tabla.length][];
            for (int i = 0; i < tabla.length; i++)
                this.tabla[i] = tabla[i].clone();
            this.base = base.clone();
        }
    }

    Estado estado;
    String mensaje;

    boolean maximizar;
    int n;
    int m;
    String[] nombre;

    final List<Paso> pasos = new ArrayList<>();

    double[] x;
    double z;
    double[] holguras;
    double[] preciosSombra;

    boolean optimosMultiples;
    boolean degenerada;
    boolean usoBland;
    int iteraciones;
}