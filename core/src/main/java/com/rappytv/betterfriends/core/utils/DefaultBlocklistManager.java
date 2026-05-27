package com.rappytv.betterfriends.core.utils;

import com.rappytv.betterfriends.api.blocklist.BlockedPlayer;
import com.rappytv.betterfriends.api.blocklist.BlocklistManager;
import com.rappytv.betterfriends.api.blocklist.event.BlockPlayerEvent;
import com.rappytv.betterfriends.api.blocklist.event.UnblockPlayerEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.models.Implements;

@Singleton
@Implements(BlocklistManager.class)
public class DefaultBlocklistManager implements BlocklistManager {

  private final Map<UUID, BlockedPlayer> blockedPlayers = new HashMap<>();

  @Override
  public boolean isBlocked(UUID uuid) {
    return this.blockedPlayers.containsKey(uuid);
  }

  @Override
  public List<BlockedPlayer> getBlockedPlayers() {
    return List.copyOf(this.blockedPlayers.values());
  }

  @Override
  public void loadBlocklist(List<BlockedPlayer> players) {
    if (!this.blockedPlayers.isEmpty()) {
      throw new IllegalStateException("Blocked players are already initialized");
    }
    for (BlockedPlayer player : players) {
      this.blockedPlayers.put(player.uuid(), player);
    }
  }

  @Override
  public void block(UUID uuid, String username) {
    this.block(new BlockedPlayer(uuid, username));
  }

  @Override
  public void block(BlockedPlayer player) {
    this.blockedPlayers.put(player.uuid(), player);
    Laby.fireEvent(new BlockPlayerEvent(player));
  }

  @Override
  public void unblock(UUID uuid) {
    Laby.fireEvent(new UnblockPlayerEvent(this.blockedPlayers.remove(uuid)));
  }
}
