package com.rappytv.betterfriends.api;

import java.util.UUID;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.mojang.GameProfile;
import net.labymod.api.reference.annotation.Referenceable;
import org.jetbrains.annotations.Nullable;

@Referenceable
public interface FriendHelper {

  @Nullable
  default Friend getFriend(GameProfile profile) {
    return this.getFriend(profile.getUniqueId());
  }

  @Nullable
  Friend getFriend(UUID uuid);

}
