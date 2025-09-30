@echo off
chcp 65001 > nul
title Манчкин: Подземелье
color 0A
cls

REM Проверка Java
java -version >nul 2>&1
if errorlevel 1 (
    color 0C
    echo [ОШИБКА] Java не найдена! Установите JRE 8 или выше.
    echo Скачать можно с: https://www.java.com/
    pause
    exit /b 1
)

REM Проверка наличия скомпилированных файлов или JAR
if exist dist\MunchkinDungeon.jar (
    echo Запуск из JAR файла...
    echo.
    java -jar dist\MunchkinDungeon.jar
) else if exist out\com\example\dungeon\Main.class (
    echo Запуск из скомпилированных классов...
    echo.
    java -cp out com.example.dungeon.Main
) else (
    color 0C
    echo [ОШИБКА] Игра не собрана!
    echo.
    echo Сначала запустите build.bat для сборки игры
    echo.
    set /p BUILD="Запустить сборку сейчас? (y/n): "
    if /i "%BUILD%"=="y" (
        if exist build.bat (
            call build.bat
            if %errorlevel% equ 0 (
                echo.
                echo Запуск игры...
                echo.
                java -cp out com.example.dungeon.Main
            )
        ) else (
            echo [ОШИБКА] Файл build.bat не найден!
        )
    )
)

echo.
echo Нажмите любую клавишу для выхода...
pause > nul
