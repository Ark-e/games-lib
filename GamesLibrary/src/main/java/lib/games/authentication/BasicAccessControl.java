package lib.games.authentication;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import lib.games.backend.DataService;
import lib.games.data.AccessLevel;
import lib.games.data.User;

public class BasicAccessControl implements AccessControl {

    @Override
    public boolean signIn(String username, String password) {
        if (username == null || username.isEmpty()) {
            return false;
        }

        User currentUser = DataService.getInstance().login(username, password);
        if (currentUser == null) {
            return false;
        }

        CurrentUser.set(currentUser);
        return true;
    }

    @Override
    public boolean isUserSignedIn() {
        return !CurrentUser.get().isEmpty();
    }

    @Override
    public boolean isUserInRole(AccessLevel level) {
        if (level.equals(AccessLevel.ADMINISTRATOR)) {
            return CurrentUser.level == AccessLevel.ADMINISTRATOR;
        }
        if (level.equals(AccessLevel.MODERATOR)) {
            return CurrentUser.level == AccessLevel.MODERATOR || CurrentUser.level == AccessLevel.ADMINISTRATOR;
        }
        return true;
    }

    public User getUser() {
        return CurrentUser.getUser();
    }

    public User changeUser(User user) {
        return CurrentUser.getUser();
    }

    @Override
    public String getPrincipalName() {
        return CurrentUser.get();
    }

    @Override
    public void signOut() {
        VaadinSession.getCurrent().getSession().invalidate();
        UI.getCurrent().navigate("login");
    }
}
