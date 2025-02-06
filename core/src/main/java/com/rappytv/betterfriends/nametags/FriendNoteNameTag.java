package com.rappytv.betterfriends.nametags;

import com.rappytv.betterfriends.BetterFriendsAddon;
import java.awt.*;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.entity.player.tag.tags.NameTagBackground;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.Nullable;

public class FriendNoteNameTag extends NameTag {

  private static final int gray = new Color(0, 0, 0, 70).getRGB();
  private final BetterFriendsAddon addon;
  private final PositionType position;

  public FriendNoteNameTag(BetterFriendsAddon addon, PositionType position) {
    this.addon = addon;
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
      return RenderableComponent.of(this.addon.getSerializer().deserialize(note));
    } else {
      String defaultTag = this.addon.configuration().friendNoteTagConfig().defaultTag().get();
      if (defaultTag.isBlank()) {
        return null;
      }
      return RenderableComponent.of(this.addon.getSerializer().deserialize(defaultTag));
    }
  }

  @Override
  public float getScale() {
    return (float) this.addon.configuration().friendNoteTagConfig().size().get() / 10;
  }

  @Override
  protected NameTagBackground getCustomBackground() {
    boolean enabled = !this.addon.configuration().friendNoteTagConfig().hideBackground().get();
    NameTagBackground background = super.getCustomBackground();

    if (background == null) {
      background = NameTagBackground.custom(enabled, gray);
    }

    background.setEnabled(enabled);
    return background;
  }

  @Override
  public boolean isVisible() {
    return this.addon.configuration().enabled().get()
        && this.addon.configuration().friendNoteTagConfig().enabled().get()
        && this.addon.configuration().friendNoteTagConfig().position().get() == this.position
        && !this.entity.isCrouching()
        && super.isVisible();
  }
}
