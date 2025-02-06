package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendStatusEvent;
import net.labymod.api.labyconnect.protocol.model.UserStatus;

public class FriendStatusUpdateListener {

  private final BetterFriendsAddon addon;

  public FriendStatusUpdateListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onStatusUpdate(LabyConnectFriendStatusEvent event) {
    if (!this.addon.configuration().friendStatusUpdateNotifications().get()) {
      return;
    }
    UserStatus state = event.getStatus();
    Component stateComponent = Component.translatable(
        state.getLocalTranslationKey(),
        state.textColor()
    );

    if (state == UserStatus.OFFLINE) {
      stateComponent = Component.translatable(
          "betterfriends.notifications.statusUpdate.offline",
          NamedTextColor.DARK_GRAY
      );
    }

    this.addon.displayMessage(
        Component.empty()
            .append(BetterFriendsAddon.prefix)
            .append(Component.translatable(
                "betterfriends.notifications.statusUpdate.message",
                NameHelper.getColoredName(event.friend()),
                stateComponent
            ))
            .color(NamedTextColor.GRAY)
    );
  }
}
