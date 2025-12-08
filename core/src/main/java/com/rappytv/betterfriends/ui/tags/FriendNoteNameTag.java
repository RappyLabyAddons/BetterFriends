package com.rappytv.betterfriends.ui.tags;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.ui.snapshot.BetterFriendsFriendSnapshot;
import com.rappytv.betterfriends.ui.snapshot.BetterFriendsKeys;
import java.util.Collections;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.NotNull;

public class FriendNoteNameTag extends ComponentNameTag {

  private final BetterFriendsAddon addon;
  private final PositionType position;

  public FriendNoteNameTag(BetterFriendsAddon addon, PositionType position) {
    this.addon = addon;
    this.position = position;
  }

  @Override
  protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot) {
    if (this.snapshot.isDiscrete()
        || this.snapshot.isInvisible()
        || !this.snapshot.has(BetterFriendsKeys.FRIEND)) {
      return super.buildComponents(snapshot);
    }
    BetterFriendsFriendSnapshot friendSnapshot = snapshot.get(BetterFriendsKeys.FRIEND);

    boolean condition = friendSnapshot.config().enabled().get()
        && friendSnapshot.config().friendNoteTagConfig().enabled().get()
        && friendSnapshot.config().friendNoteTagConfig().position().get() == this.position;

    if (!condition) {
      return super.buildComponents(snapshot);
    }

    Friend friend = friendSnapshot.friend();
    if (friend == null) {
      return super.buildComponents(snapshot);
    }
    String note = friend.getNote();
    if (note != null && !note.isBlank()) {
      return Collections.singletonList(this.addon.getSerializer().deserialize(note));
    }
    String defaultTag = friendSnapshot.config().friendNoteTagConfig().defaultTag().get();
    if (defaultTag.isBlank()) {
      return super.buildComponents(snapshot);
    }
    return Collections.singletonList(this.addon.getSerializer().deserialize(defaultTag));
  }

  @Override
  protected int getBackgroundColor(EntitySnapshot snapshot) {
    BetterFriendsFriendSnapshot friendSnapshot = snapshot.get(BetterFriendsKeys.FRIEND);
    if (friendSnapshot == null) {
      return super.getBackgroundColor(snapshot);
    }
    return friendSnapshot.config().friendNoteTagConfig().hideBackground().get()
        ? 0
        : super.getBackgroundColor(snapshot);
  }

  @Override
  public float getScale() {
    return (float) this.addon.configuration().friendNoteTagConfig().size().get() / 10;
  }
}
