package com.rappytv.betterfriends.nametags;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.serializer.legacy.LegacyComponentSerializer;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.NameTag;
import net.labymod.api.client.render.font.RenderableComponent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import org.jetbrains.annotations.Nullable;

public class FriendNoteNameTag extends NameTag {

  private static final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
  private final RenderableComponent defaultTag = RenderableComponent.of(Component.empty());

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
    if (note == null) {
      return this.defaultTag;
    }

    return RenderableComponent.of(serializer.deserialize(note));
  }

  @Override
  public boolean isVisible() {
    return !this.entity.isCrouching() && super.isVisible();
  }
}
