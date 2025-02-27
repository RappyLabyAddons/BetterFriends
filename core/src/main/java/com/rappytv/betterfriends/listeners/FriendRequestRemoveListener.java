package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.request.LabyConnectOutgoingFriendRequestRemoveEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.notification.Notification;

public class FriendRequestRemoveListener {

  private final BetterFriendsAddon addon;

  public FriendRequestRemoveListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onFriendRequestRemove(LabyConnectOutgoingFriendRequestRemoveEvent event) {
    if (!this.addon.configuration().friendRequestRemovalNotifications().get()) {
      return;
    }
    LabyConnectSession session = event.labyConnect().getSession();
    if(session == null || !session.isAuthenticated()) {
      return;
    }
    if(session.getFriend(event.request().getUniqueId()) != null) {
      return;
    }

    Notification.builder()
        .title(Component.translatable("betterfriends.notifications.friendRequestRemoval.title"))
        .text(Component.translatable(
            "betterfriends.notifications.friendRequestRemoval.description",
            NameHelper.getColoredName(event.request().getName(), event.request().gameUser())
        ))
        .icon(Icon.head(event.request().getUniqueId(), true))
        .duration(15000)
        .buildAndPush();
  }
}
