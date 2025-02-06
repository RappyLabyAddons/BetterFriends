package com.rappytv.betterfriends;

import com.rappytv.betterfriends.config.BetterFriendsConfig;
import com.rappytv.betterfriends.interactions.FriendNoteEditorBullet;
import com.rappytv.betterfriends.interactions.FriendTogglePinBullet;
import com.rappytv.betterfriends.nametags.FriendNoteNameTag;
import net.labymod.api.addon.LabyAddon;
import net.labymod.api.client.entity.player.tag.PositionType;
import net.labymod.api.models.addon.annotation.AddonMain;

@AddonMain
public class BetterFriendsAddon extends LabyAddon<BetterFriendsConfig> {

  @Override
  protected void enable() {
    this.registerSettingCategory();

    this.labyAPI().interactionMenuRegistry().register(new FriendNoteEditorBullet(this));
    this.labyAPI().interactionMenuRegistry().register(new FriendTogglePinBullet(this));

    for (PositionType position : PositionType.values()) {
      this.labyAPI().tagRegistry().register(
          "betterfriends_friendnote",
          position,
          new FriendNoteNameTag(this, position)
      );
    }
  }

  @Override
  protected Class<? extends BetterFriendsConfig> configurationClass() {
    return BetterFriendsConfig.class;
  }
}
