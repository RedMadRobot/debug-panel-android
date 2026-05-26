package com.redmadrobot.debug.plugin.aboutapp

import android.content.Context
import java.util.UUID

/**
 * No-op declaration of [AboutAppAction] for release builds.
 */
@Suppress("UnusedPrivateProperty")
public sealed interface AboutAppAction {
    public val id: String
    public val title: String

    public class Direct(
        override val title: String,
        public val onClick: (Context) -> Unit,
        override val id: String = UUID.randomUUID().toString(),
    ) : AboutAppAction

    public class Event(
        override val title: String,
        override val id: String = UUID.randomUUID().toString(),
    ) : AboutAppAction
}
