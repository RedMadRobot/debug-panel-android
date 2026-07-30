# Debug-panel

Библиотека предоставляет встроенную отладочную панель для Android-приложений, предназначенную для использования в debug и QA-сборках.

<video src="https://github.com/user-attachments/assets/c8935f5d-1458-4e4d-9008-120ef0300013" width="300" controls>
  <a href="https://github.com/user-attachments/assets/c8935f5d-1458-4e4d-9008-120ef0300013">Watch the debug-panel demo</a>
</video>

[![Maven Central Version](https://img.shields.io/maven-central/v/com.redmadrobot.debug/panel-core?style=flat-square)](https://central.sonatype.com/search?namespace=com.redmadrobot.debug)
[![License](https://img.shields.io/github/license/RedMadRobot/debug-panel-android?style=flat-square)][license]
[![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)](#)

**[Changelog][changelog]** | **[Миграция на новые версии][migration-guide]** | **[Документация по разработке плагинов][plugin-development-doc]**

Панель позволяет управлять состоянием приложения в runtime без внесения изменений в основной код.

Основные возможности:

1. Добавление, редактирование и выбор сервера.
2. Управление feature-toggles и remote config на основе Konfeature.
3. Отображение информации о приложении.

Каждая функциональность подключается отдельным плагином.

## Подключение библиотеки

Для работы с библиотекой необходимо выполнить следующие шаги:

1. Подключить `Core` модуль для работы самой панели:

```kotlin

dependencies {
    // Core модуль панели
   debugImplementation("com.redmadrobot.debug:panel-core:${debug_panel_version}")
}
```



2. Подключить необходимые плагины

```kotlin
dependencies {
    // Плагин для работы с серверами
    debugImplementation("com.redmadrobot.debug:plugin-servers:${debug_panel_version}")

    // Плагин для работы с remote config на основе Konfeature
    debugImplementation("com.redmadrobot.debug:plugin-konfeature:${debug_panel_version}")
    // Так же необходимо подключить саму библиотеку konfeature
    implementation("com.redmadrobot.konfeature:konfeature:${konfeature_version}")

    // Плагин для отображения информации о приложении
    debugImplementation("com.redmadrobot.debug:plugin-about-app:${debug_panel_version}")
}

```

3. Для того, чтобы библиотека не попала в релизную сборку, необходимо подключить `no-op` версию библиотеки

```kotlin
   releaseImplementation("com.redmadrobot.debug:panel-no-op:${debug_panel_version}")
```

## Использование библиотеки в коде

Общий принцип подключения библиотеки в коде выглядит так:

```kotlin
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        DebugPanel.initialize(
            application = this,
            plugins = listOf(
                ServersPlugin(/*arguments*/),
                KonfeaturePlugin(/*arguments*/),
                AboutAppPlugin(/*arguments*/),
            )
        )
    }
}
```

Для открытия DebugPanel необходимо вызвать:

```kotlin
fun openDebugPanel() {
    DebugPanel.showPanel(activity)
}
```

Также панель доступна через уведомление, которое появляется при запуске приложения, использующего библиотеку. Через это уведомление можно перейти к ручной настройке панели, нажав кнопку `SETTINGS` в раскрытом уведомлении.

![Режим редактирования](assets/debug_notification.png)

## Работа с плагинами

### ServersPlugin

<img width="300" alt="servers-screen" src="https://github.com/user-attachments/assets/5247295a-00d4-45a0-833a-eb5a589fd456" />

Используется для работы с тестовыми серверами

Доступна возможность задать список предустановленных серверов

```kotlin
ServersPlugin(
    preInstalledServers = listOf(
        DebugServer(
            name = "server_name",
            url = "https://debug_server.com",
            isDefault = true /*!!!Обязательно должен быть указан хотя бы один сервер по умолчанию*/
        )
    )
)
```

Подписка на событие смены сервера

```kotlin
DebugPanel.subscribeToEvents(lifecycleOwner = this) { event ->
    when (event) {
        is ServerSelectedEvent -> {
            val debugServer = event.debugServer
            // логика переключения сервера
        }
    }
}
```

Получение выбранного сервера или сервера по умолчанию:

```kotlin
   val selectedServer = ServersPlugin.getSelectedServer()
   val defaultServer = ServersPlugin.getDefaultServer()
```

При использовании `OkHttp` в сетевом стеке можно применить `DebugServerInterceptor`, который автоматически подменяет хост в запросах на выбранный сервер.

```kotlin
OkHttpClient.Builder()
    .addInterceptor(DebugServerInterceptor())
    .build()
```

Если запросы требуют дополнительной модификации, например добавления заголовков, можно воспользоваться методом `modifyRequest`

```kotlin
OkHttpClient.Builder()
   .addInterceptor(
       DebugServerInterceptor().modifyRequest { request, server ->
           if (server.name == "Test") {
               request.newBuilder()
                   .addHeader("Authorization", "testToken")
                   .build()
           } else {
               request
           }
       }
   )
   .build()
```
Получение текущего выбранного сервера

```kotlin
val selectedServer = getPlugin<ServersPlugin>().getSelectedServer()
```



### Konfeature Plugin

<img width="300" alt="konfeature-screen" src="https://github.com/user-attachments/assets/b9608e53-2c8d-42ce-9c9f-715ac5fc6f3a" />

В основе плагина лежит библиотека [Konfeature][konfeature], которая позволяет:

- отображать конфигурации feature, используемые в Konfeature
- просматривать источник каждого элемента конфигурации (Default, Firebase, AppGallery и др.)
- переопределять значения тогглов типа `Boolean` прямо в панели (значения остальных типов отображаются только для чтения)

Экран, список фич и хранение переопределений (DataStore) предоставляет библиотека [`konfeature-ui`][konfeature]. Плагин лишь встраивает её экран в дебаг-панель. Переопределённые значения сохраняются между перезапусками приложения.

Для подключения нужно создать `KonfeatureDebugPanelConfig`, подключить его к экземпляру `Konfeature` через `applyDebugPanelConfig(...)` и передать тот же самый конфиг в `KonfeaturePlugin`:

```kotlin
// создаётся один раз на старте приложения; create() — suspend-функция
val config = KonfeatureDebugPanelConfig.create(context)

val konfeatureInstance = konfeature {
    register(MyFeatureConfig())
    applyDebugPanelConfig(config)
}

KonfeaturePlugin(
    konfeature = konfeatureInstance,
    config = config,
)
```

> Важно: в `KonfeaturePlugin` необходимо передать тот же самый `config`, который был подключён к `Konfeature` через `applyDebugPanelConfig(...)`. Если конфиг не был подключён, конструктор плагина упадёт с ошибкой, так как панель показывала бы значения, которые не может переопределить.

В builder Konfeature доступны следующие настройки:

- добавление конфигурации конкретной фичи — `register(FeatureConfigN())`
- настройка работы с remote config через реализацию интерфейса `FeatureSource` — `addSource(featureSource)`
- настройка логирования — `setLogger(logger)`

### AboutApp Plugin

<img width="300" alt="about-app-screen" src="https://github.com/user-attachments/assets/37f42693-0fb0-4475-89a3-384d21e74e97" />

Предназначен для отображения информации о приложении: версии, номера сборки и других произвольных данных.

Для подключения плагина необходимо передать список объектов `AboutAppInfo`, содержащий хотя бы один элемент:

```kotlin
AboutAppPlugin(
    aboutAppInfo = listOf(
        AboutAppInfo(
            title = "Версия",
            value = BuildConfig.VERSION_NAME
        ),
        AboutAppInfo(
            title = "Номер билда",
            value = BuildConfig.VERSION_CODE.toString()
        )
    )
)
```

Каждый `AboutAppInfo` содержит:
- `title` — название поля (например, «Версия»)
- `value` — значение поля (например, «1.3.0»)

В плагин можно опционально передать список пользовательских действий — `AboutAppAction`. Каждое действие отображается отдельной кнопкой под блоком с информацией о приложении.

У `AboutAppAction` есть 2 варианта для использования:

- `AboutAppAction.Event` — при нажатии публикует переданный `DebugEvent` в шину событий панели. Это рекомендуемый способ для прикладного кода.
- `AboutAppAction.Direct` — выполняет обработчик `onClick` напрямую с `Context`. Он зарезервирован для встроенных действий самой библиотеки, и в коде приложения использовать его не следует.

Каждый `AboutAppAction` содержит:
- `title` — подпись кнопки (например, «Открыть ui-kit»)
- `debugEvent` — событие, публикуемое при нажатии

Пример с несколькими действиями:

```kotlin
object OpenUiKitEvent : DebugEvent
object ClearCacheEvent : DebugEvent
object CopyTokenEvent : DebugEvent

AboutAppPlugin(
    appInfoList = listOf(/*...*/),
    actions = listOf(
        AboutAppAction.Event(
            title = "Открыть ui-kit",
            debugEvent = OpenUiKitEvent,
        ),
        AboutAppAction.Event(
            title = "Очистить кэш",
            debugEvent = ClearCacheEvent,
        ),
        AboutAppAction.Event(
            title = "Скопировать токен",
            debugEvent = CopyTokenEvent,
        ),
    )
)
```

Обработка событий — через `observeEvents()`. Можно подписаться как на одно конкретное событие, так и на несколько:

```kotlin
DebugPanel.observeEvents()
    .filterIsInstance(OpenUiKitEvent::class)
    .onEach { /** Event handling logic **/ }
    .launchIn(lifecycleScope)
```

```kotlin
DebugPanel.observeEvents()
    .onEach { event ->
        when (event) {
            is OpenUiKitEvent -> { /** Event handling logic **/ }
            is ClearCacheEvent -> { /** Event handling logic **/ }
            is CopyTokenEvent -> { /** Event handling logic **/ }
        }
    }
    .launchIn(lifecycleScope)
```

# Безопасность

Для предотвращения попадания тестовых данных в релизные сборки рекомендуется не задавать их явно в классе Application, а использовать реализации `DebugDataProvider`, которые можно разнести по разным `buildType`. Для release-версии следует создать пустую реализацию.

**buildType**  `debug`

```kotlin
class DebugServersProvider : DebugDataProvider<List<DebugServer>> {

    override fun provideData(): List<DebugServer> {
        return listOf(
            DebugServer(name = "debug 1", url = "https://testserver1.com")
        )
    }
}
```
**buildType**  `release`

```kotlin
class DebugServersProvider : DebugDataProvider<List<DebugServer>> {

    override fun provideData(): List<DebugServer> {
        return emptyList()
    }
}
```
Передача в плагин

```kotlin
ServersPlugin(
    preInstalledServers = DebugServersProvider()
)
```

[plugin-development-doc]:docs/plugin_development.md
[changelog]: ./CHANGELOG.md
[migration-guide]: docs/migration_guide.md
[konfeature]: https://github.com/RedMadRobot/Konfeature
[license]: LICENSE
