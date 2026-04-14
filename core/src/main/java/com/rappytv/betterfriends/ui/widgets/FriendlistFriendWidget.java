package com.rappytv.betterfriends.ui.widgets;

import java.util.List;
import com.rappytv.betterfriends.ui.activities.FriendlistExpirationActivity;
import net.labymod.api.Laby;
import net.labymod.api.Textures;
import net.labymod.api.Textures.SpriteCommon;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.lss.property.annotation.AutoWidget;
import net.labymod.api.client.gui.screen.key.Key;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.client.input.KeyEvent;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

@AutoWidget
public class FriendlistFriendWidget extends FriendWidget {

  private static final Component X = Component.text("✘", NamedTextColor.RED);
  private boolean skipConfirmation = false;
  private boolean confirmRemoval = false;

  public FriendlistFriendWidget(Friend friend) {
    super(friend);
    Laby.labyAPI().eventBus().registerListener(this);
  }

  @Override
  public List<ButtonWidget> getButtons() {
    if(this.confirmRemoval) {
      ButtonWidget removeConfirmationButton = ButtonWidget.icon(SpriteCommon.GREEN_CHECKED, () -> {
        this.friend.remove();
        this.confirmRemoval = false;
      });
      removeConfirmationButton.setHoverComponent(Component.translatable(
          "betterfriends.settings.advancedFriendlist.removal.confirm",
          NamedTextColor.GREEN
      ));
      ButtonWidget cancelButton = ButtonWidget.component(X, () -> {
        this.confirmRemoval = false;
        this.reInitialize();
      });
      cancelButton.setHoverComponent(Component.translatable(
          "betterfriends.settings.advancedFriendlist.removal.cancel",
          NamedTextColor.RED
      ));
      return List.of(removeConfirmationButton, cancelButton);
    }
    ButtonWidget pinButton = ButtonWidget.icon(Textures.SpriteCommon.PIN, () -> {
      if (this.friend.isPinned()) {
        this.friend.unpin();
      } else {
        this.friend.pin();
      }
    }).addId("pin-button");
    pinButton.setHoverComponent(
        Component.translatable(
            this.friend.isPinned()
                ? "labymod.activity.labyconnect.chat.action.unpin"
                : "labymod.activity.labyconnect.chat.action.pin"
        )
    );
    ButtonWidget noteButton = ButtonWidget.icon(
        SpriteCommon.PAINT,
        this.friend::openNoteEditor
    ).addId("note-button");
    noteButton.setHoverComponent(Component.translatable(
        "labymod.activity.labyconnect.chat.action.note"
    ));
    ButtonWidget expirationButton = ButtonWidget.icon(
        SpriteCommon.QUESTION_MARK, // TODO: Add real icon
        () -> Laby.labyAPI().minecraft().minecraftWindow().displayScreen(
            new FriendlistExpirationActivity(this.friend)
        )
    ).addId("expiration-button");
    expirationButton.setHoverComponent(Component.translatable(
        "betterfriends.settings.advancedFriendlist.expiration.label"
    ));
    ButtonWidget removeButton = ButtonWidget.component(X, () -> {
      if (this.skipConfirmation) {
        this.friend.remove();
      } else {
        this.confirmRemoval = true;
        this.reInitialize();
      }
    }).addId("remove-button");
    removeButton.setHoverComponent(
        Component
            .translatable(
                "betterfriends.settings.advancedFriendlist.removal.label",
                NamedTextColor.RED
            )
            .append(Component.newline())
            .append(Component.translatable(
                "betterfriends.settings.advancedFriendlist.removal.skipConfirmation",
                NamedTextColor.DARK_GRAY
            ))
    );
    return List.of(pinButton, noteButton, expirationButton, removeButton);
  }

  @Subscribe
  public void onKeyDown(KeyEvent event) {
    if (event.key() != Key.L_SHIFT) {
      return;
    }
    if (event.state() == KeyEvent.State.PRESS) {
      this.skipConfirmation = true;
    } else if (event.state() == KeyEvent.State.UNPRESSED) {
      this.skipConfirmation = false;
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    Laby.labyAPI().eventBus().unregisterListener(this);
  }
}
