package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo

class DebugAboutAppInfoProvider {
    companion object {
        fun provideData(): List<AboutAppInfo> {
            return listOf(
                AboutAppInfo(
                    title = "Версия",
                    value = "3.14"
                ),
                AboutAppInfo(
                    title = "Номер билда",
                    value = "101"
                ),
                AboutAppInfo(
                    title = "Номер билда",
                    value = "fgkdfjgkdfgjdfkgjdfkjgkdfjgkdfjgkdfjgkdjgskdjgkdgfjdsfgjdsfgdsfgjdsfgdskjfgdsjkfgdjfgdsfg"
                ),
                AboutAppInfo(
                    title = "Версия",
                    value = "3,145"
                ),
            )
        }
    }
}