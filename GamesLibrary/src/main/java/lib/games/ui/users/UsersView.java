package lib.games.ui.users;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.Lumo;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.authentication.CurrentUser;
import lib.games.backend.DataService;
import lib.games.data.AccessLevel;
import lib.games.data.Game;
import lib.games.data.User;
import lib.games.ui.Layout;
import lib.games.ui.games.GamesDataProvider;
import lib.games.ui.games.GamesForm;
import lib.games.ui.games.GamesList;

@Route(value = "users", layout = Layout.class)
public class UsersView extends VerticalLayout implements BeforeEnterObserver {

    public static final String VIEW_NAME = "Users";
    private final UserList list;
    private TextField filter;
    private Button deleteButton;
    private Button promoteButton;
    private Button demoteButton;

    private final UsersDataProvider dataProvider = new UsersDataProvider();

    public UsersView() {
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        list = new UserList();
        list.setDataProvider(dataProvider);

        final VerticalLayout pageLayout = new VerticalLayout();

        H1 title = new H1("Users");
        pageLayout.add(title);
        pageLayout.add(topLayout);
        pageLayout.add(list);
        pageLayout.setFlexGrow(1, list);
        pageLayout.setFlexGrow(0, topLayout);
        pageLayout.setSizeFull();
        pageLayout.expand(list);

        add(pageLayout);
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        if (CurrentUser.level == AccessLevel.USER) {
            filter.setPlaceholder("Filter name or real name");
        }
        else {
            filter.setPlaceholder("Filter name, real name or email");
        }
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);



        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);

        if (CurrentUser.level == AccessLevel.ADMINISTRATOR) {
            HorizontalLayout buttonsLayout = addButtons();
            topLayout.add(buttonsLayout);
        }

        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public HorizontalLayout addButtons() {
        deleteButton = new Button();
        deleteButton.addClickListener(e ->  deleteUser());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        deleteButton.setIcon(VaadinIcon.BAN.create());

        promoteButton = new Button();
        promoteButton.addClickListener(e -> promoteUser());
        promoteButton.setIcon(VaadinIcon.ANGLE_DOUBLE_UP.create());

        demoteButton = new Button();
        demoteButton.addClickListener(e -> demoteUser());
        demoteButton.setIcon(VaadinIcon.ANGLE_DOUBLE_DOWN.create());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(promoteButton, demoteButton, deleteButton);

        return horizontalLayout;
    }
    public void clearSelection() {
        list.getSelectionModel().deselectAll();
    }

    private void deleteUser() {
        if (list.getSelected() != null) {
            DataService.getInstance().deleteCommentsByObject("userid", list.getSelected().getId());
            dataProvider.delete(list.getSelected());
            clearSelection();
            dataProvider.refreshAll();
            UI.getCurrent().getPage().reload();
        }
    }

    private void promoteUser() {
        User user = list.getSelected();
        if (user != null && user.getAccessLevel() == AccessLevel.USER) {
            user.setAccessLevel(AccessLevel.MODERATOR);
            dataProvider.save(user);
            dataProvider.refreshAll();
        }
    }

    private void demoteUser(){
        User user = list.getSelected();
        if (user != null && user.getAccessLevel() == AccessLevel.MODERATOR) {
            user.setAccessLevel(AccessLevel.USER);
            dataProvider.save(user);
            dataProvider.refreshAll();
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
        if (!accessControl.isUserSignedIn()) {
            UI.getCurrent().navigate("login");
            UI.getCurrent().getPage().reload();
        }
    }


}
