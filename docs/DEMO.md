# Demo / Screenshot Checklist

**Group Members: Syeda Momina Sherazi (534663) | Muqaddus Habib (534742)**

Take one screenshot for each numbered step.

1. **Project open in VS Code** — show `src/`, `docs/`, scripts and `README.md`
   in the Explorer, with the terminal visible.
2. **App launch** — run `run.bat` (or `./run.sh`). Capture the console lines
   with the member names and IDs plus the opened window; the title bar also
   shows both members.
3. **Add a valid book** — enter ID `B001`, title `Clean Code`, author
   `Robert Martin`, press **Add**. Show the table row and success message.
4. **Empty-title error** — enter ID `B002`, leave Title blank, press **Add**.
   Capture the `JOptionPane` error dialog and the message area.
5. **Duplicate-ID error** — press **Add** again with ID `B001` and any title /
   author. Capture the duplicate-ID error dialog.
6. **View all books** — add a second valid book (`B002`, `Effective Java`,
   `Joshua Bloch`) and show both rows in the table.
7. **Update** — select the `B001` row (or type `B001`), change the title to
   `Refactoring` and author to `Martin Fowler`, press **Update**. Show the
   changed row.
8. **Non-existing book handled** — type `B999` and press **Update**. Capture
   the friendly "No book found" error dialog; no stack trace appears.
9. **Delete** — type `B002` and press **Delete**. Show the remaining row.
10. **Status change** — select `B001`, choose `ISSUED`, press **Set Status**.
    Show the Status column changing. Repeat with `RESERVED` if time allows.
11. **Empty library** — delete the last book and capture the large
    **"Library is empty"** message in the centre of the window.
12. **Test output** — run `test.bat` (or `./test.sh`) and capture the terminal
    showing the members line, the 8-row table and `Total: 8/8 tests passed.`
13. **Git log** — run `git log --oneline --graph --stat` and capture the
    separate commits and file changes.
14. **Javadoc in browser** — open `docs/javadoc/index.html`, show the package
    list and one class page with both `@author` names and IDs.
