package com.rappytv.betterfriends.ui.snapshot;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.config.BetterFriendsConfig;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.Nullable;

public class BetterFriendsFriendSnapshot extends AbstractLabySnapshot {

  private final Friend friend;
  private final BetterFriendsConfig config;

  public BetterFriendsFriendSnapshot(Player player, Extras extras) {
    super(extras);
    this.friend = BetterFriendsAddon.references().friendHelper().getFriend(player.profile());
    this.config = BetterFriendsAddon.config();
  }

  @Nullable
  public Friend friend() {
    return this.friend;
  }

  public BetterFriendsConfig config() {
    return this.config;
  }
}
