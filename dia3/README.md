# Día 3

## Problema

El problema ocurre en el lobby, donde el escalator necesita alimentación de
emergencia. La entrada contiene una línea por cada banco de baterías. Cada carácter
de la línea representa el joltage de una batería, siempre con un valor entre `1` y
`9`.

Dentro de cada banco hay que encender un número concreto de baterías. El joltage
generado por el banco es el número formado por esas baterías en el mismo orden en el
que aparecen. No se pueden reordenar baterías.

Por ejemplo, en el banco `12345`, si se encienden las baterías `2` y `4`, el banco
produce `24` jolts.

La entrada está en:

```text
src/main/resources/input.txt
```

## Parte 1

En la primera parte hay que encender exactamente dos baterías por banco. El objetivo
es encontrar el mayor joltage que puede producir cada banco y sumar esos máximos.

Con el ejemplo oficial:

```text
987654321111111
811111111111119
234234234234278
818181911112111
```

Los mayores joltages son:

- `98`, a partir de `987654321111111`.
- `89`, a partir de `811111111111119`.
- `78`, a partir de `234234234234278`.
- `92`, a partir de `818181911112111`.

La suma total del ejemplo es:

```text
357
```

Con el input del proyecto, la respuesta de la parte 1 es:

```text
17034
```

## Parte 2

En la segunda parte hay que encender exactamente doce baterías por banco. El joltage
producido por cada banco pasa a ser un número de 12 dígitos, por lo que la suma ya no
cabe de forma segura en un `int` y se calcula con `long`.

Con el mismo ejemplo oficial, los mayores joltages son:

- `987654321111`, a partir de `987654321111111`.
- `811111111119`, a partir de `811111111111119`.
- `434234234278`, a partir de `234234234234278`.
- `888911112111`, a partir de `818181911112111`.

La suma total del ejemplo es:

```text
3121910778619
```

Con el input del proyecto, la respuesta de la parte 2 es:

```text
168798209663590
```

## Enfoque de la solución

La parte 1 podría resolverse probando todos los pares posibles de baterías, pero la
parte 2 generaliza el problema: ahora hay que escoger una subsecuencia de 12 dígitos
que forme el mayor número posible.

Por eso `MaximumJoltageCalculator` recibe cuántas baterías debe encender:

```java
new MaximumJoltageCalculator(2)
new MaximumJoltageCalculator(12)
```

El algoritmo elige los dígitos de izquierda a derecha. Para cada posición del
resultado, busca el mayor dígito posible dentro del tramo que todavía deja suficientes
baterías a la derecha para completar el número:

```java
int searchEnd = ratings.length() - (batteriesToTurnOn - selected);
int bestIndex = searchStart;

for (int currentIndex = searchStart; currentIndex <= searchEnd; currentIndex++) {
    if (ratings.charAt(currentIndex) > ratings.charAt(bestIndex)) {
        bestIndex = currentIndex;
    }
}
```

Después añade ese dígito al resultado y continúa buscando a partir de la posición
siguiente. Así se respeta el orden original de las baterías.

`TotalOutputJoltageCalculator` suma el resultado de aplicar un `JoltageCalculator` a
cada banco:

```java
return banks.stream()
        .mapToLong(joltageCalculator::calculate)
        .sum();
```

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

Contiene conceptos y servicios compartidos por ambas partes.

- `BatteryBank`: representa un banco de baterías y valida sus invariantes.
- `JoltageCalculator`: contrato para calcular el joltage de un banco.
- `MaximumJoltageCalculator`: calcula el mayor joltage posible escogiendo un número configurable de baterías.
- `TotalOutputJoltageCalculator`: suma los joltages de todos los bancos.

### `domain/part1`

Contiene la regla específica de la primera parte.

- `TotalOutputJoltageCalculatorPart1`: configura el cálculo para encender 2 baterías.

### `domain/part2`

Contiene la regla específica de la segunda parte.

- `TotalOutputJoltageCalculatorPart2`: configura el cálculo para encender 12 baterías.

### `application`

Coordina el caso de uso.

- `BatteryBankParser`: transforma las líneas del fichero en objetos `BatteryBank`.
- `LobbySolver`: lee la entrada, la parsea y delega el cálculo de cada parte.

### `infrastructure`

Contiene los detalles externos al dominio.

- `BatteryBankSource`: interfaz para obtener las líneas de entrada.
- `FileBatteryBankSource`: implementación que lee los bancos desde un fichero.

## Principios aplicados

### Abstracción

La abstracción consiste en trabajar con los conceptos esenciales del problema y
ocultar los detalles secundarios. En esta solución el dominio habla de bancos de
baterías, calculadores de joltage y totalizadores, no de rutas de ficheros ni de
consola.

`JoltageCalculator` expresa lo que necesita el totalizador:

```java
public interface JoltageCalculator {
    long calculate(BatteryBank bank);
}
```

El código que suma no necesita conocer si el cálculo escoge 2, 12 u otro número de
baterías.

### Diseño por contrato

`BatteryBank` valida sus invariantes al construirse:

```java
if (ratings == null || ratings.length() < 2) {
    throw new IllegalArgumentException("A battery bank needs at least two batteries");
}
if (!ratings.matches("[1-9]+")) {
    throw new IllegalArgumentException("Battery ratings must be digits from 1 to 9: " + ratings);
}
```

Así, el resto del dominio puede confiar en que todo banco tiene al menos dos
baterías y solo contiene dígitos del `1` al `9`.

`MaximumJoltageCalculator` también valida que el número de baterías requerido sea
válido y que el banco tenga suficientes baterías para la regla configurada.

### Alta cohesión y SRP

Cada clase tiene una responsabilidad concreta:

