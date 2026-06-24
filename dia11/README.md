# Día 11

## Problema

El problema ocurre en el reactor de la fábrica. La entrada describe una red dirigida
de dispositivos:

```text
you: bbb ccc
bbb: ddd eee
```

Cada línea indica un dispositivo y la lista de dispositivos a los que van sus
salidas. Los datos fluyen solo hacia delante por esas conexiones. El objetivo de la
parte 1 es contar cuántos caminos distintos llevan desde `you` hasta `out`.

La entrada está en:

```text
src/main/resources/input.txt
```

## Parte 1

Con el ejemplo oficial:

```text
aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out
```

El resultado es:

```text
5
```

Con el input del proyecto, la respuesta de la parte 1 es:

```text
590
```

## Parte 2

Ahora hay que contar los caminos desde `svr` hasta `out`, pero solo aquellos que
pasan por `dac` y por `fft`, en cualquier orden.

Con el ejemplo oficial de la parte 2, el resultado es:

```text
2
```

Con el input del proyecto, la respuesta de la parte 2 es:

```text
319473830844560
```

## Enfoque de la solución

`DeviceNetworkParser` transforma cada línea en una entrada del grafo dirigido. El
dominio guarda un mapa desde cada dispositivo hasta sus salidas.

`ReactorPathCounterPart1` cuenta caminos con una búsqueda en profundidad desde
`you`. Para evitar recalcular subgrafos compartidos, memoiza el número de caminos
desde cada dispositivo:

```java
memoizedPaths.put(device, totalPaths);
```

Cuando la búsqueda llega a `out`, devuelve `1`, porque se ha encontrado un camino
completo. Si un dispositivo no tiene salidas y no es `out`, devuelve `0`. Se usa
`BigInteger` para no limitar artificialmente el número de caminos.

`ReactorRequiredDevicePathCounterPart2` usa la misma idea, pero añade estado a la
búsqueda: dispositivo actual, si ya se ha pasado por `dac` y si ya se ha pasado por
`fft`. Al llegar a `out`, solo cuenta el camino si ambos dispositivos requeridos ya
han sido visitados.

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

- `DeviceNetwork`: representa la red dirigida de dispositivos.

### `domain/part1`

Contiene la regla específica de la primera parte.

- `ReactorPathCounterPart1`: cuenta los caminos desde `you` hasta `out`.

### `domain/part2`

Contiene la regla específica de la segunda parte.

- `ReactorRequiredDevicePathCounterPart2`: cuenta los caminos desde `svr` hasta
  `out` que pasan por `dac` y `fft`.

### `application`

Coordina el caso de uso.

- `DeviceNetworkParser`: transforma las líneas del fichero en un `DeviceNetwork`.
- `ReactorSolver`: lee la entrada, la parsea y delega el cálculo.

### `infrastructure`

Contiene los detalles externos al dominio.

- `DeviceNetworkSource`: interfaz para obtener las líneas de entrada.
- `FileDeviceNetworkSource`: implementación que lee la red desde un fichero.

## Principios aplicados

### Abstracción

El dominio trabaja con conceptos propios del problema: red de dispositivos,
conexiones dirigidas y contador de caminos. La lógica no depende de rutas de
ficheros ni de consola.

### Diseño por contrato

`DeviceNetworkParser` rechaza líneas vacías, líneas sin `: ` y dispositivos
duplicados. `DeviceNetwork` exige que exista `you`, que haya al menos un dispositivo
y que cada dispositivo tenga salidas válidas.

### Alta cohesión y SRP

Cada clase tiene una responsabilidad concreta:

- `DeviceNetworkParser` solo parsea líneas de entrada.
- `DeviceNetwork` solo representa la red dirigida.
- `ReactorPathCounterPart1` solo aplica la regla de conteo de la parte 1.
- `ReactorRequiredDevicePathCounterPart2` solo aplica la regla de conteo de la parte 2.
- `FileDeviceNetworkSource` solo lee líneas de un fichero.
- `ReactorSolver` solo coordina el caso de uso.
- `Main` solo prepara dependencias y muestra la salida.

Esto sigue la idea de cohesión y responsabilidad única vista en teoría: cada módulo
tiene una razón principal para cambiar.

### Bajo acoplamiento

`ReactorSolver` depende de `DeviceNetworkSource`, no de `FileDeviceNetworkSource`:

```java
public ReactorSolver(DeviceNetworkSource source) {
    this.source = source;
}
```

Esto permite cambiar el origen de datos sin modificar la lógica de aplicación.

### Inversión e inyección de dependencias

La lógica de alto nivel depende de una abstracción (`DeviceNetworkSource`). La
implementación concreta se crea fuera y se inyecta por constructor:

```java
DeviceNetworkSource source = new FileDeviceNetworkSource(inputPath);
ReactorSolver solver = new ReactorSolver(source);
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

`DeviceNetworkSource` abstrae el origen de datos. `FileDeviceNetworkSource` adapta
`Files.readAllLines` a una interfaz propia del proyecto.

### Value Object

`DeviceNetwork` se modela como `record`, por lo que representa un valor del dominio
definido por sus conexiones.

### Service

`ReactorPathCounterPart1` actúa como servicio de dominio: no representa una entidad
con identidad propia, sino una operación que calcula el resultado de la parte 1.

`ReactorRequiredDevicePathCounterPart2` también actúa como servicio de dominio, pero
para la regla de caminos obligatorios de la segunda parte.

### Memoización

El conteo de caminos usa memoización para no recalcular el número de caminos desde
un mismo dispositivo cuando varios caminos previos llegan a él.

En la parte 2 la clave memoizada no es solo el dispositivo, sino también los dos
booleanos que indican si el camino ya ha pasado por `dac` y por `fft`.

## Tests

Los tests están en:

```text
src/test/java/
```

Cubren:

- el parseo de conexiones;
- el rechazo de descripciones inválidas;
- el ejemplo oficial de la parte 1, cuyo resultado esperado es `5`;
- la detección de ciclos alcanzables desde `you`.
- el ejemplo oficial de la parte 2, cuyo resultado esperado es `2`;
- que la parte 2 ignore caminos que no pasan por los dos dispositivos requeridos.

Para ejecutar los tests desde la raíz del repositorio:

```bash
mvn -pl dia11 test
```

## Ejecución

Desde la raíz del repositorio:

```bash
mvn -pl dia11 exec:java -Dexec.mainClass=Main
```

El programa imprime:

```text
Parte 1: 590
Parte 2: 319473830844560
```
