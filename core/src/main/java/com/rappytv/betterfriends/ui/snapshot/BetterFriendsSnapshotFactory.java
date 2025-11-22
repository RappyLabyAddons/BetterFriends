package com.rappytv.betterfriends.ui.snapshot;

import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.ExtraKey;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class BetterFriendsSnapshotFactory extends
    LabySnapshotFactory<Player, BetterFriendsFriendSnapshot> {

  public BetterFriendsSnapshotFactory(ExtraKey<BetterFriendsFriendSnapshot> extraKey) {
    super(extraKey);
  }

  @Override
  protected BetterFriendsFriendSnapshot create(Player player, Extras extras) {
    return new BetterFriendsFriendSnapshot(player, extras);
  }
}
