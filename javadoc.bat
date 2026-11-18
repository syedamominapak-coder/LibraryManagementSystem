@echo off
REM Generate warning-free Javadoc HTML into docs\javadoc.
setlocal
set ROOT=%~dp0
set JAVADOC=javadoc.exe
where javadoc.exe >nul 2>nul
if errorlevel 1 (
  if defined JAVA_HOME (
    set JAVADOC=%JAVA_HOME%\bin\javadoc.exe
  ) else if exist "C:\Program Files\Java\jdk-27\bin\javadoc.exe" (
    set JAVADOC=C:\Program Files\Java\jdk-27\bin\javadoc.exe
  ) else (
    echo javadoc.exe not found: put it on PATH or set JAVA_HOME to your JDK folder.
    exit /b 1
  )
)
"%JAVADOC%" -Xdoclint:all -d "%ROOT%docs\javadoc" -author -sourcepath "%ROOT%src" -subpackages app:model:service:ui:test "%ROOT%src\Main.java"
