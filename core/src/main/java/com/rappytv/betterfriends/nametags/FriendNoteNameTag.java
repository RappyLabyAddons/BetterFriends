package com.rappytv.betterfriends.nametags;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.config.BetterFriendsConfig;
import java.awt.*;
import net.labymod.api.Laby;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.entity.player.tag.tags.NameTagBackground;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.Nullable;

public class FriendNoteNameTag extends NameTag {

  private static final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
  private static final int gray = new Color(0, 0, 0, 70).getRGB();
  private final BetterFriendsConfig config;
  private final PositionType position;

  public FriendNoteNameTag(BetterFriendsAddon addon, PositionType position) {
    this.config = addon.configuration();
    this.position = position;
  }

  @Override
  protected @Nullable RenderableComponent getRenderableComponent() {
    if (this.entity == null || !(this.entity instanceof Player)) {
      return null;
    }

    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return null;
    }

    Friend friend = session.getFriend(this.entity.getUniqueId());
    if (friend == null) {
      return null;
    }

    String note = friend.getNote();
    if (note != null && !note.isBlank()) {
      return RenderableComponent.of(serializer.deserialize(note));
    } else {
      String defaultTag = this.config.friendNoteTagConfig().defaultTag().get();
      if (defaultTag.isBlank()) {
        return null;
      }
      return RenderableComponent.of(serializer.deserialize(defaultTag));
    }
  }

  @Override
  public float getScale() {
    return (float) this.config.friendNoteTagConfig().size().get() / 10;
  }

  @Override
  protected NameTagBackground getCustomBackground() {
    boolean enabled = !this.config.friendNoteTagConfig().hideBackground().get();
    NameTagBackground background = super.getCustomBackground();

    if (background == null) {
      background = NameTagBackground.custom(enabled, gray);
    }

    background.setEnabled(enabled);
    return background;
  }

  @Override
  public boolean isVisible() {
    return this.config.enabled().get()
        && this.config.friendNoteTagConfig().enabled().get()
        && this.config.friendNoteTagConfig().position().get() == this.position
        && !this.entity.isCrouching()
        && super.isVisible();
  }
}
