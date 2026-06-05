package com.rappytv.betterfriends.core.config.subconfig;

import com.rappytv.betterfriends.api.blocklist.BlockedPlayer;
import com.rappytv.betterfriends.core.ui.activity.config.BlocklistActivity;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.annotation.IntroducedIn;
import net.labymod.api.configuration.loader.annotation.SpriteSlot;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingRequires;
import net.labymod.api.configuration.settings.annotation.SettingSection;
import net.labymod.api.util.MethodOrder;

public class BlocklistConfig extends Config {

  @Exclude
  private final List<BlockedPlayer> blockedPlayers = new ArrayList<>();

  @IntroducedIn(namespace = "betterfriends", value = "1.1.2")
  @SpriteSlot(y = 1)
  @MethodOrder(after = "blockedPlayers")
  @ActivitySetting
  public Activity menu() {
    return new BlocklistActivity();
  }

  @IntroducedIn(namespace = "betterfriends", value = "1.1.2")
  @SpriteSlot(x = 2, y = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> showInteractionBullets = new ConfigProperty<>(true);

  @IntroducedIn(namespace = "betterfriends", value = "1.1.2")
  @SpriteSlot(x = 2, y = 1)
  @SwitchSetting
  private final ConfigProperty<Boolean> showBlockIcon = new ConfigProperty<>(true);

  @SettingSection(value = "behavior", center = true)
  @IntroducedIn(namespace = "betterfriends", value = "1.1.2")
  @SpriteSlot(x = 5, y = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> unfriendBlockedPlayers = new ConfigProperty<>(true);

  @IntroducedIn(namespace = "betterfriends", value = "1.1.2")
  @SpriteSlot(x = 3, y = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> muteInVoiceChat = new ConfigProperty<>(true);

  @IntroducedIn(namespace = "betterfriends", value = "1.1.2")
  @SwitchSetting
  @SpriteSlot(x = 1, y = 1)
  private final ConfigProperty<Boolean> declineFriendRequests = new ConfigProperty<>(true);

  @IntroducedIn(namespace = "betterfriends", value = "1.1.2")
  @SettingRequires("declineFriendRequests")
  @SpriteSlot(x = 4, y = 2)
  @SwitchSetting
  private final ConfigProperty<Boolean> notifyOnFriendRequest = new ConfigProperty<>(false);

  public List<BlockedPlayer> blockedPlayers() {
    return this.blockedPlayers;
  }

  public ConfigProperty<Boolean> showBlockIcon() {
    return this.showBlockIcon;
  }

  public ConfigProperty<Boolean> showInteractionBullets() {
    return this.showInteractionBullets;
  }

  public ConfigProperty<Boolean> unfriendBlockedPlayers() {
    return this.unfriendBlockedPlayers;
  }

  public ConfigProperty<Boolean> muteInVoiceChat() {
    return this.muteInVoiceChat;
  }

  public ConfigProperty<Boolean> declineFriendRequests() {
    return this.declineFriendRequests;
  }

  public ConfigProperty<Boolean> notifyOnFriendRequest() {
    return this.notifyOnFriendRequest;
  }
}
