# Java-Based Library Book Management System Using Modern Software Construction Tools

**Group members:** Syeda Momina Sherazi (534663) | Muqaddus Habib (534742)

An Open-Ended Lab (OEL) project: a small Java Swing application that manages
books in memory, built with the JDK command-line tools (`javac`, `java`,
`javadoc`) and versioned with Git.

## Members

- Syeda Momina Sherazi — 534663
- Muqaddus Habib — 534742

## Requirements

- JDK 17 or newer (developed and verified with JDK 27).
- No external libraries; only the Java standard library is used.
- Git for version control.

## Project structure

```
src/
  Main.java                 # GUI entry point (default package)
  app/AppInfo.java          # AppInfo.MEMBERS constant (used by all packages)
  model/Book.java           # book data
  model/BookStatus.java     # AVAILABLE, ISSUED, RESERVED
  service/LibraryService.java   # in-memory business logic
  service/LibraryException.java # checked validation / not-found error
  ui/LibraryFrame.java      # Swing GUI
  test/LibraryServiceTest.java  # plain-Java test runner (8 cases)
docs/                       # generated Javadoc and evidence files
```

> `AppInfo` lives in the `app` package so that classes in `model`, `service`,
> `ui`, and `test` can all reuse `AppInfo.MEMBERS`. `Main` stays in the default
> package so the application can be launched with `java -cp out Main`.

## Compile

```bash
javac -d out src/app/AppInfo.java src/Main.java src/model/Book.java src/model/BookStatus.java src/service/LibraryException.java src/service/LibraryService.java src/ui/LibraryFrame.java src/test/LibraryServiceTest.java
```

## Run

```bash
java -cp out Main
```

## Test

```bash
java -cp out test.LibraryServiceTest
```

The test runner prints the members line and a table of eight test cases with
expected result, actual result, and PASS/FAIL. The genuine output is saved in
`docs/test-results.txt`.

## Generate Javadoc

```bash
javadoc -d docs/javadoc -author -sourcepath src -subpackages app:model:service:ui:test src/Main.java
```

Open `docs/javadoc/index.html` in a browser.

## Git workflow

Each build step is committed separately to the local `main` branch. See
`docs/git-log.txt` for the history and `docs/DEMO.md` for the screenshot plan.
