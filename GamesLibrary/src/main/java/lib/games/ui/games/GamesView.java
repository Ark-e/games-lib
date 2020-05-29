package lib.games.ui.games;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.authentication.CurrentUser;
import lib.games.data.Game;
import lib.games.data.Localisation;
import lib.games.data.Platform;
import lib.games.data.Shop;
import lib.games.ui.Layout;

import java.util.Set;

@Route(value = "games", layout = Layout.class)
@PWA(name = "Games Library", shortName = "Games Lib")
public class GamesView extends HorizontalLayout implements HasUrlParameter<String>, BeforeEnterObserver {

    public static final String VIEW_NAME = "Games";
    private final GamesList list;
    private final GamesForm form;
    private VerticalLayout listWithBarLayout;
    private TextField filter;

    private final GameViewLogic viewLogic = new GameViewLogic(this);

    private final GamesDataProvider dataProvider = new GamesDataProvider();

    public GamesView() {
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        list = new GamesList();
        list.setDataProvider(dataProvider);
        list.asSingleSelect().addValueChangeListener(event -> viewLogic.rowSelected(event.getValue()));
        form = new GamesForm(viewLogic);
        listWithBarLayout = new VerticalLayout();
        listWithBarLayout.add(topLayout);
        listWithBarLayout.add(list);
        listWithBarLayout.setFlexGrow(1, list);
        listWithBarLayout.setFlexGrow(0, topLayout);
        listWithBarLayout.setSizeFull();
        listWithBarLayout.expand(list);

        add(listWithBarLayout);
        add(form);

        viewLogic.init();

    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filter name, year or genre");
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);


        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showNotification(String msg) {
        Notification.show(msg);
    }

    public void clearSelection() {
        list.getSelectionModel().deselectAll();
    }

    public void selectRow(Game row) {
        list.getSelectionModel().select(row);
    }

    public void updateGame(Game game, Set<Shop> shopSet, Set<Platform> platformSet, Set<Localisation> localisationSet) {

        dataProvider.save(game, shopSet, platformSet, localisationSet);
    }

    public void removeGame(Game game) {
        dataProvider.delete(game);
    }


    public void editGame(Game game) {
        showForm(game != null);
        form.editGame(game);
    }


    public void showForm(boolean show) {
        form.setVisible(show);
        form.setEnabled(show);
        listWithBarLayout.setVisible(!show);
        listWithBarLayout.setEnabled(!show);
        if(show){
            form.setWidth("100%");
        }
        else{
            listWithBarLayout.setWidth("800%");
        }
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
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
