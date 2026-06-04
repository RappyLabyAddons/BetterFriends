package com.rappytv.betterfriends.core.ui.snapshot;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.config.subconfig.FriendNoteTagConfig;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.laby3d.renderer.snapshot.AbstractLabySnapshot;
import net.labymod.api.laby3d.renderer.snapshot.Extras;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FriendNoteSnapshot extends AbstractLabySnapshot {

  private final boolean addonEnabled;
  private final boolean tagEnabled;
  private final boolean hideBackground;
  private final boolean friend;
  private final float scale;
  private final PositionType position;
  private final String defaultTag;
  private final String note;

  public FriendNoteSnapshot(Player player, Extras extras, BetterFriendsAddon addon) {
    super(extras);
    FriendNoteTagConfig config = addon.configuration().friendNoteTagConfig();
    Friend friend = BetterFriendsAddon.references().sessionHelper().getFriend(player.getUniqueId());
    this.addonEnabled = addon.configuration().enabled().get();
    this.tagEnabled = config.enabled().get();
    this.hideBackground = config.hideBackground().get();
    this.friend = friend != null;
    this.scale = config.size().get() / 10f;
    this.position = config.position().get();
    this.defaultTag = config.defaultTag().get();
    this.note = this.friend ? friend.getNote() : null;
  }

  public boolean isAddonEnabled() {
    return this.addonEnabled;
  }

  public boolean isTagEnabled() {
    return this.tagEnabled;
  }

  public boolean shouldHideBackground() {
    return this.hideBackground;
  }

  public boolean isFriend() {
    return this.friend;
  }

  public float getScale() {
    return this.scale;
  }

  @NotNull
  public PositionType getPosition() {
    return this.position;
  }

  @NotNull
  public String getDefaultTag() {
    return this.defaultTag;
  }

  @Nullable
  public String getNote() {
    return this.note;
  }
}
