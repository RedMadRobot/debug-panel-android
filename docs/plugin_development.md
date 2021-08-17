# Разработка новых плагинов

**[!]Важно.**  
1. Библиотека находится в статусе разработки и миграции на более актуальные решения. 
Актуальность данного документа стоит уточнить у холдера библиотеки. В данный момент это **r.choryev@redmadrobot.com**
2. В библиотеке млогут быть спорные решения, но она открыта для предложений.


## Общая структура
Debug panel разрабатывается опираясь на подход разработки функционала отдельными плагинами.
В данный момент есть несколько модулей на которых основана работа самой панели и разработка и работа плагинов.

* **debug-panel-core** - Реализация самой панели и базовых классов для поддержки системы плагинов.
* **debug-panel-common** - Модуль содержащий общие библиотеки, классы и ресурсы переиспользуемые в плагинах.
  Библиотеки пробрасываются как сквозные зависимости при помощи типа зависимости `api`.
  Список предоставляемых библиотек можно посмотреть в файле [build.gradle](../debug-panel-common/build.gradle)

## Создание нового плагина
Для добавления нового плагина необходимо сделать несколько шагов:
1. Создать в дирректории **plugins** новый модуль для реализации своего плагина.
   
```
plugins
|   your-plugin
```
2. Объявить новый модуль в файле **settings.gradle** по примеру уже существующих плагинов.
   
```
include ':your-plugin'

project(':your-plugin').projectDir = new File(rootDir, 'plugins/your-plugin')
```
3. Добавить в **build.gradle** файл вашего модуля следующие настройки:

```groovy
android {
    /*.......*/
    
    compileSdkVersion build_versions.compile_sdk

    defaultConfig {
        minSdkVersion build_versions.min_sdk
        targetSdkVersion build_versions.target_sdk

        versionCode getVersionCodeFromProperties()
        versionName getVersionNameFromProperties()
    }

    /*.......*/


    kotlinOptions {
        freeCompilerArgs += "-Xexplicit-api=strict"
    }
}

dependencies {
    implementation(
            project(path: ':debug-panel-core'),
            project(path: ':debug-panel-common'),

            deps.kotlin.stdlib
    )
}

```
**[!]Важно. Конфигурация будет меняться при дальнейшей миграции с Groovy на Kotlin**

4. Создать в своем модуле класс-плагин который и будет отвечать за взаимодействие с DebugPanel. 
   Для этого класс должен унаследоваться от класса `Plugin()` и реализовать необходимые методы. 
   В качестве аргументов класса можно передать необходимые для инициализации плагина данные.\
   **(О методе `getPluginContainer()` и `PluginDependencyContainer()` можно будет почитать ниже)**
   
```kotlin
public class YourPlugin(
    /*some arguments*/
) : Plugin() {

    internal companion object {
        const val NAME = "AWESOME PLUGIN"
    }

    override fun getName(): String = NAME

    /*Plugin dependency container initializing*/
    override fun getPluginContainer(commonContainer: CommonContainer): PluginDependencyContainer {
        return YourPluginContainer(sharedPreferences)
    }

    /*Plugin Fragment initializing*/
    override fun getFragment(): Fragment? {
        return YourPluginFragment()
    }

    /*Plugin Setting Fragment initializing.*/ 
    override fun getSettingFragment(): Fragment { //Нужно только если есть отдельный экран для настройки плагина.
        return YourPluginSettingFragment()
    }
}
```
5. Создать **Fragment** экрана плагина(если он нужен) и унаследовать его от `PluginFragment()`.
К фрагменту создать **ViewModel** и унаследовать от `PluginViewModel()`. 
   В этой связке (Fragment+ViewModel), реализовывать пользовательское взаимодействие пользователя с плагином.
   
## PluginDependencyContainer

В библиотеке не используются библиотеки для реализации DI, т.к.:
1. Не хочется тащить их зависимости в библиотеку.
2. Библиотека не такая большая чтобы реализовывать полноценный DI.

Вместо этого, в библиотеке используется подход с **Service Locator**. 
Для этого необходимо создать свой класс-контейнер, унаследовать его от **PluginDependencyContainer** и внутри него инициировать необходимые зависимости.
Если вам для этого понадобится **Context**, его можно получить из **CommonContainer** который прилетает в качестве аргумента в методе `getPluginContainer()` при инициализации плагина.
Пример реализации можно [посмотреть тут](../plugins/accounts-plugin/src/main/kotlin/com/redmadrobot/account_plugin/plugin/AccountsPluginContainer.kt)

## Работа с классом плагина

Класс-плагин, описание которого было в пункте **4**, является точкой инициализации вашего плагина.
Поэтому доступ к данным и различным экземплярам классов нужно реализовывать через него.
Чтобу получить доступ к самому плагину, нужно использовать метод `getPlugin<YourPlugin>()`.
Например для получения контейнера зависимостей плагина, нужно вызвать:

```kotlin
getPlugin<YourPlugin>()
    .getContainer<YourPluginContainer>()
```

**Пример использования плагина для получения ViewModel во Fragment:**

```kotlin
 private val viewModel by lazy {
        obtainShareViewModel {
            getPlugin<ServersPlugin>()
                .getContainer<ServersPluginContainer>()
                .createServersViewModel()
        }
    }
```

## Области видимости

Все внутренние классы используемые только для работы плагина и не требующиеся для работы клиентского приложения, должны иметь область видимости **inner**

## Тестирование

Для тестирования плагина, необходимо:
1. Подключить его как зависимость в модуль `sample`.

```groovy
 debugImplementation(project(path: ':your-plugin'))
```

2. Инициировать плагин в **App** классе **sample** приложения.

```kotlin
  DebugPanel.initialize(
            application = this,
            plugins = listOf(YourPluggin())
)
```

3. Запустить **sample** проект

## Публикация

Публикация новых плагнинов в основном репозитории должна проходить через создание **Merge Request** в ветку **develop**.

Публикация на внутренний Maven пока делается вручную. 
За публикацией обращаться к r.choryev@redmadrobot.com.
В ближайшее время есть планы пересмотреть этот подход.
