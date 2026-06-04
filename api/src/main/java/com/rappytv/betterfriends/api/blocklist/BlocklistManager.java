package com.rappytv.betterfriends.api.blocklist;

import java.util.List;
import java.util.UUID;
import net.labymod.api.reference.annotation.Referenceable;

@Referenceable
public interface BlocklistManager {

  boolean isBlocked(UUID uuid);

  List<BlockedPlayer> getBlockedPlayers();

  void loadBlocklist(List<BlockedPlayer> players);

  void block(UUID uuid, String username);

  void block(BlockedPlayer player);

  void unblock(UUID uuid);
}
