package com.redmadrobot.debug_sample.debug_data

import com.redmadrobot.debug.plugin.aboutapp.model.AboutAppInfo

class DebugAboutAppInfoProvider {
    companion object {
        fun provideData(): List<AboutAppInfo> {
            return listOf(
                AboutAppInfo(
                    title = "Версия",
                    content = "3.14"
                ),
                AboutAppInfo(
                    title = "Номер билда",
                    content = "101"
                ),
                AboutAppInfo(
                    title = "Номер билда1",
                    content = "fgkdfjgkdfgjdfkgjdfkjgkdfjgkdfjgkdfjgkdjgskdjgkdgfjdsfgjdsfgdsfgjdsfgdskjfgdsjkfgdjfgdsfg"
                ),
                AboutAppInfo(
                    title = "Версия2",
                    content = "3,14"
                ),
            )
        }
    }
}