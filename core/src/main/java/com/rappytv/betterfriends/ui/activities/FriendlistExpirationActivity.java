package com.rappytv.betterfriends.ui.activities;

import com.rappytv.betterfriends.BetterFriendsAddon;
import com.rappytv.betterfriends.utils.FriendshipExpirationHandler;
import com.rappytv.betterfriends.utils.FriendshipExpirationHandler.FriendshipExpirationType;
import com.rappytv.betterfriends.utils.VoiceChatHelper;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.component.format.NamedTextColor;
import net.labymod.api.client.gui.icon.Icon;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.ScreenInstance;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.Widget;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.ButtonWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.renderer.IconWidget;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;
import net.labymod.api.util.I18n;

@Link("expiration.lss")
@AutoActivity
public class FriendlistExpirationActivity extends SimpleActivity {

  private final FriendshipExpirationHandler handler;
  private final VoiceChatHelper voiceChatHelper;
  private final Friend friend;
  private TextFieldWidget customExpirationField;

  public FriendlistExpirationActivity(Friend friend) {
    this.handler = BetterFriendsAddon.getInstance().getExpirationHandler();
    this.voiceChatHelper = BetterFriendsAddon.getInstance().getVoiceChatHelper();
    this.friend = friend;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget window = new FlexibleContentWidget()
        .addId("window");

    HorizontalListWidget profileWrapper = new HorizontalListWidget()
        .addId("header");
    IconWidget headIcon = new IconWidget(Icon.head(this.friend.getUniqueId())).addId("head");
    ComponentWidget nameComponent = ComponentWidget.i18n(
        "betterfriends.settings.advancedFriendlist.expiration.title",
        this.friend.getName()
    ).addId("username");
    VerticalListWidget<Widget> content = new VerticalListWidget<>().addId("content");

    profileWrapper.addEntry(headIcon);
    profileWrapper.addEntry(nameComponent);

    DropdownWidget<FriendshipExpirationType> expirationTypeDropdown = new DropdownWidget<>();
    if(Laby.labyAPI().minecraft().isIngame()) {
      expirationTypeDropdown.add(FriendshipExpirationType.ON_SERVER_LEAVE);
    }
    if(this.voiceChatHelper.isEnabled()) {
      long expiration = this.voiceChatHelper.getMuteExpiration(this.friend.getUniqueId());
      if(expiration != -1) {
        expirationTypeDropdown.add(FriendshipExpirationType.ON_MUTE_EXPIRATION);
      }
    }
    expirationTypeDropdown.add(FriendshipExpirationType.CUSTOM_DATE);
    expirationTypeDropdown.setSelected(expirationTypeDropdown.entries().getFirst());
    expirationTypeDropdown.setTranslationKeyPrefix("betterfriends.settings.advancedFriendlist.expiration.types");

    this.customExpirationField = new TextFieldWidget();
    this.customExpirationField.placeholder(Component.text(I18n.translate(
        "betterfriends.settings.advancedFriendlist.expiration.forExample"
    ) + " 6h, 2d, 4w, 1y"));
    this.customExpirationField.setVisible(expirationTypeDropdown.getSelected() == FriendshipExpirationType.CUSTOM_DATE);

    ButtonWidget saveButton = ButtonWidget.i18n(
        "betterfriends.settings.advancedFriendlist.expiration.button", () -> {
      switch (expirationTypeDropdown.getSelected()) {
        case ON_SERVER_LEAVE ->
            this.handler.scheduleServerLeaveExpiration(this.friend.getUniqueId());
        case ON_MUTE_EXPIRATION ->
            this.handler.scheduleVoiceUnmuteExpiration(this.friend.getUniqueId());
        case CUSTOM_DATE -> {
          long duration = getDuration(this.customExpirationField.getText());
          this.handler.scheduleCustomExpiration(
              this.friend.getUniqueId(),
              System.currentTimeMillis() + duration
          );
        }
      }
      Laby.references().chatExecutor().displayClientMessage(
          Component.empty()
              .append(BetterFriendsAddon.getPrefix())
              .append(Component.translatable(
                  "betterfriends.settings.advancedFriendlist.expiration.success",
                  NamedTextColor.GREEN
              ))
      );
      Laby.labyAPI().minecraft().minecraftWindow().displayScreen((ScreenInstance) null);
    });
    Runnable updateButtonVisibility = () -> saveButton.setEnabled(
        expirationTypeDropdown.getSelected() != FriendshipExpirationType.CUSTOM_DATE
            || getDuration(this.customExpirationField.getText()) > 0
    );
    expirationTypeDropdown.setChangeListener((type) -> {
      this.customExpirationField.setVisible(type == FriendshipExpirationType.CUSTOM_DATE);
      updateButtonVisibility.run();
    });
    this.customExpirationField.updateListener((text) -> updateButtonVisibility.run());
    updateButtonVisibility.run();

    window.addContent(profileWrapper);
    window.addContent(content);

    content.addChild(expirationTypeDropdown);
    content.addChild(this.customExpirationField);
    content.addChild(saveButton);

    this.document.addChild(window);
  }

  public static long getDuration(String timeArg) {
    String format;
    long duration;
    try {
      format = timeArg.substring(timeArg.length() - 1);
      duration = Integer.parseInt(timeArg.substring(0, timeArg.length() - 1));
    } catch (NumberFormatException | IndexOutOfBoundsException e) {
      return -1;
    }

    return switch (format) {
      case "s" -> duration * 1000;
      case "m" -> duration * 1000 * 60;
      case "h" -> duration * 1000 * 60 * 60;
      case "d" -> duration * 1000 * 60 * 60 * 24;
      case "w" -> duration * 1000 * 60 * 60 * 24 * 7;
      case "y" -> duration * 1000 * 60 * 60 * 24 * 7 * 52;
      default -> -1;
    };
  }
}
