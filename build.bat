@echo off
chcp 65001 > nul
title Munchkin Dungeon Build

echo ======================================
echo    MUNCHKIN DUNGEON BUILD
echo ======================================
echo.

REM Check Java
echo [1/5] Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found! Install JDK 8+
    echo Download from: https://java.com/
    pause
    exit /b 1
)

javac -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java compiler not found! Install JDK
    pause
    exit /b 1
)

echo [OK] Java found

REM Create directories
echo [2/5] Creating directories...
if not exist out mkdir out
if not exist dist mkdir dist
if not exist saves mkdir saves
echo [OK] Directories created

REM Clean old files
echo [3/5] Cleaning old files...
if exist out\* del /q /s out\* >nul 2>&1
if exist dist\* del /q dist\* >nul 2>&1
echo [OK] Cleaned

REM Compile
echo [4/5] Compiling source code...
javac -encoding UTF-8 -d out com\example\dungeon\*.java com\example\dungeon\core\*.java com\example\dungeon\model\*.java 2> compile_errors.log

if %errorlevel% equ 0 (
    echo [OK] Compilation successful!
    if exist compile_errors.log del compile_errors.log
) else (
    echo [ERROR] Compilation failed! Check compile_errors.log
    type compile_errors.log
    pause
    exit /b 1
)

REM Create JAR
echo [5/5] Creating JAR file...
cd out
jar cfe ..\dist\MunchkinDungeon.jar com.example.dungeon.Main com\
cd ..

if exist dist\MunchkinDungeon.jar (
    echo [OK] JAR created: dist\MunchkinDungeon.jar
) else (
    echo [ERROR] Failed to create JAR
    pause
    exit /b 1
)

REM Create run script
echo @echo off > run.bat
echo chcp 65001 ^> nul >> run.bat
echo title Munchkin Dungeon >> run.bat
echo java -cp out com.example.dungeon.Main >> run.bat
echo pause >> run.bat

echo.
echo ======================================
echo     BUILD COMPLETED SUCCESSFULLY!
echo ======================================
echo.
echo Run options:
echo   1. run.bat                      - run from classes
echo   2. java -jar dist\MunchkinDungeon.jar - run JAR
echo.
echo Press any key to exit...
pause > nul
