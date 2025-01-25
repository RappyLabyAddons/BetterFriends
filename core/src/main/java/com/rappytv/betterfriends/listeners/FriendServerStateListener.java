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

public class FriendServerStateListener {

  private final BetterFriendsAddon addon;

  public FriendServerStateListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onServerUpdate(LabyConnectFriendServerEvent serverEvent) {
    if (serverEvent.serverInfo() == null) {
      return;
    }

    if (serverEvent.serverInfo().getAddress() == null) {
      return;
    }

    Friend friend = serverEvent.friend();
    if (friend == null) {
      return;
    }

    String addressName = serverEvent.serverInfo().getAddress();
    Component address = Component.text(addressName)
        .color(NamedTextColor.WHITE)
        .clickEvent(ClickEvent.runCommand("/bf server " + addressName))
        .hoverEvent(HoverEvent.showText(Component.translatable("Klick")))
        .color(NamedTextColor.GRAY);

    this.addon.displayMessage(
        Component.text("§bBFriends §8» §a").append(NameHelper.getColoredName(friend))
            .append(Component.space()).append(Component.text("spielt nun auf ")).append(address));
  }

}
