package com.rappytv.betterfriends.ui.tags;

import com.rappytv.betterfriends.BetterFriendsAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class FriendPinIconTag extends IconTag {

  private final BetterFriendsAddon addon;

  public FriendPinIconTag(BetterFriendsAddon addon, Icon icon) {
    super(icon, 8);
    this.addon = addon;
  }

  @Override
  public boolean isVisible() {
    if (!this.addon.configuration().enabled().get()
        || !this.addon.configuration().pinIconConfig().pinIcon().get()) {
      return false;
    }
    if (this.entity == null || !(this.entity instanceof Player)) {
      return false;
    }

    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return false;
    }

    Friend friend = session.getFriend(this.entity.getUniqueId());
    return friend != null && friend.isPinned() && super.isVisible();
  }
}
