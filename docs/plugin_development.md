# Разработка новых плагинов

## Общая структура

Debug Panel построена на подходе с использованием плагинов — каждая функциональность реализуется в виде отдельного модуля-плагина.

Базовые модули, на которых основана работа панели:

* **panel-core** — реализация панели, базовые классы системы плагинов и событийная модель.
* **panel-no-op** — пустые реализации публичных API для релизных сборок (исключает отладочный код из продакшена).

## Создание нового плагина

### 1. Создать модуль

Создайте новый модуль в директории `plugins/`:

```
plugins/
└── plugin-your-feature/
```

### 2. Зарегистрировать модуль в settings.gradle.kts

Добавьте модуль по аналогии с существующими плагинами:

```kotlin
// Plugins
include(
    ":plugins:plugin-your-feature",
)
```

### 3. Настроить build.gradle.kts

Примените convention-плагин, который содержит всю необходимую конфигурацию (compileSdk, minSdk, `explicitApi()`, зависимость на `panel-core` и Compose):

```kotlin
plugins {
    id("convention.debug.panel.plugin")
}

description = "Plugin description"

android {
    namespace = "com.redmadrobot.debug.plugin.yourfeature"
}

dependencies {
    // Только специфичные для плагина зависимости
}
```

### 4. Создать класс плагина

Класс плагина — точка входа, отвечающая за взаимодействие с DebugPanel.
Унаследуйтесь от `Plugin()` и реализуйте обязательные методы.
Подробнее о `getPluginContainer()` и `PluginDependencyContainer` — в разделе ниже.

```kotlin
public class YourPlugin(
    /* аргументы для инициализации */
) : Plugin() {

    override fun getName(): String = "YOUR PLUGIN"

    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return YourPluginContainer(commonContainer)
    }

    @Composable
    override fun content() {
        YourScreen()
    }
}
```

Если плагин поддерживает редактирование через экран настроек панели, реализуйте интерфейс `EditablePlugin`:

```kotlin
public class YourPlugin : Plugin(), EditablePlugin {
    // ...

    @Composable
    override fun content() {
        YourScreen(isEditMode = false)
    }

    @Composable
    override fun settingsContent() {
        YourScreen(isEditMode = true)
    }
}
```

### 5. Создать UI на Jetpack Compose

UI плагина реализуется с помощью Composable-функций. Для инъекции ViewModel используется хелпер `provideViewModel`:

```kotlin
@Composable
internal fun YourScreen(
    viewModel: YourViewModel = provideViewModel {
        getPlugin<YourPlugin>()
            .getContainer<YourPluginContainer>()
            .createYourViewModel()
    },
) {
    val state by viewModel.state.collectAsState()
    // UI
}
```

## PluginDependencyContainer

В библиотеке не используются DI-фреймворки, чтобы не добавлять лишних зависимостей. Вместо этого применяется подход **Service Locator**.

Для этого создайте класс-контейнер, реализующий интерфейс `PluginDependencyContainer`, и инициализируйте в нём необходимые зависимости.
`Context` доступен через `CommonContainer`, который передаётся в метод `getPluginContainer()` при инициализации плагина.

```kotlin
internal class YourPluginContainer(
    private val container: CommonContainer,
) : PluginDependencyContainer {

    private val dataStore by lazy { YourDataStore(container.context) }

    val repository by lazy { YourRepository(dataStore) }

    fun createYourViewModel(): YourViewModel {
        return YourViewModel(repository)
    }
}
```

Пример реализации: [ServersPluginContainer](../plugins/plugin-servers/src/main/kotlin/com/redmadrobot/debug/plugin/servers/ServersPluginContainer.kt)

## Работа с классом плагина

Класс плагина является точкой доступа к данным и зависимостям.
Для получения экземпляра плагина используйте `getPlugin<YourPlugin>()`:

```kotlin
getPlugin<YourPlugin>()
    .getContainer<YourPluginContainer>()
```

## Области видимости

Все модули используют `explicitApi()` — модификаторы видимости обязательны для всех объявлений.
Внутренние классы, не предназначенные для использования в клиентском приложении, должны иметь модификатор `internal`.

## Тестирование

Для тестирования плагина:

1. Подключите его как зависимость в модуль `sample`:

```kotlin
debugImplementation(project(":plugins:plugin-your-feature"))
```

2. Инициализируйте плагин в классе `App` sample-приложения:

```kotlin
DebugPanel.initialize(
    application = this,
    plugins = listOf(
        YourPlugin(/* ... */)
    )
)
```

3. Запустите sample-проект.

## No-op реализации

Чтобы отладочный код не попадал в релизную сборку, для каждого плагина необходимо создать no-op реализацию в модуле **panel-no-op**.

Создайте пакет с публичными классами плагина, доступными пользователю, и предоставьте пустые реализации.

Важно: **package** должен совпадать с оригинальным пакетом вашего модуля.

В sample-приложении подключение выглядит так:

```kotlin
debugImplementation(project(":plugins:plugin-your-feature"))
releaseImplementation(project(":panel-no-op"))
```

Подробнее о подходе: [No-op versions for dev tools](https://medium.com/@orhanobut/no-op-versions-for-dev-tools-b0a865934398)

## Публикация

Публикация новых плагинов проходит через создание **Pull Request** в ветку **main**.
