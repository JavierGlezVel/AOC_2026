# Dia 7

## Problema

El problema ocurre en un laboratorio de teleportacion. La entrada representa un
diagrama de un colector de taquiones:

- `S` indica por donde entra el haz inicial.
- `.` indica espacio vacio.
- `^` indica un divisor.

Los haces siempre avanzan hacia abajo. Si un haz llega a un divisor, ese haz se
detiene y se emiten dos haces nuevos desde las columnas inmediatamente izquierda y
derecha del divisor. Si varios haces llegan a la misma posicion, se comportan como un
unico haz a partir de ahi.

En la segunda parte el colector se interpreta como un colector cuantico: no se
fusionan haces clasicos, sino lineas temporales. Dos caminos distintos que llegan a
la misma posicion siguen representando dos lineas temporales distintas.

La entrada esta en:

```text
src/main/resources/input.txt
```

## Parte 1

El objetivo es contar cuantas veces se divide un haz.

Con el ejemplo oficial:

```text
.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............
```

El resultado del ejemplo es:

```text
21
```

Con el input del proyecto, la respuesta de la parte 1 es:

```text
1541
```

## Parte 2

El objetivo es contar cuantas lineas temporales quedan activas despues de que una
particula complete todos sus recorridos posibles por el colector.

Con el mismo ejemplo oficial, el resultado es:

```text
40
```

Con el input del proyecto, la respuesta de la parte 2 es:

```text
80158285728929
```

## Enfoque de la solucion

### Parte 1

`BeamSplitCounterPart1` simula el avance del haz fila a fila. En cada fila mantiene
un conjunto de columnas activas:

```java
Set<Integer> activeColumns = Set.of(start.column());
```

Para cada columna activa:

- si la celda contiene `^`, se suma una division y se activan las columnas izquierda
  y derecha para la siguiente fila;
- si la celda contiene `.`, la misma columna sigue activa en la siguiente fila.

Se usa un `Set` porque dos divisores pueden emitir haces hacia la misma columna. En
ese caso, los haces se fusionan y solo hace falta procesar esa columna una vez en la
siguiente fila.

### Parte 2

`TimelineCounterPart2` usa la misma idea de recorrer el colector fila a fila, pero
mantiene multiplicidad de lineas temporales con un mapa:

```java
Map<Integer, BigInteger> activeTimelines = Map.of(start.column(), BigInteger.ONE);
```

La clave es la columna activa y el valor es cuantas lineas temporales llegan a esa
columna. Para cada entrada del mapa:

- si la celda contiene `^`, cada linea temporal se divide en dos y se acumula en las
  columnas izquierda y derecha;
- si la celda contiene `.`, las mismas lineas temporales siguen en la misma columna;
- si una rama sale lateralmente del diagrama, esa linea temporal se considera
  completada.

Se usa `BigInteger` porque el numero de lineas temporales crece de forma
exponencial con los divisores alcanzados y puede superar el rango de tipos enteros
pequenos.

## Diseno de clases

La solucion esta dividida en tres paquetes principales:

```text
application/
domain/
  common/
  part1/
  part2/
infrastructure/
```

### `domain/common`

Contiene conceptos compartidos del problema.

- `TachyonManifold`: representa el diagrama, valida sus invariantes y permite
  consultar el inicio y los divisores.
- `GridPosition`: representa una posicion del diagrama mediante fila y columna.

### `domain/part1`

Contiene la regla especifica de la primera parte.

- `BeamSplitCounterPart1`: cuenta las divisiones del haz.

### `domain/part2`

Contiene la regla especifica de la segunda parte.

- `TimelineCounterPart2`: cuenta las lineas temporales finales conservando la
  multiplicidad de caminos.

### `application`

Coordina el caso de uso.

- `TachyonManifoldParser`: transforma las lineas del fichero en un `TachyonManifold`.
- `LaboratorySolver`: lee la entrada, la parsea y delega el calculo.

### `infrastructure`

Contiene los detalles externos al dominio.

