package com.rappytv.betterfriends.core.command.subcommand;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.utils.GroupHelper;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.request.IncomingFriendRequest;
import java.util.List;

public class DeclineFriendRequestSubcommand extends SubCommand {

    public DeclineFriendRequestSubcommand() {
      super("decline");

      this.translationKey("betterfriends.command.requests");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if(arguments.length < 1) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("enterName"),
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
      List<IncomingFriendRequest> requests = session.getIncomingRequests();

      for(IncomingFriendRequest request : requests) {
        if(!request.getName().equalsIgnoreCase(arguments[0])) continue;
        request.decline();
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("success.decline"),
                    NamedTextColor.GRAY,
                    GroupHelper.getColoredName(request.getName(), request.gameUser())
                ))
        );
        return true;
      }
      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(
                  this.getTranslationKey("notFound"),
                  NamedTextColor.RED
              ))
      );
      return true;
    }
  }