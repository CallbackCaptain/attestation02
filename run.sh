#!/bin/bash

# Скрипт запуска Манчкин: Подземелье для Linux/Mac

# Цвета для вывода
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

clear

# Проверка наличия Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}[ОШИБКА]${NC} Java не найдена! Установите JRE 8 или выше."
    exit 1
fi

# Проверка наличия скомпилированных файлов или JAR
if [ -f "dist/MunchkinDungeon.jar" ]; then
    echo -e "${GREEN}Запуск из JAR файла...${NC}"
    echo ""
    java -jar dist/MunchkinDungeon.jar
elif [ -d "out/com/example/dungeon" ]; then
    echo -e "${GREEN}Запуск из скомпилированных классов...${NC}"
    echo ""
    java -cp out com.example.dungeon.Main
else
    echo -e "${RED}[ОШИБКА]${NC} Игра не собрана!"
    echo ""
    echo "Сначала запустите ./build.sh для сборки игры"
    echo ""
    read -p "Запустить сборку сейчас? (y/n): " -n 1 -r
    echo ""
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if [ -f "build.sh" ]; then
            ./build.sh
            if [ $? -eq 0 ]; then
                echo ""
                echo -e "${GREEN}Запуск игры...${NC}"
                echo ""
                java -cp out com.example.dungeon.Main
            fi
        else
            echo -e "${RED}[ОШИБКА]${NC} Файл build.sh не найден!"
        fi
    fi
fi
