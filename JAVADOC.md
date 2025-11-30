# Генерация JavaDoc документации

## Способ 1: Через Maven (рекомендуется)

### Генерация документации

```bash
mvn javadoc:javadoc
```

### Где найти документацию
После успешной генерации откройте файл:
```
target/site/apidocs/index.html
```

### Генерация JAR файла с документацией

```bash
mvn javadoc:jar
```

Будет создан файл: `target/lab2-1.0-javadoc.jar`

---

## Способ 2: Через стандартный javadoc (без Maven)

Если Maven недоступен, используйте стандартный инструмент javadoc:

```bash
# Создание директории для документации
mkdir -p docs/javadoc

# Генерация JavaDoc для всех классов
javadoc -d docs/javadoc \
  -sourcepath src/main/java \
  -subpackages com.pizzeria \
  -encoding UTF-8 \
  -charset UTF-8 \
  -docencoding UTF-8 \
  -author \
  -version \
  -use \
  -windowtitle "Pizzeria Management System API" \
  -doctitle "Pizzeria Management System Documentation" \
  -header "Pizzeria API" \
  -bottom "Copyright © 2025 Lab2"
```

### Где найти документацию
```
docs/javadoc/index.html
```

---

## Способ 3: Упрощенная команда javadoc

```bash
# Простая генерация в директорию docs
javadoc -d docs -sourcepath src/main/java -subpackages com.pizzeria -encoding UTF-8
```

---

## Полезные опции javadoc

### Базовые опции:
- `-d <directory>` - директория для вывода
- `-sourcepath <path>` - путь к исходникам
- `-subpackages <pkglist>` - пакеты для документирования
- `-encoding UTF-8` - кодировка исходников

### Опции форматирования:
- `-author` - включить информацию об авторе
- `-version` - включить информацию о версии
- `-use` - создать страницы использования классов
- `-windowtitle <text>` - заголовок окна браузера
- `-doctitle <html-code>` - заголовок документации
- `-header <html-code>` - заголовок каждой страницы
- `-footer <html-code>` - подвал каждой страницы
- `-bottom <html-code>` - нижний текст

### Опции для исключений:
- `-exclude <pkglist>` - исключить пакеты
- `-public` - показывать только public элементы
- `-protected` - показывать public и protected (по умолчанию)
- `-package` - показывать package, protected и public
- `-private` - показывать все элементы

---

## Автоматическая генерация при сборке

Если хотите, чтобы документация генерировалась при каждой сборке, добавьте в pom.xml:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-javadoc-plugin</artifactId>
      <executions>
        <execution>
          <id>attach-javadocs</id>
          <phase>package</phase>
          <goals>
            <goal>jar</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

Тогда документация будет создаваться командой:
```bash
mvn package
```

---

## Проверка сгенерированной документации

1. **Откройте index.html** в браузере
2. **Проверьте навигацию:**
   - Список всех пакетов (слева)
   - Список всех классов
   - Детальная информация по каждому классу

3. **Что должно быть в документации:**
   - Описание классов
   - Список методов с параметрами
   - Список полей
   - Наследование классов
   - Реализованные интерфейсы

---

## Улучшение документации

Для лучшей документации добавьте JavaDoc комментарии к классам и методам:

```java
/**
 * Сервис управления заказами.
 * Предоставляет функциональность для создания, получения и управления заказами.
 *
 * @author Lab2 Team
 * @version 1.0
 * @since 2025
 */
public class OrderService {

    /**
     * Создает новый заказ для клиента.
     *
     * @param customer клиент, создающий заказ
     * @return созданный заказ с уникальным ID
     * @throws DuplicateOrderException если заказ с таким ID уже существует
     */
    public Order createOrder(Customer customer) throws DuplicateOrderException {
        // ...
    }
}
```

---

## Быстрая генерация (без интернета)

Создайте скрипт `generate-docs.sh`:

```bash
#!/bin/bash

echo "Генерация JavaDoc документации..."

javadoc -d docs/javadoc \
  -sourcepath src/main/java \
  -subpackages com.pizzeria \
  -encoding UTF-8 \
  -charset UTF-8 \
  -windowtitle "Pizzeria API" \
  -doctitle "<h1>Pizzeria Management System</h1>" \
  -quiet

if [ $? -eq 0 ]; then
  echo "✅ Документация успешно создана в docs/javadoc/"
  echo "Откройте docs/javadoc/index.html в браузере"
else
  echo "❌ Ошибка при генерации документации"
fi
```

Использование:
```bash
chmod +x generate-docs.sh
./generate-docs.sh
```

---

## Просмотр документации

### Linux/Mac:
```bash
# Firefox
firefox docs/javadoc/index.html

# Chrome
google-chrome docs/javadoc/index.html

# Стандартный браузер
xdg-open docs/javadoc/index.html
```

### Windows:
```bash
start docs\javadoc\index.html
```

---

## Что делать если нет интернета

JavaDoc - это стандартный инструмент JDK, он не требует интернета!

Просто используйте команду:
```bash
javadoc -d docs -sourcepath src/main/java -subpackages com.pizzeria -encoding UTF-8
```

Документация будет создана локально без каких-либо загрузок.
