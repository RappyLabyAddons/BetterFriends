package com.rappytv.betterfriends.ui.activities.config;

import com.rappytv.betterfriends.ui.widgets.FriendWidget;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import com.rappytv.betterfriends.utils.GroupHelper;
import net.labymod.api.Laby;
import net.labymod.api.client.component.Component;
import net.labymod.api.client.gui.screen.Parent;
import net.labymod.api.client.gui.screen.activity.AutoActivity;
import net.labymod.api.client.gui.screen.activity.Link;
import net.labymod.api.client.gui.screen.activity.types.SimpleActivity;
import net.labymod.api.client.gui.screen.widget.widgets.ComponentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.TextFieldWidget;
import net.labymod.api.client.gui.screen.widget.widgets.input.dropdown.DropdownWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.FlexibleContentWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.ScrollWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.HorizontalListWidget;
import net.labymod.api.client.gui.screen.widget.widgets.layout.list.VerticalListWidget;
import net.labymod.api.event.Phase;
import net.labymod.api.event.Subscribe;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendAddEvent;
import net.labymod.api.event.labymod.labyconnect.session.friend.LabyConnectFriendRemoveEvent;
import net.labymod.api.event.labymod.labyconnect.session.login.LabyConnectFriendAddBulkEvent;
import net.labymod.api.event.labymod.user.UserUpdateDataEvent;
import net.labymod.api.labyconnect.LabyConnectSession;
import net.labymod.api.labyconnect.protocol.model.User;
import net.labymod.api.labyconnect.protocol.model.chat.ChatMessage;
import net.labymod.api.labyconnect.protocol.model.friend.Friend;

@Link("friend_list.lss")
@AutoActivity
public class FriendListActivity<T extends FriendWidget> extends SimpleActivity {

  private final Function<Friend, T> friendWidgetConstructor;
  private final VerticalListWidget<T> entries = new VerticalListWidget<>()
      .addId("friends");
  private final ComponentWidget error = ComponentWidget.empty().addId("error");
  private ScrollWidget scroll;
  private String filterQuery = "";
  private SortingStrategy sortingStrategy = SortingStrategy.ONLINE_STATUS;

  public FriendListActivity(Function<Friend, T> friendWidgetConstructor) {
    this.friendWidgetConstructor = friendWidgetConstructor;
  }

  @Override
  public void initialize(Parent parent) {
    super.initialize(parent);

    FlexibleContentWidget container = new FlexibleContentWidget().addId("container");

    HorizontalListWidget filterSettings = new HorizontalListWidget()
        .addId("filter-settings");

    TextFieldWidget searchField = new TextFieldWidget();
    searchField.addId("search-field");
    searchField.placeholder(Component.translatable("labymod.ui.textfield.search"));
    searchField.setText(this.filterQuery);
    searchField.updateListener(this::applyFriendsFilter);

    DropdownWidget<SortingStrategy> sortingDropdown = new DropdownWidget<>();
    for (SortingStrategy sortingStrategy : SortingStrategy.values()) {
      sortingDropdown.add(sortingStrategy);
    }
    sortingDropdown.setSelected(this.sortingStrategy);
    sortingDropdown.setTranslationKeyPrefix("betterfriends.settings.advancedFriendlist.sorting");
    sortingDropdown.setChangeListener(this::applySortingStrategy);

    filterSettings.addEntry(searchField);
    filterSettings.addEntry(sortingDropdown);

    this.scroll = new ScrollWidget(this.entries);
    this.entries.setComparator((f1, f2) -> {
      if (!(f1 instanceof FriendWidget friendWidget1 && f2 instanceof FriendWidget friendWidget2)) {
        return 0;
      }
      Friend friend1 = friendWidget1.getFriend();
      Friend friend2 = friendWidget2.getFriend();
      int comparePin = Boolean.compare(friend2.isPinned(), friend1.isPinned());
      if (comparePin != 0) {
        return comparePin;
      }
      int selectedComparator = sortingDropdown.getSelected().compare(friend1, friend2);
      if (selectedComparator != 0) {
        return selectedComparator;
      }
      ChatMessage message1 = friend1.chat().getLastMessage();
      ChatMessage message2 = friend2.chat().getLastMessage();
      if (message1 != null || message2 != null) {
        int compareLastMessage = Long.compare(
            message2 != null ? message2.getTimestamp() : 0L,
            message1 != null ? message1.getTimestamp() : 0L
        );
        if (compareLastMessage != 0) {
          return compareLastMessage;
        }
      }
      return Long.compare(friend2.getLastOnline(), friend1.getLastOnline());
    });
    this.initializeFriendlist(false);

    container.addContent(filterSettings);
    container.addContent(this.error);
    container.addFlexibleContent(this.scroll);
    this.document.addChild(container);
  }

