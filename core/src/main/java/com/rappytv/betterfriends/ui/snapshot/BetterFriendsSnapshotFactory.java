package com.rappytv.betterfriends.ui.snapshot;

import com.rappytv.betterfriends.BetterFriendsAddon;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class BetterFriendsSnapshotFactory extends
    LabySnapshotFactory<Player, BetterFriendsFriendSnapshot> {

  private final BetterFriendsAddon addon;

  public BetterFriendsSnapshotFactory(BetterFriendsAddon addon) {
    super(BetterFriendsKeys.FRIEND);
    this.addon = addon;
  }

  @Override
  protected BetterFriendsFriendSnapshot create(Player player, Extras extras) {
    return new BetterFriendsFriendSnapshot(player, extras, this.addon);
  }
}
