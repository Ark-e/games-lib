package lib.games.ui.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.authentication.BasicAccessControl;
import lib.games.backend.DataService;
import lib.games.ui.Layout;

@Route(value = "login")
public class LoginView extends FlexLayout implements BeforeEnterObserver {
    private AccessControl accessControl;

    public LoginView() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();

    }

    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(this::login);
        loginForm.addForgotPasswordListener(
                event -> Notification.show("You have no chances"));


        Button logup = new Button("Logup", e -> {
            UI.getCurrent().navigate("logup");
            UI.getCurrent().getPage().reload();
        });
        logup.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setSizeFull();
        verticalLayout.add(loginForm, logup);


        add(verticalLayout);
    }



    private void login(LoginForm.LoginEvent event) {
        if (accessControl.signIn(event.getUsername(), event.getPassword())) {
            UI.getCurrent().navigate("games");
        } else {
            event.getSource().setError(true);
        }
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
        if (accessControl.isUserSignedIn()) {
            UI.getCurrent().navigate("games");
            UI.getCurrent().getPage().reload();
        }
    }
}
