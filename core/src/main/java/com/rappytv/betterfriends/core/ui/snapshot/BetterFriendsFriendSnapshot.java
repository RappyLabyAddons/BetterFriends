package com.rappytv.betterfriends.core.ui.snapshot;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.config.BetterFriendsConfig;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.Nullable;

public class BetterFriendsFriendSnapshot extends AbstractLabySnapshot {

  private final Friend friend;
  private final BetterFriendsConfig config;

  public BetterFriendsFriendSnapshot(Player player, Extras extras, BetterFriendsAddon addon) {
    super(extras);
    this.friend = BetterFriendsAddon.references().friendHelper().getFriend(player.profile());
    this.config = addon.configuration();
  }

  @Nullable
  public Friend friend() {
    return this.friend;
  }

  public BetterFriendsConfig config() {
    return this.config;
  }
}
