package com.rappytv.betterfriends.core.command.subcommand;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.network.server.ServerInfo;

public class JoinServerSubcommand extends SubCommand {

    public JoinServerSubcommand() {
      super("join");

      this.translationKey("betterfriends.command.join");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if (arguments.length < 1) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("enterAddress"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }

      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(this.getTranslationKey("connecting")))
      );
      Laby.references()
          .serverController()
          .joinServer(ServerInfo.builder().address(arguments[0]).build());
      return true;
    }
  }