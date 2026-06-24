# Día 5

## Problema

El problema ocurre en la cafetería. Los Elfos tienen una base de datos del sistema de
inventario con dos secciones:

- una lista de rangos de IDs de ingredientes frescos;
- una línea en blanco;
- una lista de IDs de ingredientes disponibles.

Cada rango es inclusivo. Por ejemplo, `3-5` indica que los IDs `3`, `4` y `5` son
frescos. Los rangos pueden solaparse; un ingrediente es fresco si pertenece a
cualquiera de los rangos.

La entrada está en:

```text
src/main/resources/input.txt
```

## Parte 1

El objetivo es contar cuántos IDs disponibles son frescos.

Con el ejemplo oficial:

```text
3-5
10-14
16-20
12-18

1
5
8
11
17
32
```

Los IDs frescos disponibles son `5`, `11` y `17`, así que el resultado del ejemplo es:

```text
3
```

Con el input del proyecto, la respuesta de la parte 1 es:

```text
640
```

## Parte 2

En la segunda parte, la lista de IDs disponibles deja de importar. Ahora hay que
contar cuántos IDs distintos quedan cubiertos por los rangos frescos.

Con el ejemplo oficial, los rangos:

```text
3-5
10-14
16-20
12-18
```

cubren los IDs `3`, `4`, `5`, `10`, `11`, `12`, `13`, `14`, `15`, `16`, `17`,
`18`, `19` y `20`. Por tanto, el resultado del ejemplo es:

```text
14
```

Con el input del proyecto, la respuesta de la parte 2 es:

```text
365804144481581
```

## Enfoque de la solución

`InventoryDatabaseParser` separa la entrada en dos secciones usando la línea en
blanco. Antes de esa línea parsea rangos; después de esa línea parsea IDs
disponibles.

`FreshIngredientCounterPart1` recorre los IDs disponibles y comprueba si cada uno
pertenece a algún rango fresco:

```java
for (FreshIngredientIdRange range : database.freshRanges()) {
    if (range.contains(ingredientId)) {
        return true;
    }
}
```

Como la parte 1 solo pide clasificar los IDs disponibles, no hace falta expandir los
rangos ni generar todos los IDs frescos. Esto evita trabajar con intervalos enormes.

Para la parte 2 tampoco se expanden los rangos. `FreshIngredientIdCoverageCounterPart2`
ordena los rangos por inicio, fusiona los que se solapan o se tocan, y suma la
longitud inclusiva de los intervalos resultantes:

```java
if (currentRange.overlapsOrTouches(range)) {
    currentRange = currentRange.merge(range);
} else {
    freshIngredientIds += currentRange.size();
    currentRange = range;
}
```

Así, un ID cubierto por varios rangos se cuenta una sola vez.

## Diseño de clases

La solución está dividida en tres paquetes principales:

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

- `FreshIngredientIdRange`: representa un rango inclusivo de IDs frescos.
- `InventoryDatabase`: agrupa los rangos frescos y los IDs disponibles.

### `domain/part1`

Contiene la regla específica de la primera parte.

- `FreshIngredientCounterPart1`: cuenta los IDs disponibles que son frescos.

### `domain/part2`

Contiene la regla específica de la segunda parte.

- `FreshIngredientIdCoverageCounterPart2`: cuenta cuántos IDs distintos quedan cubiertos por los rangos frescos.

### `application`

Coordina el caso de uso.

- `InventoryDatabaseParser`: transforma las líneas del fichero en un `InventoryDatabase`.
- `CafeteriaSolver`: lee la entrada, la parsea y delega el cálculo.

### `infrastructure`

Contiene los detalles externos al dominio.

- `DatabaseSource`: interfaz para obtener las líneas de entrada.
- `FileDatabaseSource`: implementación que lee la base de datos desde un fichero.

## Principios aplicados

### Abstracción

El dominio trabaja con conceptos del problema: rango de IDs frescos, base de datos
de inventario y contador de ingredientes frescos. La lógica no depende de rutas de
ficheros ni de consola.

`FreshIngredientIdRange` ofrece el método `contains`, ocultando cómo se comprueban
los límites del intervalo.

### Diseño por contrato

`FreshIngredientIdRange` valida sus invariantes al construirse:

