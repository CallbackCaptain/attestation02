@echo off
chcp 65001 > nul
title Munchkin Dungeon
color 0A
cls

REM Check Java
java -version >nul 2>&1
if errorlevel 1 (
    color 0C
    echo [ERROR] Java not found! Install JRE 8+
    echo Download from: https://www.java.com/
    pause
    exit /b 1
)

REM Check for compiled files or JAR
if exist dist\MunchkinDungeon.jar (
    echo Starting from JAR file...
    echo.
    java -jar dist\MunchkinDungeon.jar
) else if exist out\com\example\dungeon\Main.class (
    echo Starting from compiled classes...
    echo.
    java -cp out com.example.dungeon.Main
) else (
    color 0C
    echo [ERROR] Game not built!
    echo.
    echo First run build.bat to compile the game
    echo.
    set /p BUILD="Build now? (y/n): "
    if /i "%BUILD%"=="y" (
        if exist build.bat (
            call build.bat
            if %errorlevel% equ 0 (
                echo.
                echo Starting game...
                echo.
                java -cp out com.example.dungeon.Main
            )
        ) else (
            echo [ERROR] build.bat not found!
        )
    )
)

echo.
echo Press any key to exit...
pause > nul
