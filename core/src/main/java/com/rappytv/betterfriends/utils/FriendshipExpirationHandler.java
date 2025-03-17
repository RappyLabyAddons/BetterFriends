package com.rappytv.betterfriends.utils;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.config.subconfig.FriendlistExpiryConfig;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.network.server.ServerDisconnectEvent;
import net.labymod.api.event.labymod.labyconnect.LabyConnectStateUpdateEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.LabyConnectState;
import net.labymod.api.notification.Notification;
import net.labymod.api.util.I18n;
import net.labymod.api.util.concurrent.task.Task;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FriendshipExpirationHandler {

  private final Set<UUID> serverLeaveExpirations = new HashSet<>();
  private final BetterFriendsAddon addon;
  private final FriendlistExpiryConfig config;

  public FriendshipExpirationHandler(BetterFriendsAddon addon) {
    this.addon = addon;
    this.config = addon.configuration().expiryConfig();

    Task.builder(this::removeExpiredFriendships).repeat(1, TimeUnit.MINUTES).build().execute();
  }

  public void removeExpiredFriendships() {
    List<UUID> toRemove = new ArrayList<>();
    for (UUID uuid : this.config.getExpirations().keySet()) {
      if(this.config.getExpirations().get(uuid) > System.currentTimeMillis()) {
        continue;
      }
      if(!this.removeFriend(uuid)) {
        continue;
      }
      toRemove.add(uuid);
      if(!this.config.notifyOnExpiration().get()) {
        continue;
      }
      Laby.labyAPI().labyNetController().loadNameByUniqueId(uuid, (name) ->
          Notification.builder()
              .title(Component.translatable("betterfriends.notifications.friendshipExpiration.expired.title"))
              .text(Component.translatable(
                  "betterfriends.notifications.friendshipExpiration.expired.description",
                  Component.text(
                      Objects.requireNonNull(name.getOrDefault(I18n.translate(
                          "betterfriends.notifications.friendshipExpiration.expired.unknown"
                      ))),
                      NamedTextColor.AQUA
                  )
              ))
              .icon(Icon.head(uuid, true))
              .duration(20000)
              .buildAndPush());
    }
    toRemove.forEach(this.config::removeExpiration);
  }

  public void scheduleCustomExpiration(UUID uuid, long expiration) {
    this.config.addExpiration(uuid, expiration);
  }

  public void scheduleVoiceUnmuteExpiration(UUID uuid) {
    long expiration = this.addon.getVoiceChatHelper().getMuteExpiration(uuid);
    if(expiration != -1) {
      this.config.addExpiration(uuid, expiration);
    }
  }

  public void scheduleServerLeaveExpiration(UUID uuid) {
    this.serverLeaveExpirations.add(uuid);
  }

  @Subscribe
  public void onLabyChatConnect(LabyConnectStateUpdateEvent event) {
    if(event.state() == LabyConnectState.PLAY) {
      this.removeExpiredFriendships();
    }
  }

  @Subscribe
  public void onServerLeave(ServerDisconnectEvent event) {
    List<UUID> toRemove = new ArrayList<>();
    for(UUID uuid : this.serverLeaveExpirations) {
      if(!this.removeFriend(uuid)) {
        continue;
      }
      toRemove.add(uuid);
      if(!this.config.notifyOnExpiration().get()) {
        continue;
      }
      Laby.labyAPI().labyNetController().loadNameByUniqueId(uuid, (name) ->
          Notification.builder()
              .title(Component.translatable("betterfriends.notifications.friendshipExpiration.expired.title"))
              .text(Component.translatable(
                  "betterfriends.notifications.friendshipExpiration.expired.disconnectDescription",
                  Component.text(
                      Objects.requireNonNull(name.getOrDefault(I18n.translate(
                          "betterfriends.notifications.friendshipExpiration.expired.unknown"
                      ))),
                      NamedTextColor.AQUA
                  )
              ))
              .icon(Icon.head(uuid, true))
              .duration(20000)
              .buildAndPush());
    }
    toRemove.forEach(this.serverLeaveExpirations::remove);
  }

  @SuppressWarnings("all")
  private boolean removeFriend(UUID uuid) {
    LabyConnectSession session = Laby.labyAPI().labyConnect().getSession();
    if(session == null || !session.isAuthenticated()) {
      return false;
    }
    if(!Laby.labyAPI().labyModLoader().isAddonDevelopmentEnvironment()) {
      session.removeFriend(uuid);
    }
    return true;
  }

  public enum FriendshipExpirationType {
    ON_SERVER_LEAVE,
    ON_MUTE_EXPIRATION,
    CUSTOM_DATE
  }
}
