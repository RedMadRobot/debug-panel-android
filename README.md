# Debug-panel

Библиотека для отладки приложений.

<img width="1925" height="1090" alt="debug_preview" src="https://github.com/user-attachments/assets/52d83dcb-a8e6-4c86-9d65-87b27d4796bf" />

[![Maven Central Version](https://img.shields.io/maven-central/v/com.redmadrobot.debug/panel-core?style=flat-square)](https://central.sonatype.com/search?namespace=com.redmadrobot.debug)
[![License](https://img.shields.io/github/license/RedMadRobot/debug-panel-android?style=flat-square)][license]
[![Android](https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white)](#)

**[Changelog][changelog]** | **[Миграция на новые версии][migration-guide]**

Тебе надоело пересобирать приложение для того чтобы поменять сервер в настройках или переключить feature toggle? Эта библиотека разрабатывается с идеей решить эти и другие проблемы, и сделать процесс отладки приложения более удобным.

В данный момент библиотека предоставляет следующий функционал:

1. **Добавление, редактирование и выбор сервера.**
2. **Управление feature-toggles и remote config на основе Konfeature.**
3. **Отображение информации о приложении.**

Библиотека разрабатывается используя подход работы с плагинами, когда каждый функционал подключается отдельным модулем в зависимостях.

## Подключение библиотеки

Для работы с библиотекой необходимо:

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
    debugImplementation("com.redmadrobot.konfeature:konfeature:${konfeature_version}")

    // Плагин для отображения информации о приложении
    debugImplementation("com.redmadrobot.debug:plugin-about-app:${debug_panel_version}")
}

```

3. Для того чтобы библиотека не попала в релизную сборку необходимо подключить `no-op` версию библиотеки

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

Для того чтобы открыть DebugPanel, нужно вызвать в коде:

```kotlin
fun openDebugPanel() {
    DebugPanel.showPanel(activity)
}
```

Так же в панель можно войти через уведомление которое появляется при запуске приложения использующее библиотеку. Через это же уведомление можно перейти в ручную настройку панели. Для этого нужно нажать кнопку `SETTINGS` в раскрытом уведомлении.

![Режим редактирования](assets/debug_notification.png)

## Работа с плагинами

### ServersPlugin
Используется для работы с тестовыми серверами

Можно задать список предустановленных серверов

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

И подписаться на событие смены сервера

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

Для получения выбранного сервера или **default** сервера из кода:

```kotlin
   val selectedServer = ServersPlugin.getSelectedServer()
   val defaultServer = ServersPlugin.getDefaultServer()
```

Так же если вы используете `OkHttp` в своем сетевом стеке то можете использовать `DebugServerInterceptor` который будет автоматически подменять хост в запросах на выбранный вами.

```kotlin
OkHttpClient.Builder()
    .addInterceptor(DebugServerInterceptor())
    .build()
```

Если запросы должны еще как то модифицироваться, например добавляться Header'ы то это можно сделать используя метод `modifyRequest`

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
Текущий выбранный сервер можно получить следующим образом

```kotlin
val selectedServer = getPlugin<ServersPlugin>().getSelectedServer()
```



### Konfeature Plugin

В основе плагина лежит библиотека [Konfeature][konfeature], которая позволяет:

- отображать конфигурации feature, которые используются в konfeature
- видеть источник каждого элемента конфигурации (Default, Firebase, AppGallery и т.д.)
- переопределять значение элементов конфигурации с типом Boolean, String, Long, Double

Для подключения плагина, необходимо передать в него объект класса `KonfeatureDebugPanelInterceptor` и `Konfeature`

```kotlin
val debugPanelInterceptor = KonfeatureDebugPanelInterceptor(context)

val konfeatureInstance = konfeature {
    if (isDebug) {
        addIntercepot(debugPanelInterceptor)
    }
}

KonfeaturePlugin(
    debugPanelInterceptor = debugPanelInterceptor,
    konfeature = konfeatureInstance,
)
```

В builder konfeture можно настроить следующее:

- добавить config конкретной фичи - `register(FeatureConfigN())`
- настроить работу с remote config через реализацию интерфейса `FeatureSource` - `addSource(featureSource)`
- настроить логирование - `setLogger(logger)`

### AboutApp Plugin

Используется для отображения информации о приложении: версии, номера билда и других произвольных данных.

Для подключения плагина необходимо передать список `AboutAppInfo`. Требуется хотя бы один элемент:

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
- `value` — значение поля (например, «1.0.0»)

# Безопасность!
Для того чтобы тестовые данные не попали в релизные сборки рекомендуется не задавать их явно в Application классе, а использовать реализации DebugDataProvider, которые можно разнести по разным buildType. Для release версии следует сделать пустую реализацию.

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
Добавление в плагин

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
