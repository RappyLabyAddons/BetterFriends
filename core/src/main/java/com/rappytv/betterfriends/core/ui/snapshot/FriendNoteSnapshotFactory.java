package com.rappytv.betterfriends.core.ui.snapshot;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.laby3d.renderer.snapshot.LabySnapshotFactory;
import net.labymod.api.service.annotation.AutoService;

@AutoService(LabySnapshotFactory.class)
public class FriendNoteSnapshotFactory extends
    LabySnapshotFactory<Player, FriendNoteSnapshot> {

  private final BetterFriendsAddon addon;

  public FriendNoteSnapshotFactory(BetterFriendsAddon addon) {
    super(BetterFriendsKeys.FRIEND_NOTE);
    this.addon = addon;
  }

  @Override
  protected FriendNoteSnapshot create(Player player, Extras extras) {
    return new FriendNoteSnapshot(player, extras, this.addon);
  }
}
