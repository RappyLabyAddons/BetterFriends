package com.rappytv.betterfriends.core.listeners;

import com.google.gson.JsonObject;
import com.rappytv.betterfriends.core.config.BetterFriendsConfig;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.config.ConfigurationVersionUpdateEvent;

public class ConfigVersionListener {

  @Subscribe
  public void onConfigVersionUpdate(ConfigurationVersionUpdateEvent event) {
    if (event.getConfigClass() != BetterFriendsConfig.class || event.getUsedVersion() != 1) {
      return;
    }
    JsonObject config = event.getJsonObject();
    if (!config.has("prefixColor")) {
      return;
    }
    JsonObject prefixCustomizationConfig = new JsonObject();
    prefixCustomizationConfig.add("prefixColor", config.get("prefixColor"));
    config.add("prefixCustomizationConfig", prefixCustomizationConfig);

    event.setJsonObject(config);
  }

}
