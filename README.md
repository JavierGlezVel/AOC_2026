# Advent of Code

Repositorio para resolver Advent of Code en Java, organizado como un proyecto Maven multimodulo.

## Estructura

```text
AOC/
  pom.xml
  dia1/
    pom.xml
    src/main/java/
    src/main/resources/input.txt
  dia2/
    pom.xml
    src/main/java/
    src/main/resources/input.txt
```

Cada dia es un modulo Maven independiente. El `pom.xml` de la raiz permite compilar todos los dias juntos.

## Comandos

Desde la raiz del repositorio:

```bash
mvn compile
```

Para ejecutar un dia desde IntelliJ, abre el repositorio desde la carpeta `AOC` e importa el proyecto Maven.
