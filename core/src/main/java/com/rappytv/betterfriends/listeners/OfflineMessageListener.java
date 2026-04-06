package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.config.BetterFriendsConfig;
import java.util.UUID;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.LabyConnectEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatMessageEvent;
import net.labymod.api.event.labymod.labyconnect.session.chat.LabyConnectChatMessageReadEvent;
import net.labymod.api.labyconnect.LabyConnect;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class OfflineMessageListener {

  private final BetterFriendsConfig config;

  public OfflineMessageListener(BetterFriendsAddon addon) {
    this.config = addon.configuration();
  }

  @Subscribe
  public void onLabyConnect(LabyConnectEvent connectEvent) {
    LabyConnect connect = connectEvent.labyConnect();
    if (!connect.isAuthenticated()) {
      return;
    }

    LabyConnectSession session = connect.getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    for (UUID uuids : this.config.temporaryPined()) {
      Friend friend = session.getFriend(uuids);
      if (friend == null || !friend.isPinned()) {
        continue;
      }

      friend.unpin();
    }
    this.config.temporaryPined().clear();

    for (Friend friend : session.getFriends()) {
      for (ChatMessage message : friend.chat().getMessages()) {
        if (message.isRead() || friend.isPinned()) {
          continue;
        }

        this.config.temporaryPined().add(friend.getUniqueId());
        friend.pin();
      }
    }
  }

  @Subscribe
  public void onLabyConnectChatMessage(LabyConnectChatMessageEvent chatMessageEvent) {
    LabyConnectSession session = chatMessageEvent.labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    UUID uniqueId = chatMessageEvent.message().sender().getUniqueId();
    Friend friend = session.getFriend(uniqueId);
    if (friend == null || friend.isPinned()) {
      return;
    }

    this.config.temporaryPined().add(uniqueId);
    friend.pin();
  }

  @Subscribe
  public void onLabyConnectChatMessageRead(LabyConnectChatMessageReadEvent readEvent) {
    if (this.config.temporaryPined().isEmpty()) {
      return;
    }

    UUID uniqueId = readEvent.message().sender().getUniqueId();
    if (!this.config.temporaryPined().contains(uniqueId)) {
      return;
    }

    LabyConnectSession session = readEvent.labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return;
    }

    Friend friend = session.getFriend(uniqueId);
    if (friend == null || !friend.isPinned()) {
      return;
    }

    this.config.temporaryPined().remove(uniqueId);
    friend.unpin();
  }
}
