# Demo / Screenshot Checklist

**Group Members: Syeda Momina Sherazi (534663) | Muqaddus Habib (534742)**

Take one screenshot for each numbered step.

1. **Project open in VS Code** — show `src/`, `docs/`, scripts and `README.md`
   in the Explorer, with the terminal visible.
2. **App launch** — run `run.bat` (or `./run.sh`). Capture the console lines
   with the member names and IDs plus the opened window; the title bar, dark
   header bar and small grey footer also show both members.
3. **Initial state** — with no rows, show the centred grey **"Library is empty"**
   message and the light background. All five buttons must have visible rounded
   shapes: Add blue, Delete red, Clear dark grey, and Update / Set Status
   light-grey because no row is selected. Hover **Add** to see its darker blue
   fill and tooltip; press and hold to see the darker still pressed fill.
4. **Add a valid book** — enter ID `B001`, title `Clean Code`, author
   `Robert Martin`, press **Add** (or Alt+A). Show the table row, the 28px row
   height, the dark table header with white bold text and the green success
   status bar.
5. **Empty-title error** — enter ID `B002`, leave Title blank, press **Add**.
   Capture the `JOptionPane` error dialog and the red error status bar.
6. **Duplicate-ID error** — press **Add** again with ID `B001` and any title /
   author. Capture the duplicate-ID error dialog.
7. **View all books** — add `B002` (`Effective Java`, `Joshua Bloch`) and `B003`
   (`Java Concurrency in Practice`, `Brian Goetz`). Show the three rows, the
   alternating row colours and the green / orange-red / amber status cells after
   using **Set Status** with `AVAILABLE`, `ISSUED` and `RESERVED`.
8. **Row selection** — click the `B001` row: the form fills automatically and
   Update / Delete / Set Status become enabled (Delete turns red).
9. **Update** — change the title to `Refactoring` and author to `Martin Fowler`,
   press **Update** (or Alt+U). Show the changed row.
10. **Non-existing book handled** — type `B999` and press **Update**. Capture
    the friendly "No book found" error dialog; no stack trace appears.
11. **Delete** — select `B002` and press **Delete** (or Alt+D). Show the
    remaining row and the success status bar.
12. **Status change** — select `B001`, choose `ISSUED`, press **Set Status**.
    Show the status cell turning orange/red. Change it to `RESERVED` to show
    amber, then back to `AVAILABLE` to show green.
13. **Search filter** — type `java` in the Search field, which sits in the same
    centred form grid as Book ID and spans the form-field columns. The table
    filters as you type. Type `zzz` and capture the centred **"No matching
    books"** message, then clear the search field.
14. **Clear** — press **Clear**, capture the empty form and the fact that the
    table selection is gone and the three row buttons again show their visible
    light-grey disabled shapes.
15. **Empty library** — delete the last book and capture the large
    **"Library is empty"** message in the centre of the window.
16. **Test output** — run `test.bat` (or `./test.sh`) and capture the terminal
    showing the members line, the 8-row table and `Total: 8/8 tests passed.`
17. **Git log** — run `git log --oneline --graph --stat` and capture the
    separate commits and file changes.
18. **Javadoc in browser** — open `docs/javadoc/index.html`, show the package
    list and one class page with both `@author` names and IDs.
