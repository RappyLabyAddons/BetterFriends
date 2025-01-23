package com.rappytv.betterfriends;

import com.rappytv.betterfriends.nametags.FriendNoteNameTag;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class BetterFriendsAddon extends LabyAddon<BetterFriendsConfig> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.labyAPI().tagRegistry().register(
        "betterfriends_friendnote",
        PositionType.ABOVE_NAME,
        new FriendNoteNameTag()
    );
  }

  @Override
  protected Class<? extends BetterFriendsConfig> configurationClass() {
    return BetterFriendsConfig.class;
  }
}
