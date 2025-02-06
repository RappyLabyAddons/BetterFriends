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
import net.labymod.api.util.time.TimeUtil;

public class FriendServerStateListener {

  private final BetterFriendsAddon addon;

  public FriendServerStateListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onServerUpdate(LabyConnectFriendServerEvent event) {
    if (!this.addon.configuration().friendServerSwitchNotifications().get()) {
      return;
    }
    ServerInfo info = event.serverInfo();
    if (info == null || info.getAddress() == null) {
      return;
    }

    Friend friend = event.friend();
    if (friend == null) {
      return;
    }

    Component address = Component.text(info.getAddress())
        .color(NamedTextColor.AQUA)
        .clickEvent(ClickEvent.runCommand("/bf join " + info.getDisplayInfo()))
        .hoverEvent(HoverEvent.showText(Component.translatable(
            "betterfriends.notifications.serverUpdate.hover",
            NamedTextColor.DARK_PURPLE
        )));

    String gameModeName = info.getGameModeName();
    Component text;
    if (gameModeName == null) {
      text = Component.translatable("labymod.activity.labyconnect.notifications.server",
          NamedTextColor.GRAY,
          address
      );
    } else if (TimeUtil.getCurrentTimeMillis() - event.friend().getLastServerChange() < 1000L) {
      text = Component.translatable(
          "labymod.activity.labyconnect.notifications.gameModeAndServer",
          NamedTextColor.GRAY,
          Component.text(gameModeName, NamedTextColor.AQUA),
          address
      );
    } else {
      text = Component.translatable(
          "labymod.activity.labyconnect.notifications.gameMode",
          NamedTextColor.GRAY,
          Component.text(gameModeName, NamedTextColor.AQUA)
      );
    }

    this.addon.displayMessage(
        Component.empty()
            .append(BetterFriendsAddon.prefix)
            .append(NameHelper.getColoredName(friend))
            .append(Component.space())
            .append(text)
            .color(NamedTextColor.GRAY)
    );
  }

}
