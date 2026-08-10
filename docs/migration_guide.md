# Миграция

## Миграция на версию 1.4.0

### Приведение panel-no-op в соответствие с публичным API

Изменения касаются только `panel-no-op` — модуля, который подключается как `releaseImplementation`.
Публичный API `panel-core` и плагинов не менялся, поэтому если в приложении нет кода, компилируемого
только для release (отдельные source set'ы, `release`-флейворы), миграция не требуется.

Полнота no-op реализации теперь проверяется на сборке — см. [Разработка плагинов][plugin-development].
Ранее объявления в `panel-no-op` расходились с оригиналами по пакетам и сигнатурам; расхождения
устранены, поэтому часть импортов и вызовов в release-коде нужно поправить.

#### Пакеты объявлений

```diff
- import com.redmadrobot.debug.core.internal.DebugEvent
+ import com.redmadrobot.debug.core.DebugEvent

- import com.redmadrobot.debug.plugin.aboutapp.AboutAppAction
+ import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppAction

- import com.redmadrobot.debug.plugin.aboutapp.AboutAppInfo
+ import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo
```

#### AboutAppAction.Event

Добавлен обязательный параметр `debugEvent` — событие, которое публикуется в шину при нажатии.

```diff
  AboutAppAction.Event(
      title = "Сбросить кэш",
+     debugEvent = ResetCacheEvent,
  )
```

#### DebugPanel.showPanel(FragmentManager)

Перегрузка удалена: в `panel-core` её нет с версии 0.9.0.

```diff
- DebugPanel.showPanel(supportFragmentManager)
+ DebugPanel.showPanel(this)
```

#### ServersPlugin

Тип `preInstalledServers` уточнён с `List<Any>` до `List<DebugServer>`.

```diff
- ServersPlugin(preInstalledServers = listOf<Any>(/*...*/))
+ ServersPlugin(preInstalledServers = listOf(DebugServer(/*...*/)))
```

#### Объявления, добавленные в panel-no-op

В no-op появились `DebugPanel.isInitialized`, `AboutAppInfo.id`,
`ServersPlugin.getSelectedServer()` и `ServersPlugin.getDefaultServer()` — раньше код,
использующий их, не компилировался в release-сборке.

> `ServersPlugin.getSelectedServer()` и `ServersPlugin.getDefaultServer()` в release-сборке
> **всегда бросают** `IllegalArgumentException`: панели нет, а значит нет и выбранного сервера.
> Раньше такой вызов не компилировался, теперь он собирается и падает в рантайме.
> Если приложение берёт URL из панели, разведите источники по source set'ам или проверяйте
> `DebugPanel.isInitialized`:

```kotlin
val baseUrl = if (DebugPanel.isInitialized) {
    ServersPlugin.getSelectedServer().url
} else {
    BuildConfig.BASE_URL
}
```

## Миграция на версию 1.3.0

### Переход plugin-konfeature на библиотеку konfeature-ui

`plugin-konfeature` переведён на публичную библиотеку [`konfeature-ui`][konfeature]: экран, список фич и хранение переопределений (DataStore) теперь предоставляет она. Собственный `KonfeatureDebugPanelInterceptor`, экран, `ViewModel` и диалог редактирования удалены.

Вместо `KonfeatureDebugPanelInterceptor` используется `KonfeatureDebugPanelConfig` — он объединяет хранилище переопределений и интерцептор, гарантируя, что панель и `Konfeature` работают с одним и тем же store.

> Миграция хранимых значений не предусмотрена: ранее переопределённые значения после обновления сбрасываются к значениям из источников.

#### Инициализация

`KonfeatureDebugPanelConfig.create(...)` — `suspend`-функция, вызовите её на старте приложения из подходящего scope. Тот же `config` необходимо подключить к `Konfeature` через `applyDebugPanelConfig(...)` и передать в `KonfeaturePlugin`.

```diff
- val debugPanelInterceptor = KonfeatureDebugPanelInterceptor(context)
- val konfeatureInstance = konfeature {
-     addInterceptor(debugPanelInterceptor)
- }
- KonfeaturePlugin(
-     debugPanelInterceptor = debugPanelInterceptor,
-     konfeature = konfeatureInstance,
- )

+ val config = KonfeatureDebugPanelConfig.create(context)
+ val konfeatureInstance = konfeature {
+     register(MyFeatureConfig())
+     applyDebugPanelConfig(config)
+ }
+ KonfeaturePlugin(
+     konfeature = konfeatureInstance,
+     config = config,
+ )
```

> Конструктор `KonfeaturePlugin` упадёт с ошибкой, если переданный `config` не был подключён к `Konfeature` через `applyDebugPanelConfig(...)`.

#### Переопределение значений

Инлайн-переопределение в панели теперь доступно только для тогглов типа `Boolean`. Значения остальных типов отображаются только для чтения.

## Миграция на версию 1.0.0

### Миграция с FlipperPlugin на KonfeaturePlugin

`KonfeaturePlugin` является функциональным расширением `FlipperPlugin`.
Поддерживает больше типов значений (Boolean, String, Long, Double), отображает источник каждого значения (Default / Interceptor / Source), позволяет сбрасывать значения по отдельности.

#### Gradle

```diff
- debugImplementation("com.redmadrobot.debug-panel:plugin-flipper:<version>")
- implementation("com.redmadrobot:flipper:<version>")

+ debugImplementation("com.redmadrobot.debug-panel:plugin-konfeature:<version>")
+ implementation("com.redmadrobot:konfeature:<version>")
```

#### Инициализация

```diff
- FlipperPlugin(
-     featureStateMap = mapOf(
-         "my_toggle" to FlipperValue.Enabled,
-     )
- )

+ val debugPanelInterceptor = KonfeatureDebugPanelInterceptor(context)
+ val konfeatureInstance = konfeature {
+     addInterceptor(debugPanelInterceptor)
+ }
+ KonfeaturePlugin(
+     debugPanelInterceptor = debugPanelInterceptor,
+     konfeature = konfeatureInstance,
+ )
```

#### Наблюдение за изменёнными значениями

```diff
- FlipperPlugin.observeChangedToggles()
-     .onEach { changes -> /* handle Map<String, FlipperValue> */ }
-     .launchIn(scope)

+ // В Konfeature значения читаются напрямую через FeatureConfigSpec:
+ val myToggle: Boolean by myFeatureConfig.myToggle
```

#### Несколько источников (`addSource`)

```diff
- FlipperPlugin.addSource("remote", mapOf("my_toggle" to FlipperValue.Enabled))

+ // В Konfeature источники объявляются через FeatureSource и передаются в builder:
+ konfeature { addSource(remoteSource) }
```

### Удаление plugin-accounts

`AccountsPlugin` полностью удалён из библиотеки. Классы `AccountsPlugin`, `DebugAccount`, `DebugAuthenticator`, `AccountSelectedEvent` больше не доступны.

#### Gradle

```diff
  dependencies {
-     debugImplementation("com.redmadrobot.debug:plugin-accounts:<version>")
  }
```

#### Инициализация

```diff
  DebugPanel.initialize(
      application = this,
      plugins = listOf(
-         AccountsPlugin(
-             preInstalledAccounts = listOf(
-                 DebugAccount(login = "user", password = "pass")
-             ),
-             debugAuthenticator = UserAuthenticator()
-         ),
          ServersPlugin(/*...*/),
      )
  )
```

#### Подписка на события

```diff
  DebugPanel.subscribeToEvents(lifecycleOwner = this) { event ->
      when (event) {
-         is AccountSelectedEvent -> { /* ... */ }
          is ServerSelectedEvent -> { /* ... */ }
      }
  }
```

### Удаление plugin-app-settings

`AppSettingsPlugin` полностью удалён из библиотеки. Класс `AppSettingsPlugin` больше не доступен.

#### Gradle

```diff
  dependencies {
-     debugImplementation("com.redmadrobot.debug:plugin-app-settings:<version>")
  }
```

#### Инициализация

```diff
  DebugPanel.initialize(
      application = this,
      plugins = listOf(
-         AppSettingsPlugin(
-             sharedPreferences = listOf(primarySharedPreferences)
-         ),
          ServersPlugin(/*...*/),
      )
  )
```

### Удаление DebugPanelConfig и shaker mode

Класс `DebugPanelConfig` удалён. Параметр `config` убран из `DebugPanel.initialize()`. Открытие панели по встряхиванию устройства больше не поддерживается.

```diff
  DebugPanel.initialize(
      application = this,
-     config = DebugPanelConfig(shakerMode = false),
      plugins = listOf(/*...*/)
  )
```

### Миграция с DebugStage на DebugServer

`DebugStage` удалён из `plugin-servers`.
Концепция «стейджей» с маппингом нескольких хостов больше не поддерживается — используйте `DebugServer` с единственным URL.

#### Инициализация плагина

```diff
- ServersPlugin(
-     preInstalledServers = listOf(
-         DebugServer(name = "Production", url = "https://prod.example.com"),
-         DebugStage(
-             name = "Staging",
-             hosts = mapOf(
-                 "main" to "https://staging.example.com",
-                 "s3"   to "https://s3.staging.example.com",
-             ),
-             isDefault = true
-         ),
-     )
- )

+ ServersPlugin(
+     preInstalledServers = listOf(
+         DebugServer(name = "Production", url = "https://prod.example.com"),
+         DebugServer(name = "Staging",    url = "https://staging.example.com", isDefault = true),
+     )
+ )
```

#### OkHttp interceptor

```diff
- import com.redmadrobot.debug.plugin.servers.interceptor.DebugStageInterceptor
+ import com.redmadrobot.debug.plugin.servers.interceptor.DebugServerInterceptor

  OkHttpClient.Builder()
-     .addInterceptor(DebugStageInterceptor("main"))
+     .addInterceptor(DebugServerInterceptor())
      .build()
```

```diff
- DebugStageInterceptor("main").modifyRequest { request, stage ->
-     if (stage.name == "Staging") request.newBuilder().addHeader("Authorization", "token").build()
-     else request
- }

+ DebugServerInterceptor().modifyRequest { request, server ->
+     if (server.name == "Staging") request.newBuilder().addHeader("Authorization", "token").build()
+     else request
+ }
```

#### Удалённые API

```diff
- ServersPlugin.getSelectedStage()
- ServersPlugin.getDefaultStage()
```

Если необходимо получить текущий выбранный сервер программно, используйте `ServersPlugin.getSelectedServer()`.

## Миграция на версию 0.9.0

### Изменения Maven-координатов библиотек

```diff
- debugImplementation("com.redmadrobot.debug:accounts-plugin:<version>")
+ debugImplementation("com.redmadrobot.debug:plugin-accounts:<version>")

- debugImplementation("com.redmadrobot.debug:app-settings-plugin:<version>")
+ debugImplementation("com.redmadrobot.debug:plugin-app-settings:<version>")

- debugImplementation("com.redmadrobot.debug:flipper-plugin:<version>")
+ debugImplementation("com.redmadrobot.debug:plugin-flipper:<version>")

- debugImplementation("com.redmadrobot.debug:servers-plugin:<version>")
+ debugImplementation("com.redmadrobot.debug:plugin-servers:<version>")
```

### Изменения нейминга и структуры пакетов

```diff
- import com.redmadrobot.account_plugin.plugin.AccountsPlugin
+ import com.redmadrobot.debug.plugin.accounts.AccountsPlugin

- import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
+ import com.redmadrobot.debug.plugin.flipper.FlipperPlugin

- import com.redmadrobot.debug.appsettings.plugin.AppSettingsPlugin
+ import com.redmadrobot.debug.plugin.appsettings.AppSettingsPlugin

- import com.redmadrobot.debug.servers.data.model.DebugServer
+ import com.redmadrobot.debug.plugin.servers.data.model.DebugServer

- import com.redmadrobot.debug.servers.plugin.ServersPlugin
+ import com.redmadrobot.debug.plugin.servers.ServersPlugin

- import com.redmadrobot.debug_panel_core.internal.DebugPanel
+ import com.redmadrobot.debug.core.DebugPanel
```

### Возможность отображения DebugPanel без использования FragmentManager

Вместо метода `DebugPanel.showPanel(FragmentManager)` следует использовать `DebugPanel.showPanel(Activity)`

### Миграция с VariablePlugin на KonfeaturePlugin

VariablePlugin полностью убран из дебаг панели, тк функционал нового KonfeaturePlugin практически полностью его заменяет и предоставляет новые возможности.
Документацию по настройке KonfeaturePlugin можно посмотреть в [README][readme].

VariablePlugin позволял изменять значения переменных типа Int и Float, которые пока не поддерживаются в KonfeaturePlugin.
Вместо них, необходимо использовать Long и Double соответственно.

Также в VariablePlugin была возможность настроить использование значений переменных любого типа, в KonfeaturePlugin данной возможности нет.
В качестве временного решения можно использовать тип String и на стороне приложение приводить его к нужному типу.

Планируется развивать и улучшать KonfeaturePlugin, так что следите за обновлениями.


[readme]: /README.md
[konfeature]: https://github.com/RedMadRobot/Konfeature
[plugin-development]: plugin_development.md