- `BatteryBankParser` solo parsea líneas de entrada.
- `BatteryBank` solo representa y valida un banco de baterías.
- `MaximumJoltageCalculator` solo calcula el máximo joltage de un banco.
- `TotalOutputJoltageCalculator` solo suma joltages de varios bancos.
- `TotalOutputJoltageCalculatorPart1` solo configura la regla de la parte 1.
- `TotalOutputJoltageCalculatorPart2` solo configura la regla de la parte 2.
- `FileBatteryBankSource` solo lee líneas de un fichero.
- `LobbySolver` solo coordina el caso de uso.
- `Main` solo prepara dependencias y muestra la salida.

Esto evita mezclar lectura de ficheros, parseo, algoritmo de selección, suma y salida
por consola en una única clase.

### Bajo acoplamiento

`LobbySolver` depende de la interfaz `BatteryBankSource`, no de
`FileBatteryBankSource`:

```java
public LobbySolver(BatteryBankSource source) {
    this.source = source;
}
```

Esto permite cambiar el origen de datos sin tocar la lógica de aplicación.

`TotalOutputJoltageCalculator` depende de `JoltageCalculator`, no de
`MaximumJoltageCalculator`. Por eso puede sumar resultados de cualquier estrategia
que respete el contrato.

### Inversión e inyección de dependencias

La inversión de dependencias aparece porque las clases de alto nivel dependen de
abstracciones:

- `LobbySolver` depende de `BatteryBankSource`.
- `TotalOutputJoltageCalculator` depende de `JoltageCalculator`.

La inyección de dependencias se usa al pasar esas dependencias por constructor. En
`Main` se crea la fuente concreta:

```java
BatteryBankSource source = new FileBatteryBankSource(inputPath);
LobbySolver solver = new LobbySolver(source);
```

La creación del objeto concreto queda separada de su uso.

### Abierto/Cerrado

El principio abierto/cerrado indica que el código debería poder extenderse sin
modificar las piezas estables.

La parte 2 se añade creando `TotalOutputJoltageCalculatorPart2` y configurando
`MaximumJoltageCalculator` con `12` baterías. El servicio común
`TotalOutputJoltageCalculator` no necesita saber qué parte está resolviendo: solo
trabaja con el contrato `JoltageCalculator`.

Si apareciera otra regla de joltage, se podría añadir otra implementación de
`JoltageCalculator` sin cambiar el totalizador.

### Sustitución de Liskov

La sustitución de Liskov aparece de forma sencilla en `JoltageCalculator`: cualquier
implementación que respete ese contrato puede sustituir a otra en
`TotalOutputJoltageCalculator` sin romper la suma.

El test `canUseAnyCalculatorThatSatisfiesTheJoltageContract` usa una implementación
alternativa del contrato para comprobar que el totalizador no depende de
`MaximumJoltageCalculator`.

### Modularidad

La modularidad divide el sistema en piezas que pueden entenderse y modificarse por
separado:

- `domain/common`: reglas y conceptos compartidos por ambas partes.
- `domain/part1`: configuración específica de la primera parte.
- `domain/part2`: configuración específica de la segunda parte.
- `application`: coordinación del caso de uso.
- `infrastructure`: detalles técnicos de entrada.

Esta separación permite cambiar una pieza concreta sin arrastrar al resto. Por
ejemplo, si la entrada dejara de venir de un fichero, el dominio no tendría que
cambiar.

### Polimorfismo

El polimorfismo permite trabajar con distintas implementaciones a través de un mismo
tipo común. En esta solución aparece en dos contratos:

- `BatteryBankSource`, implementado por `FileBatteryBankSource`.
- `JoltageCalculator`, implementado por `MaximumJoltageCalculator`.

Gracias a esto, una clase cliente puede usar el contrato sin depender de una clase
concreta.

### Streams

La API de Streams se usa en el punto donde aporta claridad: sumar el resultado de
aplicar el calculador a cada banco.

Se utiliza `mapToLong`, un stream de primitivos, porque la parte 2 produce números y
sumas grandes:

```java
banks.stream()
        .mapToLong(joltageCalculator::calculate)
        .sum();
```

## Patrones y técnicas usadas

### Source

`BatteryBankSource` actúa como abstracción del origen de datos. El dominio no
depende de si la entrada viene de un fichero, de memoria o de otro sistema.

### Value Object

`BatteryBank` se modela como `record`, por lo que representa un valor del dominio
definido por sus datos. Además, valida sus invariantes al construirse.

### Service

`MaximumJoltageCalculator`, `TotalOutputJoltageCalculator`,
`TotalOutputJoltageCalculatorPart1` y `TotalOutputJoltageCalculatorPart2` actúan como
servicios de dominio. No representan entidades con identidad propia, sino operaciones
del problema que reciben datos y devuelven un resultado.

### Estrategia

`JoltageCalculator` permite separar el algoritmo de cálculo del código que suma los
resultados. La implementación usada actualmente es `MaximumJoltageCalculator`, que
se configura con el número de baterías que hay que encender.

### Fachada de caso de uso

`LobbySolver` ofrece métodos simples (`solvePart1` y `solvePart2`) que ocultan los
pasos internos: leer entrada, parsear bancos y calcular la suma.

## Tests

Los tests están en:

```text
src/test/java/
```

Cubren:

- el parseo de un banco por línea;
- el rechazo de ratings fuera del rango `1` a `9`;
- el ejemplo oficial de la parte 1, cuyo resultado esperado es `357`;
- el ejemplo oficial de la parte 2, cuyo resultado esperado es `3121910778619`;
- el respeto del orden original de las baterías;
- el rechazo de bancos con menos baterías de las que exige la regla configurada;
- el uso de cualquier implementación que cumpla el contrato `JoltageCalculator`.
