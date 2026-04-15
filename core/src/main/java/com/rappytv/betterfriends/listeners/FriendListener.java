package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.GroupHelper;
import net.labymod.addons.voicechat.api.audio.stream.AudioStreamState;
import net.labymod.addons.voicechat.api.client.VoiceConnector;
import net.labymod.addons.voicechat.core.VoiceChatAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.event.ClickEvent;
import net.labymod.api.client.component.event.HoverEvent;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendAddEvent;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendRemoveEvent;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendServerEvent;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendStatusEvent;
import net.labymod.api.labyconnect.protocol.model.UserStatus;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.labyconnect.protocol.model.friend.ServerInfo;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.time.TimeUtil;

public class FriendListener {

  private final BetterFriendsAddon addon;

  public FriendListener(BetterFriendsAddon addon) {
    this.addon = addon;
  }

  @Subscribe
  public void onFriendAdd(LabyConnectFriendAddEvent event) {
    if (!this.addon.configuration().restartWhenMuted().get()) {
      return;
    }

    if (!Laby.labyAPI().addonService().isEnabled("voicechat")) {
      return;
    }

    VoiceConnector client = VoiceChatAddon.INSTANCE.client();
    if (!client.isAuthenticated() || !this.isSelfMuted()) {
      return;
    }

    client.disconnect();
    client.connect();
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
            GroupHelper.getColoredName(event.friend())
        ))
        .icon(Icon.head(event.friend().getUniqueId(), true))
        .duration(15000)
        .buildAndPush();
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
            .append(BetterFriendsAddon.getPrefix())
            .append(GroupHelper.getColoredName(friend))
            .append(Component.space())
            .append(text)
            .color(NamedTextColor.GRAY)
    );
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
            .append(BetterFriendsAddon.getPrefix())
            .append(Component.translatable(
                "betterfriends.notifications.statusUpdate.message",
                GroupHelper.getColoredName(event.friend()),
                stateComponent
            ))
            .color(NamedTextColor.GRAY)
    );
  }

  private boolean isSelfMuted() {
    return VoiceChatAddon.INSTANCE
        .referenceStorage()
        .audioStreamRegistry()
        .getState(Laby.labyAPI().getUniqueId(), false) == AudioStreamState.INPUT_GLOBAL_MUTED;
  }

}
