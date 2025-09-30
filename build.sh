#!/bin/bash

# Скрипт сборки Манчкин: Подземелье для Linux/Mac

echo "======================================"
echo "   МАНЧКИН: ПОДЗЕМЕЛЬЕ - СБОРКА      "
echo "======================================"
echo ""

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Директории
SRC_DIR="src"
OUT_DIR="out"
LIB_DIR="lib"
DIST_DIR="dist"
PACKAGE="com/example/dungeon"

# Проверка наличия Java
echo -e "${YELLOW}[1/5]${NC} Проверка установки Java..."
if ! command -v java &> /dev/null; then
    echo -e "${RED}[ОШИБКА]${NC} Java не найдена! Установите JDK 8 или выше."
    exit 1
fi

if ! command -v javac &> /dev/null; then
    echo -e "${RED}[ОШИБКА]${NC} Java компилятор не найден! Установите JDK."
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f 2)
echo -e "${GREEN}[OK]${NC} Найдена Java версии: $JAVA_VERSION"

# Создание директорий
echo -e "${YELLOW}[2/5]${NC} Создание директорий..."
mkdir -p $OUT_DIR
mkdir -p $DIST_DIR
mkdir -p saves
echo -e "${GREEN}[OK]${NC} Директории созданы"

# Очистка старых файлов
echo -e "${YELLOW}[3/5]${NC} Очистка старых файлов..."
rm -rf $OUT_DIR/*
rm -rf $DIST_DIR/*
echo -e "${GREEN}[OK]${NC} Очистка завершена"

# Компиляция
echo -e "${YELLOW}[4/5]${NC} Компиляция исходного кода..."
if [ -d "$SRC_DIR" ]; then
    # Если исходники в папке src
    javac -d $OUT_DIR -sourcepath $SRC_DIR $SRC_DIR/$PACKAGE/**/*.java 2> compile_errors.log
else
    # Если исходники в текущей директории
    javac -d $OUT_DIR $PACKAGE/**/*.java 2> compile_errors.log
fi

if [ $? -eq 0 ]; then
    echo -e "${GREEN}[OK]${NC} Компиляция успешна!"
    rm -f compile_errors.log
else
    echo -e "${RED}[ОШИБКА]${NC} Ошибка компиляции! Смотрите compile_errors.log"
    cat compile_errors.log
    exit 1
fi

# Создание JAR файла
echo -e "${YELLOW}[5/5]${NC} Создание JAR файла..."
cd $OUT_DIR
jar cfe ../dist/MunchkinDungeon.jar com.example.dungeon.Main com/
cd ..

if [ -f "dist/MunchkinDungeon.jar" ]; then
    echo -e "${GREEN}[OK]${NC} JAR файл создан: dist/MunchkinDungeon.jar"
else
    echo -e "${RED}[ОШИБКА]${NC} Не удалось создать JAR файл"
    exit 1
fi

# Создание скрипта запуска
echo "#!/bin/bash" > run.sh
echo "java -cp out com.example.dungeon.Main" >> run.sh
chmod +x run.sh

# Итоговая информация
echo ""
echo "======================================"
echo -e "${GREEN}    СБОРКА ЗАВЕРШЕНА УСПЕШНО!${NC}"
echo "======================================"
echo ""
echo "Варианты запуска:"
echo "  1. ./run.sh                     - запуск из классов"
echo "  2. java -jar dist/MunchkinDungeon.jar - запуск JAR"
echo ""
echo "Структура проекта:"
echo "  out/     - скомпилированные классы"
echo "  dist/    - JAR файл"
echo "  saves/   - сохранения игры"
echo ""
echo "Удачной игры!"
