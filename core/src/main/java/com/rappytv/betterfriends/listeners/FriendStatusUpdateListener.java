package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.client.component.Component;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendStatusEvent;
import net.labymod.api.labyconnect.protocol.model.UserStatus;

public class FriendStatusUpdateListener {

  private final BetterFriendsAddon addon;

  public FriendStatusUpdateListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onStatusUpdate(LabyConnectFriendStatusEvent statusEvent) {
    Component friendName = NameHelper.getColoredName(statusEvent.friend());
    UserStatus state = statusEvent.getStatus();

    if (statusEvent.isOnline() && statusEvent.getPreviousStatus().equals(UserStatus.OFFLINE)) {
      this.addon.displayMessage(
          Component.empty()
              .append(Component.text("§bBFriends §8» §r"))
              .append(friendName)
              .append(Component.translatable(" ist nun "))
              .append(state.component())
              .append(Component.text("!")));
    } else if (statusEvent.wasOnline() && state.equals(UserStatus.OFFLINE)) {
      this.addon.displayMessage(
          Component.empty()
              .append(Component.text("§bBFriends §8» §r"))
              .append(friendName)
              .append(Component.translatable(" ist nun "))
              .append(Component.text("§4offline"))
              .append(Component.text("!")));
    } else {
      this.addon.displayMessage(
          Component.empty()
              .append(Component.text("§bBFriends §8» §r"))
              .append(friendName)
              .append(Component.translatable(" ist nun "))
              .append(state.component())
              .append(Component.text("!")));
    }
  }
}
