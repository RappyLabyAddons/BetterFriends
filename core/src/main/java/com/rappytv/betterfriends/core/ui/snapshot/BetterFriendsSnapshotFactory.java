package com.rappytv.betterfriends.core.ui.snapshot;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class BetterFriendsSnapshotFactory extends
    LabySnapshotFactory<Player, BetterFriendsPlayerSnapshot> {

  private final BetterFriendsAddon addon;

  public BetterFriendsSnapshotFactory(BetterFriendsAddon addon) {
    super(BetterFriendsKeys.PLAYER);
    this.addon = addon;
  }

  @Override
  protected BetterFriendsPlayerSnapshot create(Player player, Extras extras) {
    return new BetterFriendsPlayerSnapshot(player, extras, this.addon);
  }
}
