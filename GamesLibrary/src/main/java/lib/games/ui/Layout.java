package lib.games.ui;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.authentication.BasicAccessControl;
import lib.games.data.AccessLevel;
import lib.games.ui.games.GamesView;
import lib.games.ui.personal.PersonalView;
import lib.games.ui.shops.ShopsView;
import lib.games.ui.users.UsersView;
/*
* Боковая панель
* */
public class Layout extends AppLayout implements RouterLayout {

    private final Button logoutButton;
    private final Button viewGamesButton;
    private final Button viewUsersButton;
    private final Button viewPersonalButton;
    private final Label welcomeLabel;

    public Layout() {

        final DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassName("menu-toggle");
        addToNavbar(drawerToggle);


        final HorizontalLayout top = new HorizontalLayout();
        top.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        top.setClassName("menu-header");


        final Label title = new Label("Games Library");
        top.add(title);
        addToNavbar(top);

        VerticalLayout verticalLayout = new VerticalLayout();

        welcomeLabel = new Label("Welcome, " + AccessControlFactory.getInstance().createAccessControl().getPrincipalName()+"!");
        verticalLayout.add(welcomeLabel);

        logoutButton = createMenuButton("Logout", VaadinIcon.SIGN_OUT.create());
        logoutButton.addClickListener(e -> logout());

        viewGamesButton = createMenuButton("Games", VaadinIcon.GAMEPAD.create());
        viewGamesButton.addClickListener(e ->  UI.getCurrent().navigate("games"));

        viewUsersButton = createMenuButton("Users", VaadinIcon.USERS.create());
        viewUsersButton.addClickListener(e ->  UI.getCurrent().navigate("users"));

        viewPersonalButton = createMenuButton("Personal", VaadinIcon.USER.create());
        viewPersonalButton.addClickListener(e ->  UI.getCurrent().navigate(PersonalView.class, AccessControlFactory.getInstance().createAccessControl().getPrincipalName()));

        AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
        if (accessControl.isUserInRole(AccessLevel.MODERATOR)) {
            Button addGameButton = createMenuButton("Add new", VaadinIcon.PENCIL.create());
            addGameButton.addClickListener(e ->  UI.getCurrent().navigate("new"));

            Button viewCompaniesButton = createMenuButton("Companies", VaadinIcon.BUILDING.create());
            viewCompaniesButton.addClickListener(e ->  UI.getCurrent().navigate("companies"));

            Button viewShopsButton = createMenuButton("Shops", VaadinIcon.COIN_PILES.create());
            viewShopsButton.addClickListener(e ->  UI.getCurrent().navigate(ShopsView.class));

            Button viewPlatformsButton = createMenuButton("Platforms", VaadinIcon.AUTOMATION.create());
            viewPlatformsButton.addClickListener(e ->  UI.getCurrent().navigate("platforms"));

            Button viewLocalisationsButton = createMenuButton("Localisations", VaadinIcon.ACADEMY_CAP.create());
            viewLocalisationsButton.addClickListener(e ->  UI.getCurrent().navigate("localisations"));

            verticalLayout.add(viewCompaniesButton, viewShopsButton, viewPlatformsButton, viewLocalisationsButton, addGameButton);
        }


        verticalLayout.add(viewGamesButton, viewUsersButton, viewPersonalButton, logoutButton);
        addToDrawer(verticalLayout);
    }

    private void logout() {
        AccessControlFactory.getInstance().createAccessControl().signOut();
    }


    private Button createMenuButton(String caption, Icon icon) {
        final Button routerButton = new Button(caption);
        routerButton.setClassName("menu-button");
        routerButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        routerButton.setIcon(icon);
        icon.setSize("32px");
        return routerButton;
    }

}
