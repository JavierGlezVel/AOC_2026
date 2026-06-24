# Día 10

## Problema

El problema ocurre en una fábrica. La entrada contiene una máquina por línea. Cada
línea tiene:

- un diagrama de luces entre corchetes, donde `.` significa apagada y `#` encendida;
- uno o más botones entre paréntesis, indicando qué luces conmuta cada botón;
- requisitos de joltage entre llaves, que en la parte 1 se ignoran.

Un ejemplo de línea es:

```text
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
```

Todas las luces empiezan apagadas. El objetivo es calcular el menor número total de
pulsaciones necesario para configurar todas las máquinas.

La entrada está en:

```text
src/main/resources/input.txt
```

## Parte 1

Como los botones solo conmutan luces, pulsar dos veces el mismo botón deja las luces
igual que antes y solo añade dos pulsaciones. Por eso, para minimizar, cada botón se
considera como usado o no usado.

Con el ejemplo oficial:

```text
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
```

El resultado es:

```text
7
```

Con el input del proyecto, la respuesta de la parte 1 es:

```text
545
```

## Parte 2

En la segunda parte se ignora el diagrama de luces y se usan los requisitos de
joltage. Cada botón incrementa en `1` todos los contadores que aparecen en su
esquema, y puede pulsarse cualquier número de veces.

Con el ejemplo oficial, el resultado es:

```text
33
```

Con el input del proyecto, la respuesta de la parte 2 es:

```text
22430
```

## Enfoque de la solución

`FactoryMachineParser` convierte el diagrama objetivo y cada botón en máscaras de
bits. También conserva los requisitos de joltage como una lista de enteros.

`MinimumButtonPressesCalculatorPart1` prueba todos los subconjuntos de botones de
cada máquina. Para cada subconjunto aplica XOR con las máscaras de los botones:

```java
currentMask ^= machine.buttonMasks().get(button);
```

Si la máscara resultante coincide con la máscara objetivo, se guarda el menor número
de botones usados.

`MinimumJoltageButtonPressesCalculatorPart2` modela cada máquina como un sistema de
ecuaciones lineales: cada variable es el número de veces que se pulsa un botón, y
cada ecuación representa un contador de joltage. Reduce el sistema con eliminación
gaussiana exacta usando fracciones, y enumera solo las variables libres. En este
input, cada máquina deja como máximo tres variables libres, así que la búsqueda es
exacta y acotada.

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

- `FactoryMachine`: representa una máquina mediante número de luces, máscara
  objetivo, máscaras de botones y requisitos de joltage.

### `domain/part1`

Contiene la regla específica de la primera parte.

- `MinimumButtonPressesCalculatorPart1`: calcula el menor número total de
  pulsaciones.

### `domain/part2`

Contiene la regla específica de la segunda parte.

- `MinimumJoltageButtonPressesCalculatorPart2`: calcula el menor número total de
  pulsaciones para alcanzar los requisitos de joltage.

### `application`

Coordina el caso de uso.

- `FactoryMachineParser`: transforma las líneas del fichero en máquinas del dominio.
- `FactorySolver`: lee la entrada, la parsea y delega el cálculo.

### `infrastructure`

Contiene los detalles externos al dominio.

- `FactoryMachineSource`: interfaz para obtener las líneas de entrada.
- `FileFactoryMachineSource`: implementación que lee las máquinas desde un fichero.

## Principios aplicados

### Abstracción

El dominio trabaja con conceptos propios del problema: máquina, luces objetivo y
botones. La lógica no depende de rutas de ficheros ni de consola.

### Diseño por contrato

`FactoryMachineParser` rechaza líneas que no tengan la estructura esperada y botones
que referencien luces fuera de rango. `FactoryMachine` exige al menos una luz, al
menos un botón y máscaras coherentes con el número de luces.

### Alta cohesión y SRP

Cada clase tiene una responsabilidad concreta:

- `FactoryMachineParser` solo parsea líneas del manual.
- `FactoryMachine` solo representa una máquina validada.
- `MinimumButtonPressesCalculatorPart1` solo aplica la regla de la parte 1.
- `MinimumJoltageButtonPressesCalculatorPart2` solo aplica la regla de la parte 2.
- `FileFactoryMachineSource` solo lee líneas de un fichero.
- `FactorySolver` solo coordina el caso de uso.
- `Main` solo prepara dependencias y muestra la salida.

Esto sigue la idea de cohesión y responsabilidad única vista en teoría: cada módulo
tiene una razón principal para cambiar.

### Bajo acoplamiento

`FactorySolver` depende de `FactoryMachineSource`, no de `FileFactoryMachineSource`:

```java
public FactorySolver(FactoryMachineSource source) {
    this.source = source;
}
```

Esto permite cambiar el origen de datos sin modificar la lógica de aplicación.

### Inversión e inyección de dependencias

La lógica de alto nivel depende de una abstracción (`FactoryMachineSource`). La
implementación concreta se crea fuera y se inyecta por constructor:

```java
FactoryMachineSource source = new FileFactoryMachineSource(inputPath);
FactorySolver solver = new FactorySolver(source);
```

Así se separa la creación del objeto concreto de su uso, reduciendo acoplamiento.

### Modularidad

La división en paquetes separa responsabilidades:

- `domain/common`: conceptos compartidos del problema.
- `domain/part1`: regla específica de la primera parte.
- `domain/part2`: regla específica de la segunda parte.
- `application`: coordinación del caso de uso.
- `infrastructure`: detalles técnicos de entrada.

## Patrones y técnicas usadas

### Source / Adapter

`FactoryMachineSource` abstrae el origen de datos. `FileFactoryMachineSource` adapta
`Files.readAllLines` a una interfaz propia del proyecto.

### Value Object

`FactoryMachine` se modela como `record`, por lo que representa un valor del dominio
definido por sus datos.

### Service

`MinimumButtonPressesCalculatorPart1` actúa como servicio de dominio: no representa
una entidad con identidad propia, sino una operación que calcula el resultado de la
parte 1.

`MinimumJoltageButtonPressesCalculatorPart2` también actúa como servicio de dominio,
pero para resolver los contadores de joltage de la segunda parte.

### Eliminación gaussiana

La parte 2 usa eliminación gaussiana con fracciones exactas para transformar el
sistema de botones y contadores. Después enumera las variables libres y acepta solo
soluciones enteras no negativas.

## Tests

Los tests están en:

```text
src/test/java/
```

Cubren:

- el parseo de una máquina válida;
- el rechazo de líneas inválidas;
- el ejemplo oficial de la parte 1, cuyo resultado esperado es `7`;
- que no hace falta pulsar un botón más de una vez para configurar solo luces.
- el ejemplo oficial de la parte 2, cuyo resultado esperado es `33`;
- que en la parte 2 un botón pueda pulsarse varias veces.

Para ejecutar los tests desde la raíz del repositorio:

```bash
mvn -pl dia10 test
```

## Ejecución

Desde la raíz del repositorio:

```bash
mvn -pl dia10 exec:java -Dexec.mainClass=Main
```

El programa imprime:

```text
Parte 1: 545
Parte 2: 22430
```
