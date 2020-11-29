# Debug-panel

#### Бибилиотека для отладки приложений. 
### v 0.5.8

**Важно** : Библиотека находится в стадии разработки.

Тебе надоело каждый раз вручную вбивать логин и пароль тестового пользователя или пересобирать приложение для того чтобы поменять сервер в настройках? Эта библиотека делалась с идеей решить эти и другие проблемы, и сделать процесс отладки приложения более удобным. 

В данный момент библиотека предоставляет следующий функционал:

1. **Добавление, редактирование и выбор юзера.**
2. **Добавление, редактирование и выбор сервера.**
3. **Просмотр и редактирование SharedPreferences.**

Библиотека пишется с испольщованием подхода работы с плагинами, когда каждый функционал подключается отдельным модулем в зависимостях.

## Подключение библиотеки

Для работы с библиотекой необходимо:

1. Подключить репозиторий в ваш `build.gradle`  файл

```groovy
maven {
    credentials {
        username 'nexus_user_name'
        password 'nexus_user_password'
    }
    url 'https://nexus.redmadrobot.com/repository/android/'
}
```



2. Подключить `Core` модуль для работы самой панели:

```groovy

dependencies {
    //core модуль панели
    implementation 'com.redmadrobot.debug:panel_core:${debug_panel_version}'
}
```



3. Подключить плагины для того функционала который вам нужен

```groovy
dependencies {
    //Плагин для работы с аккаунтами
    implementation 'com.redmadrobot.debug:accounts_plugin:${debug_panel_version}'
    
    //Плагин для работы с серверами
    implementation 'com.redmadrobot.debug:servers_plugin:${debug_panel_version}'
    
    //Плагин для работы с SharedPreferences
    implementation 'com.redmadrobot.debug:app_settings_plugin:${debug_panel_version}'
}

```



4. Для того чтобы библиотека не попала в релизную сборку необходимо подключить `no-op` версию библиотеки

```groovy
   releaseImplementation 'com.redmadrobot.debug:panel_no_op:${debug_panel_version}'
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
                AccountsPlugin(/*arguments*/),
                ServersPlugin(/*arguments*/),
                AppSettingsPlugin(/*arguments*/)
            )
        )
    }
}
```



Для того чтобы открыть панель внутри приложения достаточно встряхнуть устройство, либо бызвать в коде:

```kotlin
fun openDebugPanel() {
    DebugPanel.showPanel(supportFragmentManager)
}
```

Так же у панели есть режим редактирования который можно открыть через центр уведомлений.

![Режим редактирования](https://git.redmadrobot.com/r.choryev/Debug-panel/raw/master/assets/debug_notification.png)

## Работа с плагинами

### AccountsPlugin 
Используется для работы тестовыми аккаунтами.

Можно задать список предустановленных аккаунтов

```kotlin
AccountsPlugin(
    preInstalledAccounts = listOf(
        DebugAccount(
            login = "user_login",
            password = "user_password",
            pin = "pin" //необязательное поле
        )
    )
)
```

Чтобы реагировать на смену пользователя вы можете подписаться на события `DebugPanel` внутри любого `LifecycleOwner` 

```kotlin
DebugPanel.subscribeToEvents(lifecycleOwner = this) { event ->
    when (event) {
        is AccountSelectedEvent -> {
            val account = event.debugAccount
          //Реализация перелогина
        }
    }
}
```

Так же вы можете использовать интерфейс `DebugAuthenticator` чтобы реализовать логику перелогина в отдельном классе который можно передать в плагин.

```kotlin
class UserAuthenticator : DebugAuthenticator {
    override fun onAccountSelected(account: DebugAccount) {
         //Реализация перелогина
    }
}
```

```kotlin
AccountsPlugin(
    preInstalledAccounts = listOf(),
    debugAuthenticator = UserAuthenticator()
)
```

Метод `onAccountSelected` будет вызываться при каждом выборе аккаунта

### ServersPlugin
Используется для работы с тестовыми серверами

Можно задать список предустановленных серверов

``` kotlin
ServersPlugin(
    preInstalledServers = listOf(
        DebugServer(
            name = "server_name",
            url = "https://debug_server.com"
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
### AppSettingsPlugin 
Используется для просмотра и редактирования `SharedPreferences` в проекте

Для подключения плагина, необходимо передать в него список `SharedPreferences` с которыми хотите работать:

```kotlin
 AppSettingsPlugin(
     sharedPreferences = listOf(
         primarySharedPreferences,
         secondarySharedPreferences
     )
 )
```


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


## Backlog разработки

- [ ]  Управление Feature toggle
- [ ]  Логирование
    - [ ]  Логирование запросов
    - [ ]  Логирование аналитики
    - [ ]  Логирование ошибок(под вопросом)
- [ ]  Изменение настроек(Необходим ресерч)
    - [ ]  Настройки proxy.
    - [ ]  включение/отключение режима show layout bounds.
    - [ ]  включение/отключение профилирования GPU
- [ ]  Создание Deep-link
- [ ]  Сброс окружения/настроек/токенов
