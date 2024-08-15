# Миграция

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

- debugImplementation("com.redmadrobot.debug:variable-plugin:<version>")
+ debugImplementation("com.redmadrobot.debug:plugin-variable:<version>")
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

- import com.redmadrobot.debug.variable.plugin.VariablePlugin
+ import com.redmadrobot.debug.plugin.variable.VariablePlugin

- import com.redmadrobot.debug_panel_core.internal.DebugPanel
+ import com.redmadrobot.debug.core.DebugPanel
```

### Возможность отображения DebugPanel без использования FragmentManager

Вместо метода `DebugPanel.showPanel(FragmentManager)` следует использовать `DebugPanel.showPanel(Activity)`

### Миграция с VariablePlugin на KonfeaturePlugin

TODO: Написать гид миграции