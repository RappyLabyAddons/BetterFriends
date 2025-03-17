package com.rappytv.betterfriends.config.subconfig;

import net.labymod.api.client.gui.screen.activity.Activity;
import net.labymod.api.client.gui.screen.widget.widgets.activity.settings.ActivitySettingWidget.ActivitySetting;
import net.labymod.api.client.gui.screen.widget.widgets.input.SwitchWidget.SwitchSetting;
import net.labymod.api.configuration.loader.Config;
import net.labymod.api.configuration.loader.annotation.Exclude;
import net.labymod.api.configuration.loader.property.ConfigProperty;
import net.labymod.api.util.MethodOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FriendlistExpiryConfig extends Config {

  @Exclude
  private final Map<UUID, Long> expirations = new HashMap<>();

  @SwitchSetting
  private final ConfigProperty<Boolean> sendNotifications = new ConfigProperty<>(true);

  @SwitchSetting
  private final ConfigProperty<Boolean> notifyOnExpiration = new ConfigProperty<>(true);

  @MethodOrder(after = "notifyOnExpiration")
  @ActivitySetting
  public Activity manageExpirationDates() {
    return null;
  }

  public void addExpiration(UUID uuid, long expiration) {
    this.expirations.put(uuid, expiration);
  }

  public void removeExpiration(UUID uuid) {
    this.expirations.remove(uuid);
  }

  public Map<UUID, Long> getExpirations() {
    return this.expirations;
  }

  public ConfigProperty<Boolean> sendNotifications() {
    return this.sendNotifications;
  }
  public ConfigProperty<Boolean> notifyOnExpiration() {
    return this.notifyOnExpiration;
  }
}
