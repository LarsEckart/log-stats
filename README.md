# Log-Stats

[![Build Status](https://travis-ci.org/LarsEckart/log-stats.svg?branch=master)](https://travis-ci.org/LarsEckart/log-stats)

### How to build & run tests

```bash
> ./gradlew build
```

## Top N average duration resources

### How to execute

```bash
> ./gradlew build 
> java -jar build/libs/log-stats.jar timing.log 5
```

## Hourly request count histogram

# How to execute

This gradle task requires the file 'timing.log' in the same folder.
If not, one has to modify build.gradle to change the file name/path.

```bash
> ./gradlew histogram
```
