package com.rappytv.betterfriends.api.blocklist.event;

import com.rappytv.betterfriends.api.blocklist.BlockedPlayer;
import net.labymod.api.event.Event;

public record UnblockPlayerEvent(BlockedPlayer player) implements Event {

}
