package com.rappytv.betterfriends.core.listeners;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.config.BetterFriendsConfig;
import java.util.UUID;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.LabyConnectEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatMessageEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatMessageReadEvent;
import net.labymod.api.labyconnect.LabyConnect;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class TemporaryPinListener {

  private final BetterFriendsConfig config;

  public TemporaryPinListener(BetterFriendsAddon addon) {
    this.config = addon.configuration();
  }

  @Subscribe
  public void onLabyConnect(LabyConnectEvent event) {
    LabyConnect connect = event.labyConnect();
    if (!connect.isAuthenticated()) {
      return;
    }

    LabyConnectSession session = connect.getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    for (UUID uuid : this.config.temporaryPins()) {
      Friend friend = session.getFriend(uuid);
      if (friend == null || !friend.isPinned()) {
        continue;
      }

      friend.unpin();
    }
    this.config.temporaryPins().clear();

    if (!this.config.temporaryPinsEnabled().get()) {
      return;
    }
    for (Friend friend : session.getFriends()) {
      for (ChatMessage message : friend.chat().getMessages()) {
        if (message.isRead() || friend.isPinned()) {
          continue;
        }

        this.config.temporaryPins().add(friend.getUniqueId());
        friend.pin();
        break;
      }
    }
  }

  @Subscribe
  public void onLabyConnectChatMessage(LabyConnectChatMessageEvent event) {
    if (!this.config.temporaryPinsEnabled().get()) {
      return;
    }
    LabyConnectSession session = event.labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    UUID uniqueId = event.message().sender().getUniqueId();
    Friend friend = session.getFriend(uniqueId);
    if (friend == null || friend.isPinned()) {
      return;
    }

    this.config.temporaryPins().add(uniqueId);
    friend.pin();
  }

  @Subscribe
  public void onLabyConnectChatMessageRead(LabyConnectChatMessageReadEvent event) {
    if (this.config.temporaryPins().isEmpty()) {
      return;
    }

    UUID uniqueId = event.message().sender().getUniqueId();
    if (!this.config.temporaryPins().contains(uniqueId)) {
      return;
    }

    LabyConnectSession session = event.labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    Friend friend = session.getFriend(uniqueId);
    if (friend == null || !friend.isPinned()) {
      return;
    }

    this.config.temporaryPins().remove(uniqueId);
    friend.unpin();
  }
}
