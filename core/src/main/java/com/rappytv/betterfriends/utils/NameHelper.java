package com.rappytv.betterfriends.utils;

import net.labymod.api.client.component.Component;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class NameHelper {

  public static Component getColoredName(Friend friend) {
    return Component.text(friend.getName(), friend.gameUser().visibleGroup().getTextColor());
  }
}