  private void initializeFriendlist(boolean initialized) {
    this.entries.getChildren().clear();
    LabyConnectSession session = Laby.references().labyConnect().getSession();
    if (session == null || !session.isAuthenticated()) {
      this.handleError("betterfriends.errors.notConnected");
      return;
    }
    if (session.getFriends().isEmpty()) {
      this.handleError("betterfriends.errors.emptyList");
      return;
    }
    this.error.setVisible(false);
    this.scroll.setVisible(true);

    List<T> children = new ArrayList<>();
    int addedOfflineFriends = 0;

    for (Friend friend : session.getFriends()) {
      boolean offline = !friend.isOnline();
      if ((!offline || addedOfflineFriends < 100 || friend.isPinned()) && this.isUserInFilter(
          friend)) {
        children.add(this.friendWidgetConstructor.apply(friend));
        if (offline) {
          addedOfflineFriends++;
        }
      }
    }

    if (initialized) {
      this.entries.addChildrenInitialized(children, true);
    } else {
      this.entries.addChildren(children, true);
    }
  }

  private void handleError(String translatable) {
    this.error.setComponent(Component.translatable(translatable));
    this.error.setVisible(true);
    this.scroll.setVisible(false);
  }

  private void applyFriendsFilter(String query) {
    this.filterQuery = query;
    this.initializeFriendlist(true);
  }

  private void applySortingStrategy(SortingStrategy strategy) {
    this.sortingStrategy = strategy;
    this.initializeFriendlist(true);
  }

  private boolean hasFilter() {
    return this.filterQuery != null && !this.filterQuery.isEmpty();
  }

  private boolean isUserInFilter(User user) {
    return !this.hasFilter() || user.getName().toLowerCase()
        .contains(this.filterQuery.toLowerCase());
  }

  @Subscribe
  public void onUserUpdateData(final UserUpdateDataEvent event) {
    if (event.phase() == Phase.POST) {
      this.labyAPI.minecraft().executeOnRenderThread(() ->
          FriendListActivity.this.entries.reInitializeChildrenIf(
              FriendWidget.class,
              widget -> widget.getFriend().getUniqueId().equals(
                  event.gameUser().getUniqueId()
              )
          )
      );
    }
  }

  @Subscribe
  public void onLabyConnectFriendAdd(LabyConnectFriendAddEvent event) {
    this.entries.addChildAsync(this.friendWidgetConstructor.apply(event.friend()));
  }

  @Subscribe
  public void onLabyConnectFriendAddBulk(LabyConnectFriendAddBulkEvent event) {
    for (Friend friend : event.getFriends()) {
      this.entries.addChildAsync(this.friendWidgetConstructor.apply(friend));
    }
  }

  @Subscribe
  public void onLabyConnectFriendRemove(LabyConnectFriendRemoveEvent event) {
    this.entries.removeChildIf(
        FriendWidget.class,
        widget -> widget.getFriend().getUniqueId().equals(event.friend().getUniqueId())
    );
  }

  private enum SortingStrategy {
    ONLINE_STATUS((a, b) -> Boolean.compare(b.isOnline(), a.isOnline())),
    ROLE((a, b) -> Integer.compare(
        GroupHelper.getGroupIndex(a.gameUser().visibleGroup().getIdentifier()),
        GroupHelper.getGroupIndex(b.gameUser().visibleGroup().getIdentifier())
    )),
    A_TO_Z((a, b) -> a.getName().compareToIgnoreCase(b.getName())),
    Z_TO_A((a, b) -> b.getName().compareToIgnoreCase(a.getName()));

    private final BiFunction<Friend, Friend, Integer> sortingFunction;

    SortingStrategy(BiFunction<Friend, Friend, Integer> sortingFunction) {
      this.sortingFunction = sortingFunction;
    }

    public int compare(Friend f1, Friend f2) {
      return this.sortingFunction.apply(f1, f2);
    }
  }
}