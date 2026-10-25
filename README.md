# Método Simplex

Aplicación de consola en Java para resolver problemas de programación lineal con el método Simplex.

## Estructura

```text
.
├── README.md
└── src/
    ├── ClienteSimplex.java
    ├── EntradaSimplex.java
    ├── PresentacionSimplex.java
    ├── Simplex.java
    └── SimplexSolver.java
```

`Simplex.java` contiene el método `main` y es el punto de entrada de la aplicación.

- `ClienteSimplex`: coordina la entrada del problema y la resolución.
- `EntradaSimplex`: lee y valida los datos ingresados.
- `PresentacionSimplex`: muestra expresiones, tablas y valores formateados.
- `SimplexSolver`: construye y resuelve las tablas del método Simplex.
- `Simplex`: inicia la aplicación.

## Requisitos

- JDK instalado y disponible en la terminal (`javac` y `java`).

## Compilar y ejecutar

Desde la carpeta raíz del proyecto:

```sh
mkdir -p out
javac -d out src/*.java
java -cp out Simplex
```

El programa solicita el tipo de optimización, las variables de decisión, las restricciones y sus coeficientes. Admite restricciones de tipo `<=` y requiere lados derechos no negativos.
