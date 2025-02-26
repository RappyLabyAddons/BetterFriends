package com.rappytv.betterfriends.ui.badge;

import com.rappytv.betterfriends.BetterFriendsAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.badge.renderer.BadgeRenderer;
import net.labymod.api.client.network.NetworkPlayerInfo;
import net.labymod.api.client.render.matrix.Stack;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class FriendPinBadge extends BadgeRenderer {

  private final BetterFriendsAddon addon;

  public FriendPinBadge(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Override
  public void render(Stack stack, float x, float y, NetworkPlayerInfo player) {
    this.addon.configuration()
        .pinIconConfig()
        .texture().get()
        .getIcon()
        .render(
            stack,
            x + 2.0F,
            y + 0.5F,
            this.addon.configuration().pinIconConfig().texture().get().getBadgeSize()
        );
  }

  @Override
  protected boolean isVisible(NetworkPlayerInfo player) {
    if (!this.addon.configuration().enabled().get()
        || !this.addon.configuration().pinIconConfig().pinBadge().get()) {
      return false;
    }

    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return false;
    }

    Friend friend = session.getFriend(player.profile().getUniqueId());
    return friend != null && friend.isPinned();
  }
}
