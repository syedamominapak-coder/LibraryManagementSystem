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
- Git for version control. VS Code is the IDE used for the demo.

## Project structure

```
src/
  Main.java                     # GUI entry point (default package)
  app/AppInfo.java              # AppInfo.MEMBERS constant (used by all packages)
  model/Book.java               # book data
  model/BookStatus.java         # AVAILABLE, ISSUED, RESERVED
  service/LibraryService.java   # in-memory business logic
  service/LibraryException.java # checked validation / not-found error
  ui/LibraryFrame.java          # Swing GUI, Theme palette and nested renderers
  test/LibraryServiceTest.java  # plain-Java test runner (8 cases)
docs/                           # generated Javadoc and evidence files
run.sh / run.bat                # compile and launch
test.sh / test.bat              # compile and run tests
javadoc.sh / javadoc.bat        # generate Javadoc HTML
```

`AppInfo` lives in the `app` package so that classes in `model`, `service`,
`ui`, and `test` can all reuse `AppInfo.MEMBERS`. `Main` stays in the default
package so the application can be launched with `java -cp out Main`.

## GUI features

- System look and feel (Nimbus fallback) applied before the window is built.
- Dark header with the title and the members line; small grey footer with both
  members and IDs.
- GridBagLayout form with equal-width fields, 12px padding and a 900x600 minimum
  window size; the window resizes cleanly.
- One reusable `ThemedButton` paints its own 110x34 rounded background in
  `paintComponent`, so the Windows look and feel cannot hide it. Add is blue,
  Update/Set Status teal, Delete red and Clear neutral, with darker hover and
  pressed fills and a visible light-grey disabled shape. All five buttons are
  centred with 10px gaps and keep tooltips and mnemonics (Alt+A/U/D/S/C).
- Table with 28px rows and custom opaque renderers: a dark header with white
  bold text, alternating rows and colour-coded statuses (green AVAILABLE,
  orange/red ISSUED, amber RESERVED). Text fields and the combo box also paint
  their own light backgrounds.
- Live search is part of the centred form grid, aligned with the Book ID label;
  its field spans the form-field columns. It filters by ID, title or author, and
  the empty message becomes "No matching books" when a filter hides everything.
- Update / Delete / Set Status stay disabled until a table row is selected;
  selecting a row fills the form, and Clear also clears the selection.
- Slim bottom status bar shows the last message in green (success) or red
  (error), alongside `JOptionPane` error dialogs.

## Compile

```bash
javac -d out src/app/AppInfo.java src/Main.java src/model/BookStatus.java src/model/Book.java src/service/LibraryException.java src/service/LibraryService.java src/ui/LibraryFrame.java src/test/LibraryServiceTest.java
```

## Run

```bash
java -cp out Main
```

Or use the scripts: `run.bat` on Windows, `./run.sh` in Git Bash/WSL. The window
title, footer, and console output all show both group members and IDs.

## Test

```bash
java -cp out test.LibraryServiceTest
```

Or use `test.bat` / `./test.sh`. The runner prints the members line and a table
of eight test cases with expected result, actual result, and PASS/FAIL. The
genuine output is saved in `docs/test-results.txt`.

## Generate Javadoc

```bash
javadoc -Xdoclint:all -d docs/javadoc -author -sourcepath src -subpackages app:model:service:ui:test src/Main.java
```

Or use `javadoc.bat` / `./javadoc.sh`. On this machine `javadoc` is not on
`PATH`; the scripts fall back to `%JAVA_HOME%\bin\javadoc.exe` or
`C:\Program Files\Java\jdk-27\bin\javadoc.exe`. Open
`docs/javadoc/index.html` in a browser. The build is warning-free.

## Evidence files

- `docs/test-results.txt` — genuine output of the 8-case test runner
- `docs/git-status.txt` — output of `git status`
- `docs/git-log.txt` — output of `git log --oneline --graph --stat`
- `docs/git-diff-example.txt` — a real `git diff` captured before committing
- `docs/DEMO.md` — screenshot checklist for the viva/demo
- `docs/javadoc/` — generated API documentation

## Git workflow

Each build step is committed separately to the local `main` branch. No remote
was supplied, so nothing has been pushed.
