package com.rappytv.betterfriends.config;

import com.rappytv.betterfriends.config.subconfig.FriendNoteTagConfig;
import com.rappytv.betterfriends.ui.activities.config.AdvancedFriendListActivity;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;

public class BetterFriendsConfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  private final FriendNoteTagConfig friendNoteTagConfig = new FriendNoteTagConfig();

  @MethodOrder(after = "friendNoteTagConfig")
  @ActivitySetting
  public Activity advancedFriendlist() {
    return new AdvancedFriendListActivity();
  }

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public FriendNoteTagConfig friendNoteTagConfig() {
    return this.friendNoteTagConfig;
  }
}
