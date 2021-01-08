# kodvent
A collection of solutions for advent-of-code over the years

## Java
This is where I started off during the year of 2018.

The source code is split up into two modules: `solutions` and `common`.

### `solutions`
Contains the solutions written in java.

To run a specific day, mark the class as a component.

(All tests use springboot to load dependencies and are expected to fail)
#### 2018
Initial year: All days covered.
#### 2019
All days covered.

#### 2015
Started working on days retroactively
#### 2016
Started working on days retroactively

### `common`
The shared common library used to avoid writing duplicated code for a bunch of generic solutions.

#### use as library
The `solutions` module depends on this so all source code can be used.


#### publish as maven distribution
The library can be used from the scala settings as well. Just use the local maven repository
```
./gradlew :common:publishToMavenLocal
```
will produce a maven dist `kodvent:common`

## Scala

### 2017
An attempt to learn the scala syntax before starting working at Recorded Future.
Not many days have been solved.

### `scala-common`
A utility library containing AoC helper functions for parsing input-data and benchmarking.

### aoc2017
A package setup in beginning of 2021 to learn more scala practices by solving the 2017 puzzles retroactively.

### aoc2020
This year I will try to stick to solving the whole year using Scala and running each day using sbt.
