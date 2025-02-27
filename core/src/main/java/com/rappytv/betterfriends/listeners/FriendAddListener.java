package com.rappytv.betterfriends.listeners;

import com.rappytv.betterfriends.BetterFriendsAddon;
import net.labymod.addons.voicechat.api.audio.stream.AudioStreamState;
import net.labymod.addons.voicechat.api.client.VoiceConnector;
import net.labymod.addons.voicechat.core.VoiceChatAddon;
import net.labymod.api.Laby;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendAddEvent;

public class FriendAddListener {

  private final BetterFriendsAddon addon;

  public FriendAddListener(BetterFriendsAddon addon) {
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

  private boolean isSelfMuted() {
    return VoiceChatAddon.INSTANCE
        .referenceStorage()
        .audioStreamRegistry()
        .getState(Laby.labyAPI().getUniqueId(), false) == AudioStreamState.INPUT_GLOBAL_MUTED;
  }

}
