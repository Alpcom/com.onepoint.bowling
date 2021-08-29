
# Bowling

## Requirement
Java 11 + or GraalVM 11 +  
Maven 3.5 +

## Packaging and installation

Use maven for packaging executable jar :

```bash
mvn clean install
```

Use maven for packaging a native executable (only tested on linux):

```bash
mvn clean install -Pnative-image
```

## Running
running the jar file

```bash
java -jar ./target/bowling-0.0.1-SNAPSHOT-jar-with-dependencies.jar -d="X X X X X X X X X XXX"
```
running the executable file

```bash
./target/bowling -d="X X X X X X X X X XXX"
```

more help is available using :

```bash
java -jar ./target/bowling-0.0.1-SNAPSHOT-jar-with-dependencies.jar -h
```
or

```bash
./target/bowling -h
```

