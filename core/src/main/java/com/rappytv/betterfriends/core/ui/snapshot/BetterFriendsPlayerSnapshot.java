package com.rappytv.betterfriends.core.ui.snapshot;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.config.subconfig.FriendNoteTagConfig;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.Nullable;

public class BetterFriendsPlayerSnapshot extends AbstractLabySnapshot {

  private final Friend friend;
  private final boolean blocked;
  private final boolean addonEnabled;
  private final boolean pinIconEnabled;
  private final boolean blockIconEnabled;
  private final FriendNoteTagConfig friendNoteTagConfig;

  public BetterFriendsPlayerSnapshot(Player player, Extras extras, BetterFriendsAddon addon) {
    super(extras);
    this.friend = BetterFriendsAddon.references().sessionHelper().getFriend(player.profile());
    this.blocked = BetterFriendsAddon.references()
        .blocklistManager()
        .isBlocked(player.getUniqueId());
    this.addonEnabled = addon.configuration().enabled().get();
    this.pinIconEnabled = addon.configuration().pinIconConfig().pinIcon().get();
    this.blockIconEnabled = addon.configuration().blocklist().showBlockIcon().get();
    this.friendNoteTagConfig = addon.configuration().friendNoteTagConfig();
  }

  @Nullable
  public Friend friend() {
    return this.friend;
  }

  public boolean isBlocked() {
    return this.blocked;
  }

  public boolean isAddonEnabled() {
    return this.addonEnabled;
  }

  public boolean isPinIconEnabled() {
    return this.pinIconEnabled;
  }

  public boolean isBlockIconEnabled() {
    return this.blockIconEnabled;
  }

  public FriendNoteTagConfig getFriendNoteTagConfig() {
    return this.friendNoteTagConfig;
  }
}
