package org.summer.story.server

import java.util.concurrent.atomic.AtomicReference

class GlobalState {
    private val state: AtomicReference<MapleServerState> = AtomicReference(MapleServerState.SHUTDOWN)

    var serverState: MapleServerState
        get() = state.get()
        set(value) = state.set(value)
}