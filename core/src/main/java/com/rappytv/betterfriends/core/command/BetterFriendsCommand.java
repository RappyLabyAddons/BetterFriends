package com.rappytv.betterfriends.core.command;

import com.rappytv.betterfriends.api.blocklist.BlockedPlayer;
import com.rappytv.betterfriends.api.blocklist.BlocklistManager;
import com.rappytv.betterfriends.core.BetterFriendsAddon;
import com.rappytv.betterfriends.core.listeners.LabyChatReceiveListener;
import com.rappytv.betterfriends.core.utils.GroupHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.labymod.api.Laby;
import net.labymod.api.client.chat.command.Command;
import net.labymod.api.client.chat.command.SubCommand;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.entity.player.Player;
import net.labymod.api.client.network.server.ServerInfo;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.User;
import net.labymod.api.labyconnect.protocol.model.chat.Chat;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import net.labymod.api.labyconnect.protocol.model.chat.TextChatMessage;
import net.labymod.api.labyconnect.protocol.model.request.IncomingFriendRequest;
import org.jetbrains.annotations.Nullable;

public class BetterFriendsCommand extends Command {

  public BetterFriendsCommand() {
    super("betterfriends", "bf");

    this.translationKey("betterfriends.command");
    this.withSubCommand(new AcceptFriendRequestSubcommand());
    this.withSubCommand(new BlockPlayerSubcommand());
    this.withSubCommand(new UnblockPlayerSubcommand());
    this.withSubCommand(new DeclineFriendRequestSubcommand());
    this.withSubCommand(new JoinServerSubcommand());
    this.withSubCommand(new MessageSubcommand());
    this.withSubCommand(new ReadSubcommand());
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

  private static class AcceptFriendRequestSubcommand extends SubCommand {

    private AcceptFriendRequestSubcommand() {
      super("accept");

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
        request.accept();
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("success.accept"),
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

  private static class BlockPlayerSubcommand extends SubCommand {

    private final BlocklistManager manager = BetterFriendsAddon.references().blocklistManager();

    private BlockPlayerSubcommand() {
      super("block");

      this.translationKey("betterfriends.command.block");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if (arguments.length < 1) {
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
      String username = arguments[0];
      Player player = this.getPlayer(username);
      if (player == null) {
        Laby.labyAPI()
            .labyNetController()
            .loadUniqueIdByName(username, (result) -> this.handlePlayer(
                new BlockedPlayer(result.getNullable(), username)
            ));
        return true;
      }

      this.handlePlayer(new BlockedPlayer(player.getUniqueId(), player.getName()));
      return true;
    }

    @Nullable
    private Player getPlayer(String username) {
      for (Player player : Laby.labyAPI().minecraft().clientWorld().getPlayers()) {
        if (player.getName().equalsIgnoreCase(username)) {
          return player;
        }
      }

      return null;
    }

    private void handlePlayer(@Nullable BlockedPlayer player) {
      if (player == null) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("playerNotFound"),
                    NamedTextColor.RED
                ))
        );
        return;
      }
      if (Laby.labyAPI().getUniqueId().equals(player.uuid())) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("cantBlockSelf"),
                    NamedTextColor.RED
                ))
        );
        return;
      }
      if (this.manager.isBlocked(player.uuid())) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("alreadyBlocked"),
                    NamedTextColor.RED
                ))
        );
        return;
      }
      Laby.labyAPI().minecraft().executeNextTick(() -> this.manager.block(player));
    }
  }

  private static class UnblockPlayerSubcommand extends SubCommand {

    private final BlocklistManager manager = BetterFriendsAddon.references().blocklistManager();

    private UnblockPlayerSubcommand() {
      super("unblock");

      this.translationKey("betterfriends.command.block");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if (arguments.length < 1) {
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
      BlockedPlayer player = this.getPlayer(arguments[0]);
      if (player == null) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("notBlocked"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      Laby.labyAPI().minecraft().executeNextTick(() -> this.manager.unblock(player.uuid()));
      return true;
    }

    @Nullable
    private BlockedPlayer getPlayer(String username) {
      for (BlockedPlayer player : this.manager.getBlockedPlayers()) {
        if (player.username().equalsIgnoreCase(username)) {
          return player;
        }
      }

      return null;
    }
  }

  private static class DeclineFriendRequestSubcommand extends SubCommand {

    private DeclineFriendRequestSubcommand() {
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

  private static class JoinServerSubcommand extends SubCommand {

    private JoinServerSubcommand() {
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

  private static class MessageSubcommand extends SubCommand {

    private MessageSubcommand() {
      super("message", "msg");

      this.translationKey("betterfriends.command.message");
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
      List<Chat> chats = session.getChats();

      for(Chat chat : chats) {
        boolean containsUser = false;
        for(User user : chat.getParticipants()) {
          if(user.getName().equalsIgnoreCase(arguments[0])) containsUser = true;
        }
        if(!containsUser) continue;
        if(arguments.length < 2) {
          this.displayMessage(
              Component.empty()
                  .append(BetterFriendsAddon.getPrefix())
                  .append(Component.translatable(
                      this.getTranslationKey("enterText"),
                      NamedTextColor.RED
                  ))
          );
          return true;
        }
        String message = String.join(
            " ",
            Arrays.copyOfRange(arguments, 1, arguments.length)
        );

        chat.sendMessage(message);
        for(ChatMessage msg : chat.getMessages()) {
          if(!msg.isRead())
            msg.markAsRead();
        }
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

  private static class ReadSubcommand extends SubCommand {

    private ReadSubcommand() {
      super("read");

      this.translationKey("betterfriends.command.read");
    }

    @Override
    public boolean execute(String prefix, String[] arguments) {
      if(Laby.references().labyConnect().getSession() == null) {
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
      if(arguments.length < 1) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("manual"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      UUID uuid;

      try {
        uuid = UUID.fromString(arguments[0]);
      } catch (IllegalArgumentException e) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("manual"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }

      TextChatMessage message = LabyChatReceiveListener.getMessage(uuid);

      if(message == null) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("manual"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      if(message.isRead()) {
        this.displayMessage(
            Component.empty()
                .append(BetterFriendsAddon.getPrefix())
                .append(Component.translatable(
                    this.getTranslationKey("alreadyRead"),
                    NamedTextColor.RED
                ))
        );
        return true;
      }
      message.markAsRead();
      this.displayMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(this.getTranslationKey("success")))
      );
      return true;
    }
  }
}
