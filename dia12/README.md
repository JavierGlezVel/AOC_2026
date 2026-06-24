# Día 12

## Problema

El problema ocurre en una granja de árboles de Navidad. La entrada contiene:

- una lista de formas de regalos, representadas con `#` y `.`;
- una lista de regiones rectangulares bajo los árboles;
- para cada región, cuántos regalos de cada forma deben colocarse.

Los regalos pueden rotarse y reflejarse, pero deben colocarse sobre una cuadrícula
bidimensional. No pueden solaparse, aunque los huecos `.` de una forma no bloquean a
otras formas.

La entrada está en:

```text
src/main/resources/input.txt
```

## Parte 1

El objetivo es contar cuántas regiones pueden contener todos los regalos indicados.

Con el ejemplo oficial:

```text
4x4: 0 0 0 0 2 0
12x5: 1 0 1 0 2 2
12x5: 1 0 1 0 3 2
```

El resultado es:

```text
2
```

Con el input del proyecto, la respuesta de la parte 1 es:

```text
587
```

## Enfoque de la solución

`TreeFarmParser` separa la entrada en dos partes: primero parsea las formas de los
regalos y después las regiones. Cada forma se convierte en una lista de celdas
ocupadas.

`FittingRegionCounterPart1` aplica dos niveles de decisión:

- si el área total de los regalos supera el área de la región, la región no puede
  servir;
- para regiones pequeñas, usa un solver exacto de colocación con todas las
  rotaciones y reflexiones de cada forma;
- para las regiones grandes del input, donde todas tienen anchura y altura muy por
  encima de las formas de 3x3, la restricción efectiva es el área total disponible.

El solver exacto se usa en los tests del ejemplo oficial. Esto evita aceptar casos
pequeños que tienen área suficiente pero no admiten una colocación real.

## Diseño de clases

La solución está dividida en tres paquetes principales:

```text
application/
domain/
  common/
  part1/
infrastructure/
```

### `domain/common`

Contiene conceptos compartidos del problema.

- `Cell`: representa una celda ocupada de una forma.
- `PresentShape`: representa una forma de regalo y genera sus variantes por rotación
  y reflexión.
- `TreeRegion`: representa una región rectangular y los regalos que debe contener.
- `TreeFarmPlan`: agrupa formas y regiones.

### `domain/part1`

Contiene la regla específica de la primera parte.

- `FittingRegionCounterPart1`: cuenta cuántas regiones pueden contener sus regalos.

### `application`

Coordina el caso de uso.

- `TreeFarmParser`: transforma las líneas del fichero en un `TreeFarmPlan`.
- `TreeFarmSolver`: lee la entrada, la parsea y delega el cálculo.

### `infrastructure`

Contiene los detalles externos al dominio.

- `TreeFarmSource`: interfaz para obtener las líneas de entrada.
- `FileTreeFarmSource`: implementación que lee el plan desde un fichero.

## Principios aplicados

### Abstracción

El dominio trabaja con conceptos propios del problema: forma, celda, región y plan de
la granja. La lógica no depende de rutas de ficheros ni de consola.

### Diseño por contrato

`PresentShape`, `TreeRegion` y `TreeFarmPlan` validan sus invariantes al construirse:
índices válidos, dimensiones positivas, listas no vacías y un contador por cada
forma de regalo.

### Alta cohesión y SRP

Cada clase tiene una responsabilidad concreta:

- `TreeFarmParser` solo parsea la entrada.
- `PresentShape` solo representa una forma y sus variantes.
- `TreeRegion` solo representa una región.
- `FittingRegionCounterPart1` solo aplica la regla de la parte 1.
- `FileTreeFarmSource` solo lee líneas de un fichero.
- `TreeFarmSolver` solo coordina el caso de uso.
- `Main` solo prepara dependencias y muestra la salida.

Esto sigue la idea de cohesión y responsabilidad única vista en teoría: cada módulo
tiene una razón principal para cambiar.

### Bajo acoplamiento

`TreeFarmSolver` depende de `TreeFarmSource`, no de `FileTreeFarmSource`:

```java
public TreeFarmSolver(TreeFarmSource source) {
    this.source = source;
}
```

Esto permite cambiar el origen de datos sin modificar la lógica de aplicación.

### Inversión e inyección de dependencias

La lógica de alto nivel depende de una abstracción (`TreeFarmSource`). La
implementación concreta se crea fuera y se inyecta por constructor:

```java
TreeFarmSource source = new FileTreeFarmSource(inputPath);
TreeFarmSolver solver = new TreeFarmSolver(source);
```

Así se separa la creación del objeto concreto de su uso, reduciendo acoplamiento.

### Modularidad

La división en paquetes separa responsabilidades:

- `domain/common`: conceptos compartidos del problema.
- `domain/part1`: regla específica de la primera parte.
- `application`: coordinación del caso de uso.
- `infrastructure`: detalles técnicos de entrada.

## Patrones y técnicas usadas

### Source / Adapter

`TreeFarmSource` abstrae el origen de datos. `FileTreeFarmSource` adapta
`Files.readAllLines` a una interfaz propia del proyecto.

### Value Object

`Cell`, `PresentShape`, `TreeRegion` y `TreeFarmPlan` se modelan como `record`, por
lo que representan valores del dominio definidos por sus datos.

### Service

`FittingRegionCounterPart1` actúa como servicio de dominio: no representa una entidad
con identidad propia, sino una operación que calcula el resultado de la parte 1.

### Backtracking exacto

Para regiones pequeñas, el solver genera todas las colocaciones posibles de cada
forma y usa backtracking con máscaras de bits para comprobar si existe una colocación
sin solapamientos.

## Tests

Los tests están en:

```text
src/test/java/
```

Cubren:

- el parseo de formas y regiones;
- el ejemplo oficial de la parte 1, cuyo resultado esperado es `2`;
- el caso pequeño no trivial donde el área no basta por sí sola.

Para ejecutar los tests desde la raíz del repositorio:

```bash
mvn -pl dia12 test
```

## Ejecución

Desde la raíz del repositorio:

```bash
mvn -pl dia12 exec:java -Dexec.mainClass=Main
```

El programa imprime:

```text
Parte 1: 587
```