```java
if (firstId < 0 || lastId < 0) {
    throw new IllegalArgumentException("Range limits must be >= 0");
}
if (firstId > lastId) {
    throw new IllegalArgumentException("First ID must be <= last ID");
}
```

Así, el contador puede confiar en que todo rango tiene límites no negativos y que el
inicio no es mayor que el final.

### Alta cohesión y SRP

Cada clase tiene una responsabilidad concreta:

- `InventoryDatabaseParser` solo parsea la entrada.
- `FreshIngredientIdRange` solo representa y valida un rango.
- `InventoryDatabase` solo agrupa los datos de la base de datos.
- `FreshIngredientCounterPart1` solo aplica la regla de la parte 1.
- `FreshIngredientIdCoverageCounterPart2` solo aplica la regla de la parte 2.
- `FileDatabaseSource` solo lee líneas de un fichero.
- `CafeteriaSolver` solo coordina el caso de uso.
- `Main` solo prepara dependencias y muestra la salida.

Esto evita mezclar lectura de ficheros, parseo, validación de rangos y conteo en una
única clase.

### Bajo acoplamiento

`CafeteriaSolver` depende de `DatabaseSource`, no de `FileDatabaseSource`:

```java
public CafeteriaSolver(DatabaseSource source) {
    this.source = source;
}
```

Esto permite cambiar el origen de datos sin modificar la lógica de aplicación.

### Inversión e inyección de dependencias

La lógica de alto nivel depende de una abstracción (`DatabaseSource`). La
implementación concreta se crea fuera y se inyecta por constructor:

```java
DatabaseSource source = new FileDatabaseSource(inputPath);
CafeteriaSolver solver = new CafeteriaSolver(source);
```

Así se separa la creación del objeto concreto de su uso.

### Modularidad

La división en paquetes separa responsabilidades:

- `domain/common`: conceptos compartidos del problema.
- `domain/part1`: regla específica de la primera parte.
- `domain/part2`: regla específica de la segunda parte.
- `application`: coordinación del caso de uso.
- `infrastructure`: detalles técnicos de entrada.

Esto deja claro qué código pertenece a cada parte y qué código es compartido.

### Polimorfismo

El polimorfismo aparece en `DatabaseSource`. `FileDatabaseSource` es la
implementación actual, pero `CafeteriaSolver` solo conoce la interfaz. Podría usarse
otra implementación, como una fuente en memoria, sin cambiar el solver.

## Patrones y técnicas usadas

### Source / Adapter

`DatabaseSource` abstrae el origen de datos. `FileDatabaseSource` adapta la lectura
de `Files.readAllLines` a una interfaz propia del proyecto.

### Value Object

`FreshIngredientIdRange` se modela como `record`, por lo que representa un valor del
dominio definido por sus datos (`firstId` y `lastId`). Además, valida sus invariantes
al construirse.

### Service

`FreshIngredientCounterPart1` actúa como servicio de dominio: no representa una
entidad con identidad propia, sino una operación que calcula el resultado de la parte
1.

`FreshIngredientIdCoverageCounterPart2` también actúa como servicio de dominio, pero
para contar la cobertura total de los rangos.

### Fusión de intervalos

La parte 2 usa una técnica de fusión de intervalos: ordenar por inicio, unir rangos
solapados o contiguos y sumar sus tamaños. Esto evita generar explícitamente todos
los IDs de rangos enormes.

### Fachada de caso de uso

`CafeteriaSolver` ofrece métodos simples (`solvePart1` y `solvePart2`) que ocultan los pasos
internos: leer entrada, parsear la base de datos y calcular la respuesta.

## Tests

Los tests están en:

```text
src/test/java/
```

Cubren:

- el parseo de rangos y IDs disponibles;
- el rechazo de rangos inválidos;
- la comprobación inclusiva de límites;
- la fusión de rangos solapados o contiguos;
- el ejemplo oficial de la parte 1, cuyo resultado esperado es `3`;
- el ejemplo oficial de la parte 2, cuyo resultado esperado es `14`.

Para ejecutar los tests desde la raíz del repositorio:

```bash
mvn -pl dia5 test
```

## Ejecución

Desde la raíz del repositorio:

```bash
mvn -pl dia5 exec:java -Dexec.mainClass=Main
```

El programa imprime:

```text
Parte 1: 640
Parte 2: 365804144481581
```
