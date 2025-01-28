package com.rappytv.betterfriends.config;

import com.rappytv.betterfriends.config.subconfig.FriendNoteTagConfig;
import net.labymod.api.addon.AddonConfig;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget.TextFieldSetting;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.configuration.settings.annotation.SettingSection;

public class BetterFriendsConfig extends AddonConfig {

  @SwitchSetting
  private final ConfigProperty<Boolean> enabled = new ConfigProperty<>(true);
  private final FriendNoteTagConfig friendNoteTagConfig = new FriendNoteTagConfig();
  @TextFieldSetting
  private final ConfigProperty<String> friendPrefix = new ConfigProperty<>("&aⒻ");

  @SettingSection("notifications")
  @SwitchSetting
  private final ConfigProperty<Boolean> friendRequestNotifications = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> friendServerSwitchNotifications = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> friendStatusUpdateNotifications = new ConfigProperty<>(true);
  @SwitchSetting
  private final ConfigProperty<Boolean> friendRemovalNotifications = new ConfigProperty<>(true);

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

  public ConfigProperty<Boolean> friendRequestNotifications() {
    return this.friendRequestNotifications;
  }

  public ConfigProperty<Boolean> friendServerSwitchNotifications() {
    return this.friendServerSwitchNotifications;
  }

  public ConfigProperty<Boolean> friendStatusUpdateNotifications() {
    return this.friendStatusUpdateNotifications;
  }

  public ConfigProperty<Boolean> friendRemovalNotifications() {
    return this.friendRemovalNotifications;
  }
}
