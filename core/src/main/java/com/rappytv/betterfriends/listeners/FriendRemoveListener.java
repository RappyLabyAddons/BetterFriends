package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendRemoveEvent;
import net.labymod.api.notification.Notification;

public class FriendRemoveListener {

  private final BetterFriendsAddon addon;

  public FriendRemoveListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onFriendRemove(LabyConnectFriendRemoveEvent event) {
    if (!this.addon.configuration().friendRemovalNotifications().get()) {
      return;
    }
    Notification.builder()
        .title(Component.translatable("betterfriends.notifications.friendRemoval.title"))
        .text(Component.translatable(
            "betterfriends.notifications.friendRemoval.description",
            NameHelper.getColoredName(event.friend())
        ))
        .icon(Icon.head(event.friend().getUniqueId(), true))
        .duration(15000)
        .buildAndPush();
  }
}
