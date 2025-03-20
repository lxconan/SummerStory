package org.summer.story.server

import java.util.concurrent.atomic.AtomicReference

class GlobalState {
    private val state: AtomicReference<MapleServerState> = AtomicReference(MapleServerState.STOPPED)

    var serverState: MapleServerState
        get() = state.get()
        set(value) = state.set(value)
}