package org.summer.story.server

import io.netty.channel.CombinedChannelDuplexHandler
import org.summer.story.config.GlobalConfiguration
import org.summer.story.net.encryption.ClientCyphers

class PacketCodec(
    clientCyphers: ClientCyphers,
    configuration: GlobalConfiguration
) : CombinedChannelDuplexHandler<PacketDecoder, PacketEncoder>(
    PacketDecoder(clientCyphers.getReceiveCypher(), configuration),
    PacketEncoder(clientCyphers.getSendCypher(), configuration)
)