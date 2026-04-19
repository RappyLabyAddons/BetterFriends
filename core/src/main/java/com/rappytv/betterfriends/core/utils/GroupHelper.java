package com.rappytv.betterfriends.core.utils;

import net.labymod.api.client.component.Component;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.user.GameUser;

public class GroupHelper {

  public static Component getColoredName(Friend friend) {
    return getColoredName(friend.getName(), friend.gameUser());
  }

  public static Component getColoredName(String name, GameUser user) {
    return Component.text(name, user.visibleGroup().getTextColor());
  }
}
