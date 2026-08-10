package com.redmadrobot.debug.plugin.aboutapp.model

import android.content.Context
import com.redmadrobot.debug.core.DebugEvent
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
        public val debugEvent: DebugEvent,
        override val id: String = UUID.randomUUID().toString(),
    ) : AboutAppAction
}
