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
import com.vaadin.flow.server.VaadinSession;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.authentication.CurrentUser;
import lib.games.data.User;
import lib.games.ui.Layout;



@Route(value = "personal", layout = Layout.class)
public class PersonalView extends VerticalLayout implements HasUrlParameter<String>, BeforeEnterObserver {


    public PersonalView() {


        H1 title = new H1(AccessControlFactory.getInstance().createAccessControl().getPrincipalName());
        HorizontalLayout headerLayout = new HorizontalLayout();

        Icon userIcon = new Icon(VaadinIcon.USER);
        userIcon.setSize("80px");
        headerLayout.add(userIcon, title);
        add(headerLayout);

        TextField username = new TextField("Username");
        TextField realname = new TextField("Real name");
        EmailField email = new EmailField("Email");
        PasswordField password = new PasswordField("Password");

        username.setReadOnly(true);
        realname.setReadOnly(true);
        email.setReadOnly(true);
        password.setReadOnly(true);

        HorizontalLayout passwordLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button changeButton = new Button("Change", e -> {
            username.setReadOnly(false);
            username.setRequired(true);

        });
        Button saveButton = new Button("Save", e -> {

        });
        saveButton.setVisible(false);

        Button cancelButton = new Button("Cancel", e -> {

        });
       cancelButton.setVisible(false);

        buttonsLayout.add(changeButton);




        Label commentsLabel = new Label("Comments:");
        CommentList commentList;

        User user = AccessControlFactory.getInstance().createAccessControl().getUser();
        if (user != null) {
            realname.setValue(user.getFullName());
            email.setValue(user.getEmail());
            password.setValue(user.getPassword());
            commentList = new CommentList(user);
            add(realname, email, password, commentsLabel, commentList);
        }

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
