package lib.games.ui.personal;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import lib.games.authentication.AccessControlFactory;
import lib.games.backend.DataService;
import lib.games.data.User;
import lib.games.ui.Layout;



@Route(value = "personal", layout = Layout.class)
public class PersonalView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {
    private H1 title;
    private TextField username;
    private TextField realname;
    private EmailField email;
    private PasswordField password;
    private PasswordField repeatPassword;
    private Button saveButton;
    private Button cancelButton;
    private Button changeButton;
    private User user;
    private CommentList commentList;

    public PersonalView() {

        title = new H1();
        HorizontalLayout headerLayout = new HorizontalLayout();

        Icon userIcon = new Icon(VaadinIcon.USER);
        userIcon.setSize("80px");
        headerLayout.add(userIcon, title);
        add(headerLayout);

        username = new TextField("Username");
        realname = new TextField("Real name");
        email = new EmailField("Email");
        password = new PasswordField("Password");
        repeatPassword = new PasswordField("Repeat password");

        username.setRequired(true);
        realname.setRequired(true);
        email.setRequiredIndicatorVisible(true);
        password.setRequired(true);
        repeatPassword.setRequired(true);

        user = AccessControlFactory.getInstance().createAccessControl().getUser();
        if
        (user != null) {
            fill();
            setEditable(false);
        }
    }

    public void fill() {
            title.add(AccessControlFactory.getInstance().createAccessControl().getPrincipalName());
            Label commentsLabel = new Label("Comments:");

            setUser();
            commentList = new CommentList(user);

            HorizontalLayout buttonsLayout = new HorizontalLayout();

            changeButton = new Button("Change", e -> {
                setEditable(true);
            });

            saveButton = new Button("Save", e -> {
                saveChanges();
                setEditable(false);
            });

            cancelButton = new Button("Cancel", e -> {
                setUser();
                setEditable(false);
            });

            buttonsLayout.add(changeButton, saveButton, cancelButton);

            add(username, realname, email, password, repeatPassword, buttonsLayout, commentsLabel, commentList);
    }

    public void saveChanges() {
        if (!username.isEmpty()) {
            user.setUsername(username.getValue());
        }

        if (!realname.isEmpty()) {
            user.setFullName(realname.getValue());
        }

        if (!email.isEmpty() && !email.isInvalid()) {
            user.setEmail(email.getValue());
        }

        if (!password.isEmpty() && !repeatPassword.isEmpty() && password.getValue().equals(repeatPassword.getValue())) {
            user.setPassword(password.getValue());
        }
        DataService.getInstance().updateUser(user);
        AccessControlFactory.getInstance().createAccessControl().changeUser(user);
    }

    public void setUser() {
        username.setValue(user.getUsername());
        realname.setValue(user.getFullName());
        email.setValue(user.getEmail());
        password.setValue(user.getPassword());
    }

    public void setEditable(boolean edit) {
        username.setReadOnly(!edit);
        realname.setReadOnly(!edit);
        email.setReadOnly(!edit);
        password.setReadOnly(!edit);
        repeatPassword.setVisible(edit);
        saveButton.setVisible(edit);
        saveButton.setEnabled(edit);
        cancelButton.setVisible(edit);
        cancelButton.setEnabled(edit);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!AccessControlFactory.getInstance().createAccessControl().isUserSignedIn()) {
            UI.getCurrent().navigate("login");
            UI.getCurrent().getPage().reload();
        }
    }

    public void setParameter(BeforeEvent event, String parameter) {}
}
