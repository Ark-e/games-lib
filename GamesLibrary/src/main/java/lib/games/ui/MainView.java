package lib.games.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;

@Route(value = "")
public class MainView extends VerticalLayout implements BeforeEnterObserver {

    public MainView() {

    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        UI.getCurrent().navigate("games");
        UI.getCurrent().getPage().reload();
    }
}
