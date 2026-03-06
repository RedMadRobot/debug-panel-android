## [Unreleased]

### Changes

* **Breaking changes:** Удалён `plugin-flipper`. Вместо него используйте плагин Konfeature. См. гайд по миграции: [migration guide](docs/migration_guide.md).
* **Breaking changes:** Удалён `DebugStage` из `plugin-servers`. Вместо него используйте `DebugServer`. См. [migration guide](docs/migration_guide.md).
* **Breaking changes:** Удалён `plugin-accounts`. Плагин для работы с тестовыми аккаунтами полностью удалён. См. [migration guide](docs/migration_guide.md).
* **Breaking changes:** Удалён `plugin-app-settings`. Плагин для просмотра и редактирования `SharedPreferences` полностью удалён. См. [migration guide](docs/migration_guide.md).
* **Breaking changes:** Удалён `DebugPanelConfig` и shaker mode. Параметр `config` убран из `DebugPanel.initialize()`. Открытие панели по встряхиванию устройства больше не поддерживается. См. [migration guide](docs/migration_guide.md).
* **Breaking changes:** Повышен `minSdk` с 21 до 23. Библиотека больше не поддерживает Android 5.0–5.1 (API 21–22).
* **Breaking changes:** Java compatibility повышена с 11 до 17. Требуется JDK 17+.
* **Breaking changes:** Gradle обновлён с 8.11 до 9.1.
* **Breaking changes:** Обновлён каталог версий зависимостей (2025.03.10 → 2026.02.23). Подробнее [здесь](https://github.com/RedMadRobot/gradle-version-catalogs/releases/tag/2026.02.23).
* Повышены `compileSdk` и `targetSdk` с 35 до 36.
* Удалён модуль `panel-common`. Зависимости перенесены в `panel-core` и отдельные плагины.
* Дебаг-панель теперь открывается как полноэкранная `Activity` вместо bottom sheet.
* Хранение серверов переведено с Room на DataStore.
* Добавлен статический анализатор detekt.
* Добавлен `plugin-about-app` для отображения информации о приложении в дебаг-панели.
* Добавлена возможность поиска тогглов по ключу в `plugin-konfeature`.
* Исправлена утечка памяти в `DebugPanelBroadcastReceiver`.

## [0.9.3] (2025-04-24)

### Changes

* Исправлено, что не происходило отмены регистрации Broadcast Receiver
* Исправлено, что не открывался bottom sheet дебаг панели из нотификации для API 34+
* Gradle обновлен до 8.11
* Обновлены зависимости (полный список обновлений смотрите [тут](https://github.com/RedMadRobot/gradle-version-catalogs/releases/tag/2025.03.10))

## [0.9.2] (2024-12-16)

### Changes

* Gradle обновлен до 8.9
* compileSdk и targetSdk повышены до 35
* Обновлены зависимости (полный список обновлений смотрите [тут](https://github.com/RedMadRobot/gradle-version-catalogs/releases/tag/2024.12.12))

## [0.9.1] (2024-09-18)

### Changed

* ServersPlugin больше не помечен аннотацией @DebugPanelInternal (#34)

## 0.9.0
### Изменения
* Переименованы модули.
* Gradle обновлен до 8.7
* compileSdk и targetSdk повышены до 34
* Добавлена возможность открыть DebugPanel без FragmentManager
* Добавлена поддержка Jetpack Compose
* accounts-plugin переведен на Jetpack Compose
* app-settings-plugin переведен на Jetpack Compose
* flipper-plugin переведен на Jetpack Compose
* servers-plugin переведен на Jetpack Compose
* **Breaking changes:** Изменены Maven-координаты библиотек. Наименования пакетов во всех модулях приведены к общему виду. См. [гайд по миграции][docs\migration_guide].
* **Breaking changes:** Удален плагин Variable. Вместо него используйте плагин Konfeature. См. [гайд по миграции][docs\migration_guide].

## 0.8.1
### Изменения
* Исправлен краш при остановке активити (#13)

## 0.8.0
### Изменения
* Исправлен краш на SDK 34 (#7)
* Исправлен краш при доступе к БД (#5)
* Обновлены зависимости:
  * Kotlin 1.6.20 → 1.9.23
  * Room 2.4.0 → 2.6.1
  * KotlinX Coroutines 1.3.9 → 1.8.0
* Удалено использование синтетиков
* Обновлено окружение сборки проекта

## 0.7.5
### Изменения
* Добавлена поддержка строк для плагина Flipper.
  Теперь их можно указывать в качестве изменяемых значений.

## 0.7.4
### Изменения
* Добавлена конфигурация для публикации в публичный Maven
* Проект переведен на Gradle Kotlin DSL

## 0.7.3
### Изменения

* Исправлена ошибка с отсутствием события изменения сервера.
* Метод observeEvents в DebugPanel возвращает не nullable Flow

## 0.7.2
### Изменения
* Добавил группирование для групп тоглов (без под групп).
  Добавил возможность добавлять источники в рантайме Добавил возможность выбирать между источниками в рантайме.
  Изменения в поведении: теперь в изменеённых тоглах будут приходить состояния всех тоглов, а не только имзененных

* Добавлен плагин для быстрой подмены переменных в проекте VariablePlugin

## 0.6.9
### Изменения
Исправлены ошибки в работе `flipper-plugin` для релизной сборки

## 0.6.8
### Изменения
Добавлен [flipper-plugin](../plugins/flipper-plugin) для удобной работы с библиотекой [Flipper](https://github.com/RedMadRobot/flipper)

## 0.6.7
### Изменения
* Полностью удален Rx из зависимостей.
* Удален Groupie. Для списков используется [itemsAdapter](https://github.com/RedMadRobot/itemsadapter)
* Kotlin обновлен до 1.5.21.
* Android Gradle plugin обновлен до 4.2.0.
* Изменен способ удаления добавленных вручную элементов.
* Немного доработана цветовая тема библиотеки.
* Добавлена поддержка **viewbinding**.
* Исправлена проблема с задвоением логов при использовании Timber.
* Исправлены мелкие баги.

[migration-guide]: migration_guide.md
