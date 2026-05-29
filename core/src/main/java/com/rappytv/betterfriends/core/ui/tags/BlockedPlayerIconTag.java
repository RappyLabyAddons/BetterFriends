package com.rappytv.betterfriends.core.ui.tags;

import com.rappytv.betterfriends.api.ui.BetterFriendsTextures;
import com.rappytv.betterfriends.core.ui.snapshot.BetterFriendsKeys;
import com.rappytv.betterfriends.core.ui.snapshot.BetterFriendsPlayerSnapshot;
import net.labymod.api.client.entity.player.tag.tags.IconTag;

public class BlockedPlayerIconTag extends IconTag {

  public BlockedPlayerIconTag() {
    super(BetterFriendsTextures.BLOCKED, 8);
  }

  @Override
  public boolean isVisible() {
    if (!this.snapshot.has(BetterFriendsKeys.PLAYER)) {
      return false;
    }
    BetterFriendsPlayerSnapshot playerSnapshot = this.snapshot.get(BetterFriendsKeys.PLAYER);

    return super.isVisible()
        && !this.snapshot.isDiscrete()
        && !this.snapshot.isInvisible()
        && playerSnapshot.isAddonEnabled()
        && playerSnapshot.isBlocked()
        && playerSnapshot.isBlockIconEnabled();
  }
}