- `DiagramSource`: interfaz para obtener las lineas de entrada.
- `FileDiagramSource`: implementacion que lee el diagrama desde un fichero.

## Principios aplicados

### Abstraccion

El dominio trabaja con conceptos propios del problema: colector, posicion y contador
de divisiones o lineas temporales. La logica de simulacion no depende de rutas de
ficheros ni de consola.

### Diseno por contrato

`TachyonManifold` valida que el diagrama tenga al menos una fila y una columna, que
todas las filas tengan la misma anchura, que solo aparezcan los caracteres `.`, `S` y
`^`, y que exista exactamente un inicio `S`.

### Alta cohesion y SRP

Cada clase tiene una responsabilidad concreta:

- `TachyonManifoldParser` solo parsea lineas de entrada.
- `TachyonManifold` solo representa y valida el diagrama.
- `GridPosition` solo representa una coordenada.
- `BeamSplitCounterPart1` solo aplica la regla de simulacion de la parte 1.
- `TimelineCounterPart2` solo aplica la regla de simulacion de la parte 2.
- `FileDiagramSource` solo lee lineas de un fichero.
- `LaboratorySolver` solo coordina el caso de uso.
- `Main` solo prepara dependencias y muestra la salida.

Esto sigue la idea de cohesion y responsabilidad unica vista en teoria: cada modulo
tiene una razon principal para cambiar.

### Bajo acoplamiento

`LaboratorySolver` depende de `DiagramSource`, no de `FileDiagramSource`:

```java
public LaboratorySolver(DiagramSource source) {
    this.source = source;
}
```

Esto permite cambiar el origen de datos sin modificar la logica de aplicacion.

### Inversion e inyeccion de dependencias

La logica de alto nivel depende de una abstraccion (`DiagramSource`). La
implementacion concreta se crea fuera y se inyecta por constructor:

```java
DiagramSource source = new FileDiagramSource(inputPath);
LaboratorySolver solver = new LaboratorySolver(source);
```

Asi se separa la creacion del objeto concreto de su uso, reduciendo acoplamiento.

### Modularidad

La division en paquetes separa responsabilidades:

- `domain/common`: conceptos compartidos del problema.
- `domain/part1`: regla especifica de la primera parte.
- `domain/part2`: regla especifica de la segunda parte.
- `application`: coordinacion del caso de uso.
- `infrastructure`: detalles tecnicos de entrada.

## Patrones y tecnicas usadas

### Source / Adapter

`DiagramSource` abstrae el origen de datos. `FileDiagramSource` adapta
`Files.readAllLines` a una interfaz propia del proyecto.

### Value Object

`TachyonManifold` y `GridPosition` se modelan como `record`, por lo que representan
valores del dominio definidos por sus datos. `TachyonManifold` ademas valida sus
invariantes al construirse.

### Service

`BeamSplitCounterPart1` y `TimelineCounterPart2` actuan como servicios de dominio:
no representan entidades con identidad propia, sino operaciones que calculan los
resultados de cada parte.

### Fachada de caso de uso

`LaboratorySolver` ofrece `solvePart1` y `solvePart2`, ocultando los pasos internos:
leer entrada, parsear el diagrama y calcular la respuesta.

## Tests

Los tests estan en:

```text
src/test/java/
```

Cubren:

- el parseo de un diagrama valido;
- el rechazo de diagramas sin un unico inicio;
- el ejemplo oficial de la parte 1, cuyo resultado esperado es `21`;
- la fusion de haces que llegan a la misma columna.
- el ejemplo oficial de la parte 2, cuyo resultado esperado es `40`;
- la conservacion de lineas temporales distintas aunque lleguen a la misma columna;
- las lineas temporales que salen lateralmente del diagrama.

Para ejecutar los tests desde la raiz del repositorio:

```bash
mvn -pl dia7 test
```

## Ejecucion

Desde la raiz del repositorio:

```bash
mvn -pl dia7 exec:java -Dexec.mainClass=Main
```

El programa imprime:

```text
Parte 1: 1541
Parte 2: 80158285728929
```
