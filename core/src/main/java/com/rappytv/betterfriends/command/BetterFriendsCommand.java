package com.rappytv.betterfriends.command;

import com.rappytv.betterfriends.BetterFriendsAddon;
import java.util.ArrayList;
import java.util.List;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.network.server.ServerInfo;

public class BetterFriendsCommand extends Command {

  public BetterFriendsCommand() {
    super("betterfriends", "bf");

    this.translationKey("betterfriends.command");
    this.withSubCommand(new JoinServerSubcommand());
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    List<String> subCommands = new ArrayList<>();
    for (SubCommand subCommand : this.getSubCommands()) {
      subCommands.add(subCommand.getPrefix());
    }

    this.displayMessage(
        Component.empty()
            .append(BetterFriendsAddon.prefix)
            .append(Component.translatable(
                this.getTranslationKey("usage"),
                NamedTextColor.RED,
                Component.text(
                    "/" + prefix + " <" + String.join("/", subCommands) + ">",
                    NamedTextColor.AQUA
                )
            ))
    );
    return true;
  }

  public static class JoinServerSubcommand extends SubCommand {

    protected JoinServerSubcommand() {
      super("join");

      this.translationKey("betterfriends.command.join");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if (arguments.length < 1) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.prefix)
                .append(Component.translatable(
                    this.getTranslationKey("enterAddress"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }

      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.prefix)
              .append(Component.translatable(this.getTranslationKey("connecting")))
      );
      Laby.references()
          .serverController()
          .joinServer(ServerInfo.builder().address(arguments[0]).build());
      return true;
    }
  }
}
