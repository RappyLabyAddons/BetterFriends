package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.NameHelper;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendServerEvent;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.labyconnect.protocol.model.friend.ServerInfo;

public class FriendServerStateListener {

  private final BetterFriendsAddon addon;

  public FriendServerStateListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onServerUpdate(LabyConnectFriendServerEvent event) {
    ServerInfo info = event.serverInfo();
    if (info == null || info.getAddress() == null) {
      return;
    }

    Friend friend = event.friend();
    if (friend == null) {
      return;
    }

    String addressName = event.serverInfo().getAddress();
    Component address = Component.text(addressName)
        .clickEvent(ClickEvent.runCommand("/bf join " + addressName))
        .hoverEvent(HoverEvent.showText(Component.translatable(
            "betterfriends.notifications.serverUpdate.hover",
            NamedTextColor.DARK_PURPLE
        )))
        .color(NamedTextColor.AQUA);

    this.addon.displayMessage(
        Component.empty()
            .append(BetterFriendsAddon.prefix)
            .append(Component.translatable(
                "betterfriends.notifications.serverUpdate.message",
                NameHelper.getColoredName(friend),
                address
            ))
            .color(NamedTextColor.GRAY)
    );
  }

}
