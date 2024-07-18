# Миграция

## Миграция на версию 0.9.0

### Изменения нейминга и структуры пакетов:

```diff
- import com.redmadrobot.account_plugin.plugin.AccountsPlugin
+ import com.redmadrobot.debug.plugin.accounts.AccountsPlugin
```

```diff
- import com.redmadrobot.flipper_plugin.plugin.FlipperPlugin
+ import com.redmadrobot.debug.plugin.flipper.FlipperPlugin
```

```diff
- import com.redmadrobot.debug.appsettings.plugin.AppSettingsPlugin
+ import com.redmadrobot.debug.plugin.appsettings.AppSettingsPlugin
```

```diff
- import com.redmadrobot.debug.servers.data.model.DebugServer
+ import com.redmadrobot.debug.plugin.servers.data.model.DebugServer
```

```diff
- import com.redmadrobot.debug.servers.plugin.ServersPlugin
+ import com.redmadrobot.debug.plugin.servers.ServersPlugin
```

```diff
- import com.redmadrobot.debug.variable.plugin.VariablePlugin
+ import com.redmadrobot.debug.plugin.variable.VariablePlugin
```

```diff
- import com.redmadrobot.debug_panel_core.internal.DebugPanel
+ import com.redmadrobot.debug.core.internal.DebugPanel
```

### Возможность отображения DebugPanel без использования FragmentManager:

> Для оботражения необходимо вызвать метод `showPanel` у объекта `DebugPanel`
>
> ```kotlin
> public fun showPanel(activity: Activity)
> ```
>
> При этом можно использовать старый метод через `FragmentManager`
>
> ```kotlin
> public fun showPanel(fragmentManager: FragmentManager)
> ```
>

### Изменены идентификаторы конфигурации Maven:

`accounts-plugin` -> `plugin-accounts`

`app-settings-plugin` -> `plugin-app-settings`

`flipper-plugin` -> `plugin-flipper`

`servers-plugin` -> `plugin-servers`

`variable-plugin` -> `plugin-variable`