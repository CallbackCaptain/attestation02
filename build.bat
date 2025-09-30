@echo off
chcp 65001 > nul
title Манчкин: Подземелье - Сборка

echo ======================================
echo    МАНЧКИН: ПОДЗЕМЕЛЬЕ - СБОРКА
echo ======================================
echo.

REM Директории
set SRC_DIR=src
set OUT_DIR=out
set DIST_DIR=dist
set PACKAGE=com\example\dungeon

REM Проверка Java
echo [1/5] Проверка установки Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo [ОШИБКА] Java не найдена! Установите JDK 8 или выше.
    echo Скачать можно с: https://adoptium.net/
    pause
    exit /b 1
)

javac -version >nul 2>&1
if errorlevel 1 (
    echo [ОШИБКА] Java компилятор не найден! Установите JDK.
    pause
    exit /b 1
)

for /f tokens^=2-5^ delims^=.-_^" %%j in ('java -fullversion 2^>^&1') do set "JAVA_VERSION=%%j.%%k.%%l_%%m"
echo [OK] Найдена Java версии: %JAVA_VERSION%

REM Создание директорий
echo [2/5] Создание директорий...
if not exist %OUT_DIR% mkdir %OUT_DIR%
if not exist %DIST_DIR% mkdir %DIST_DIR%
if not exist saves mkdir saves
echo [OK] Директории созданы

REM Очистка старых файлов
echo [3/5] Очистка старых файлов...
if exist %OUT_DIR%\* del /q /s %OUT_DIR%\* >nul 2>&1
if exist %DIST_DIR%\* del /q %DIST_DIR%\* >nul 2>&1
echo [OK] Очистка завершена

REM Компиляция
echo [4/5] Компиляция исходного кода...
if exist %SRC_DIR% (
    REM Если исходники в папке src
    javac -encoding UTF-8 -d %OUT_DIR% -sourcepath %SRC_DIR% %SRC_DIR%\%PACKAGE%\*.java %SRC_DIR%\%PACKAGE%\core\*.java %SRC_DIR%\%PACKAGE%\model\*.java 2> compile_errors.log
) else (
    REM Если исходники в текущей директории
    javac -encoding UTF-8 -d %OUT_DIR% %PACKAGE%\*.java %PACKAGE%\core\*.java %PACKAGE%\model\*.java 2> compile_errors.log
)

if %errorlevel% equ 0 (
    echo [OK] Компиляция успешна!
    if exist compile_errors.log del compile_errors.log
) else (
    echo [ОШИБКА] Ошибка компиляции! Смотрите compile_errors.log
    type compile_errors.log
    pause
    exit /b 1
)

REM Создание JAR файла
echo [5/5] Создание JAR файла...
cd %OUT_DIR%
jar cfe ..\dist\MunchkinDungeon.jar com.example.dungeon.Main com\
cd ..

if exist dist\MunchkinDungeon.jar (
    echo [OK] JAR файл создан: dist\MunchkinDungeon.jar
) else (
    echo [ОШИБКА] Не удалось создать JAR файл
    pause
    exit /b 1
)

REM Создание файла запуска
echo @echo off > run.bat
echo chcp 65001 ^> nul >> run.bat
echo title Манчкин: Подземелье >> run.bat
echo java -cp out com.example.dungeon.Main >> run.bat
echo pause >> run.bat

REM Итоговая информация
echo.
echo ======================================
echo     СБОРКА ЗАВЕРШЕНА УСПЕШНО!
echo ======================================
echo.
echo Варианты запуска:
echo   1. run.bat                      - запуск из классов
echo   2. java -jar dist\MunchkinDungeon.jar - запуск JAR
echo   3. Двойной клик по MunchkinDungeon.jar
echo.
echo Структура проекта:
echo   out\     - скомпилированные классы  
echo   dist\    - JAR файл
echo   saves\   - сохранения игры
echo.
echo Нажмите любую клавишу для выхода...
pause > nul
