package com.rappytv.betterfriends.core.utils;

import com.rappytv.betterfriends.api.SessionHelper;
import java.util.UUID;
import javax.inject.Singleton;
import net.labymod.api.Laby;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.models.Implements;
import org.jetbrains.annotations.Nullable;

@Singleton
@Implements(SessionHelper.class)
public class DefaultSessionHelper implements SessionHelper {

  @Override
  public LabyConnectSession getValidSession() {
    LabyConnectSession session = Laby.labyAPI().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      return null;
    }
    return session;
  }

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
