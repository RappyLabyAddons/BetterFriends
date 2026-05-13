package com.rappytv.betterfriends.core.command.subcommand;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.request.OutgoingFriendRequest;

public class SendFriendRequestSubcommand extends SubCommand {

  public SendFriendRequestSubcommand() {
    super("send", "add");
    this.translationKey("betterfriends.command.sendRequest");
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    if (arguments.length < 1) {
      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(
                  this.getTranslationKey("enterUsername"),
                  NamedTextColor.RED
              ))
      );
      return true;
    }

    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if(session == null) {
      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(
                  "betterfriends.errors.notConnected",
                  NamedTextColor.RED
              ))
      );
      return true;
    }

    for (OutgoingFriendRequest outgoingRequest : session.getOutgoingRequests()) {
      if(!outgoingRequest.getName().equalsIgnoreCase(arguments[0])) continue;

      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(
                  this.getTranslationKey("alreadyRequested"),
                  NamedTextColor.RED,
                  Component.text(arguments[0])
              ))
      );
      return true;
    }

    this.displayMessage(
        Component.empty()
            .append(BetterFriendsAddon.getPrefix())
            .append(Component.translatable(this.getTranslationKey("requested"), NamedTextColor.GREEN))
    );

    session.sendFriendRequest(arguments[0]);
    return true;
  }
}
