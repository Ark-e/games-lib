package lib.games.ui.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.backend.DataService;
import lib.games.data.Game;
import lib.games.data.User;

import java.util.ArrayList;
import java.util.List;

@Route(value = "logup")
public class LogupView extends VerticalLayout implements BeforeEnterObserver {

    public LogupView() {
        setSizeFull();

        TextField realnameField = new TextField("Name");
        TextField usernameField = new TextField("Username");
        EmailField emailField= new EmailField("Email");
        PasswordField passwordField = new PasswordField("Password");
        PasswordField passwordField2 = new PasswordField("Repeat Password");

        realnameField.setRequired(true);
        usernameField.setRequired(true);
        emailField.setRequiredIndicatorVisible(true);
        passwordField.setRequired(true);
        passwordField2.setRequired(true);



        Button logup = new Button("Logup", e -> {
            if (!realnameField.isEmpty() && !usernameField.isEmpty() && !emailField.isEmpty() && !passwordField.isEmpty() && !passwordField2.isEmpty()) {
                if(passwordField.getValue().equals(passwordField2.getValue())) {
                    if (!emailField.isInvalid()) {
                        User user = new User(usernameField.getValue(), realnameField.getValue(), passwordField.getValue(), usernameField.getValue());
                        DataService.getInstance().addUser(user);
                        AccessControlFactory.getInstance().createAccessControl().signIn(user.getUsername(), user.getPassword());
                        UI.getCurrent().navigate("games");
                    }

                }
            }
            else {
                if (realnameField.isEmpty()) {
                    realnameField.setInvalid(true);
                }
                if (usernameField.isEmpty()) {
                    usernameField.setInvalid(true);
                }
                if (emailField.isEmpty()) {
                    emailField.setInvalid(true);
                }
                if(passwordField.isEmpty()) {
                    passwordField.setInvalid(true);
                }
                if(passwordField2.isEmpty()) {
                    passwordField2.setInvalid(true);
                }
                passwordField.clear();
                passwordField2.clear();
            }
        });
        Button discard = new Button("Discard", e -> {
            realnameField.clear();
            usernameField.clear();
            emailField.clear();
            passwordField.clear();
            passwordField2.clear();
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        horizontalLayout.add(logup, discard);

        add(realnameField, usernameField, emailField, passwordField, passwordField2, horizontalLayout);
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
