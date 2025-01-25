package com.rappytv.betterfriends.listeners;

import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendRemoveEvent;
import net.labymod.api.notification.Notification;

public class FriendRemoveListener {

  @Subscribe
  public void onFriendRemove(LabyConnectFriendRemoveEvent removeEvent) {
    Laby.labyAPI().notificationController().push(
        Notification.builder()
            .title(Component.text("Friend Removed!"))
            .text(Component.text(removeEvent.friend().getName()))
            .icon(Icon.head(removeEvent.friend().getUniqueId(), true))
            .duration(10000L).build());
  }
}
