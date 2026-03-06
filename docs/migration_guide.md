# Миграция

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