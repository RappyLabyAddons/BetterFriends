package com.rappytv.betterfriends.core.command;

import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.command.subcommand.AcceptFriendRequestSubcommand;
import com.rappytv.betterfriends.core.command.subcommand.DeclineFriendRequestSubcommand;
import com.rappytv.betterfriends.core.command.subcommand.JoinServerSubcommand;
import com.rappytv.betterfriends.core.command.subcommand.MessageSubcommand;
import com.rappytv.betterfriends.core.command.subcommand.ReadSubcommand;
import java.util.ArrayList;
import java.util.List;
import com.rappytv.betterfriends.core.command.subcommand.SendFriendRequestSubcommand;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;

public class BetterFriendsCommand extends Command {

  public BetterFriendsCommand() {
    super("betterfriends", "bf");

    this.translationKey("betterfriends.command");
    this.withSubCommand(new AcceptFriendRequestSubcommand());
    this.withSubCommand(new DeclineFriendRequestSubcommand());
    this.withSubCommand(new JoinServerSubcommand());
    this.withSubCommand(new MessageSubcommand());
    this.withSubCommand(new ReadSubcommand());
    this.withSubCommand(new SendFriendRequestSubcommand());
  }

  @Override
  public boolean execute(String prefix, String[] arguments) {
    List<String> subCommands = new ArrayList<>();
    for (SubCommand subCommand : this.getSubCommands()) {
      subCommands.add(subCommand.getPrefix());
    }

    this.displayMessage(
        Component.empty()
            .append(BetterFriendsAddon.getPrefix())
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
}
