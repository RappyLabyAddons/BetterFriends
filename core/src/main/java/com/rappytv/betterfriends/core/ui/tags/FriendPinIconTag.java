package com.rappytv.betterfriends.core.ui.tags;

import com.rappytv.betterfriends.core.ui.snapshot.BetterFriendsFriendSnapshot;
import com.rappytv.betterfriends.core.ui.snapshot.BetterFriendsKeys;
import net.labymod.api.Textures;
import net.labymod.api.client.entity.player.tag.tags.IconTag;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

public class FriendPinIconTag extends IconTag {

  public FriendPinIconTag() {
    super(Textures.SpriteCommon.PIN, 8);
  }

  @Override
  public boolean isVisible() {
    if (!this.snapshot.has(BetterFriendsKeys.FRIEND)) {
      return false;
    }
    BetterFriendsFriendSnapshot friendSnapshot = this.snapshot.get(BetterFriendsKeys.FRIEND);

    Friend friend = friendSnapshot.friend();
    return super.isVisible()
        && !this.snapshot.isDiscrete()
        && !this.snapshot.isInvisible()
        && friendSnapshot.config().enabled().get()
        && friendSnapshot.config().pinIconConfig().pinIcon().get()
        && friend != null
        && friend.isPinned();
  }
}
