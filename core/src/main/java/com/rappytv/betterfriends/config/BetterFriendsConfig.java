package com.rappytv.betterfriends.config;

import com.rappytv.betterfriends.config.subconfig.FriendNoteTagConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class BetterFriendsConfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  private final FriendNoteTagConfig friendNoteTagConfig = new FriendNoteTagConfig();

  @SettingSection("interactions")
  @SwitchSetting
  private final ConfigProperty<Boolean> noteEditorBullet = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> togglePinBullet = new ConfigProperty<>(true);

  @Override
  public ConfigProperty<Boolean> enabled() {
    return this.enabled;
  }

  public FriendNoteTagConfig friendNoteTagConfig() {
    return this.friendNoteTagConfig;
  }

  public ConfigProperty<Boolean> noteEditorBullet() {
    return this.noteEditorBullet;
  }

  public ConfigProperty<Boolean> togglePinBullet() {
    return this.togglePinBullet;
  }
}
