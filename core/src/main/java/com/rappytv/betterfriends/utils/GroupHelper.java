package com.rappytv.betterfriends.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Constants.Urls;
import net.labymod.api.client.component.Component;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.user.GameUser;
import net.labymod.api.user.group.Group;
import net.labymod.api.util.GsonUtil;
import net.labymod.api.util.io.web.request.Request;

public class GroupHelper {

  private static final List<Integer> groupIds = new ArrayList<>();

  public static void registerGroupIds() {
    Request.ofGson(JsonElement.class)
        .url(Urls.LABYNET_GROUPS)
        .execute(response -> {
          if (response.hasException()) {
            return;
          }
          JsonElement element = response.get();
          if (!element.isJsonObject()) {
            return;
          }
          JsonObject groupContainer = element.getAsJsonObject();

          for(JsonElement groupElement : groupContainer.getAsJsonArray("groups")) {
            Group group = GsonUtil.DEFAULT_GSON.fromJson(groupElement, Group.class);
            group.initialize();
            groupIds.add(group.getIdentifier());
          }
        });
  }

  public static int getGroupIndex(int identifier) {
    int index = groupIds.indexOf(identifier);
    return index != -1 ? index : groupIds.size();
  }

  public static Component getColoredName(Friend friend) {
    return getColoredName(friend.getName(), friend.gameUser());
  }

  public static Component getColoredName(String name, GameUser user) {
    return Component.text(name, user.visibleGroup().getTextColor());
  }
}
