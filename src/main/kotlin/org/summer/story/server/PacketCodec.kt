package org.summer.story.server

import io.netty.channel.CombinedChannelDuplexHandler
import org.summer.story.net.encryption.ClientCyphers

class PacketCodec(clientCyphers: ClientCyphers) : CombinedChannelDuplexHandler<PacketDecoder, PacketEncoder>(
    PacketDecoder(clientCyphers.getReceiveCypher()),
    PacketEncoder(clientCyphers.getSendCypher())
) {
}