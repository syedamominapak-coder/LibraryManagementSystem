@echo off
REM Compile all sources into out\ and launch the Swing application.
setlocal
set ROOT=%~dp0
if not exist "%ROOT%out" mkdir "%ROOT%out"
javac -d "%ROOT%out" "%ROOT%src\app\AppInfo.java" "%ROOT%src\Main.java" "%ROOT%src\model\BookStatus.java" "%ROOT%src\model\Book.java" "%ROOT%src\service\LibraryException.java" "%ROOT%src\service\LibraryService.java" "%ROOT%src\ui\LibraryFrame.java" "%ROOT%src\test\LibraryServiceTest.java"
if errorlevel 1 exit /b 1
java -cp "%ROOT%out" Main
