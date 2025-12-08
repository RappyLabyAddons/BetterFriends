package com.rappytv.betterfriends.utils;

import com.rappytv.betterfriends.api.FriendHelper;
import java.util.UUID;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.models.Implements;
import org.jetbrains.annotations.Nullable;

@Singleton
@Implements(FriendHelper.class)
public class DefaultFriendHelper implements FriendHelper {

  @Override
  @Nullable
  public Friend getFriend(UUID uuid) {
    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return null;
    }

    return session.getFriend(uuid);
  }
}
