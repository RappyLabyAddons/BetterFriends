package com.rappytv.betterfriends.utils;

import net.labymod.addons.voicechat.api.audio.stream.AudioStreamState;
import net.labymod.addons.voicechat.api.client.VoiceConnector;
import net.labymod.addons.voicechat.api.client.user.VoiceUser;
import net.labymod.addons.voicechat.core.VoiceChatAddon;
import net.labymod.api.Laby;
import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class VoiceChatHelper {

  public boolean isEnabled() {
    return Laby.labyAPI().addonService().isEnabled("voicechat");
  }

  public void reconnect() {
    VoiceConnector client = VoiceChatAddon.INSTANCE.client();
    if (!client.isAuthenticated() || !this.isSelfMuted()) {
      return;
    }

    client.disconnect();
    client.connect();
  }

  public long getMuteExpiration(UUID uuid) {
    VoiceUser user = VoiceChatAddon.INSTANCE
        .referenceStorage()
        .voiceUserRegistry().get(uuid);

    if (user == null) {
      return -1;
    }

    return user.isMuted() ? user.getMute().getTimeEnd() : -1;
  }

  private boolean isSelfMuted() {
    return VoiceChatAddon.INSTANCE
        .referenceStorage()
        .audioStreamRegistry()
        .getClientState(false) == AudioStreamState.INPUT_GLOBAL_MUTED;
  }
}
