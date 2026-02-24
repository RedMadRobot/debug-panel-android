package com.redmadrobot.debug.plugin.aboutapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.redmadrobot.debug.core.extension.getPlugin
import com.redmadrobot.debug.core.extension.provideViewModel
import com.redmadrobot.debug.plugin.aboutapp.AboutAppPlugin
import com.redmadrobot.debug.plugin.aboutapp.AboutAppPluginContainer
import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo

@Composable
internal fun AboutAppScreen(
    viewModel: AboutAppViewModel = provideViewModel {
        getPlugin<AboutAppPlugin>()
            .getContainer<AboutAppPluginContainer>()
            .createAboutAppViewModel()
    }
) {
    val state by viewModel.state.collectAsState()

    AboutAppLayout(state = state)
}

@Composable
private fun AboutAppLayout(state: AboutAppViewState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        contentPadding = PaddingValues(all = 16.dp),
    ) {
        items(items = state.appInfoList, key = { it.id }) { item ->
            AboutAppItem(item = item)
        }
    }
}

@Composable
private fun AboutAppItem(item: AboutAppInfo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 56.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.primary,
            )
            Text(
                text = item.value,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val state = remember {
        AboutAppViewState(
            appInfoList = listOf(
                AboutAppInfo(title = "Версия", value = "3,14"),
                AboutAppInfo(
                    title = "Номер билда",
                    value = "fgkdfjgkdfgjdfkgjdfkjgkdfjgkdfjgkdfjgkdjgskdjgkdgfjdsfgjdsfgdsfgjdsfgdskjfgdsjkfgdjfgdsfg"
                ),
                AboutAppInfo(title = "Версия", value = "3,14"),
                AboutAppInfo(title = "Версия", value = "3,14"),
                AboutAppInfo(title = "Версия", value = "3,14"),
                AboutAppInfo(title = "Версия", value = "3,14"),
                AboutAppInfo(title = "Версия", value = "3,14"),
                AboutAppInfo(title = "Версия", value = "3,14"),
                AboutAppInfo(title = "Версия", value = "3,14"),
            )
        )
    }

    MaterialTheme {
        AboutAppLayout(state = state)
    }
}
