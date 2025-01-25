package com.rappytv.betterfriends.config;

import com.rappytv.betterfriends.config.subconfig.FriendNoteTagConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;

public class BetterFriendsConfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  private final FriendNoteTagConfig friendNoteTagConfig = new FriendNoteTagConfig();
  @TextFieldSetting
  private final ConfigProperty<String> friendPrefix = new ConfigProperty<>("&aⒻ");

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public FriendNoteTagConfig friendNoteTagConfig() {
    return this.friendNoteTagConfig;
  }

  public ConfigProperty<String> friendPrefix() {
    return this.friendPrefix;
  }
}
