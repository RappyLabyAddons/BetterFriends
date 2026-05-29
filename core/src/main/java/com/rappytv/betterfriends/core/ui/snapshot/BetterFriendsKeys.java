package com.rappytv.betterfriends.core.ui.snapshot;

import net.labymod.api.laby3d.renderer.snapshot.ExtraKey;

public class BetterFriendsKeys {

  public static final ExtraKey<BetterFriendsPlayerSnapshot> PLAYER = ExtraKey.of(
      "better_friends_player", BetterFriendsPlayerSnapshot.class);
  public static final ExtraKey<FriendNoteSnapshot> FRIEND_NOTE = ExtraKey.of(
      "friend_note", FriendNoteSnapshot.class);
}
