package com.rappytv.betterfriends.core.ui.tags;

import com.rappytv.betterfriends.core.ui.snapshot.BetterFriendsKeys;
import com.rappytv.betterfriends.core.ui.snapshot.FriendNoteSnapshot;
import java.util.Collections;
import java.util.List;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.ComponentNameTag;
import net.labymod.api.client.render.state.entity.EntitySnapshot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FriendNoteNameTag extends ComponentNameTag {

  private final PositionType position;
  private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

  public FriendNoteNameTag(PositionType position) {
    this.position = position;
  }

  @Override
  protected @NotNull List<Component> buildComponents(EntitySnapshot snapshot) {
    Component note = this.getNote(snapshot);
    if (note == null) {
      return super.buildComponents(snapshot);
    }
    return Collections.singletonList(note);
  }

  @Nullable
  private Component getNote(EntitySnapshot snapshot) {
    if (this.snapshot.isDiscrete()
        || this.snapshot.isInvisible()
        || !this.snapshot.has(BetterFriendsKeys.FRIEND_NOTE)) {
      return null;
    }
    FriendNoteSnapshot noteSnapshot = snapshot.get(BetterFriendsKeys.FRIEND_NOTE);

    boolean condition = noteSnapshot.isAddonEnabled()
        && noteSnapshot.isTagEnabled()
        && noteSnapshot.getPosition() == this.position
        && noteSnapshot.isFriend();

    if (!condition) {
      return null;
    }

    String note = noteSnapshot.getNote();
    String tag = note != null && !note.isBlank() ? note : noteSnapshot.getDefaultTag();
    if (tag.isBlank()) {
      return null;
    }
    return this.serializer.deserialize(tag);
  }

  @Override
  protected int getBackgroundColor(EntitySnapshot snapshot) {
    FriendNoteSnapshot noteSnapshot = snapshot.get(BetterFriendsKeys.FRIEND_NOTE);
    if (noteSnapshot == null) {
      return super.getBackgroundColor(snapshot);
    }
    return noteSnapshot.shouldHideBackground() ? 0 : super.getBackgroundColor(snapshot);
  }

  @Override
  public float getScale() {
    FriendNoteSnapshot noteSnapshot = this.snapshot.get(BetterFriendsKeys.FRIEND_NOTE);
    if (noteSnapshot == null) {
      return super.getScale();
    }
    return noteSnapshot.getScale();
  }
}
