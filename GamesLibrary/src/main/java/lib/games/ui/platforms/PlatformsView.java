package lib.games.ui.platforms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.data.AccessLevel;
import lib.games.ui.Layout;
import lib.games.ui.additional.GameXDataprovider;
import lib.games.ui.additional.PlatformDataProvider;
import lib.games.ui.shops.EditShopDialog;
import lib.games.ui.shops.ShopList;

@Route(value = "platforms", layout = Layout.class)
public class PlatformsView extends VerticalLayout implements BeforeEnterObserver {

    private final PlatformList list;
    private TextField filter;
    private Button deleteButton;
    private Button addButton;
    private Button editButton;

    private final PlatformDataProvider dataProvider = new PlatformDataProvider();

    public PlatformsView() {
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        list = new PlatformList();
        list.setDataProvider(dataProvider);

        final VerticalLayout pageLayout = new VerticalLayout();

        H1 title = new H1("Platforms");
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
        filter.setPlaceholder("Filter name or url");
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        final HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);


        HorizontalLayout buttonsLayout = addButtons();
        topLayout.add(buttonsLayout);

        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public HorizontalLayout addButtons() {
        deleteButton = new Button();
        deleteButton.addClickListener(e ->  deletePlatform());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        deleteButton.setIcon(VaadinIcon.BAN.create());

        addButton = new Button();
        addButton.addClickListener(e -> addPlatform());
        addButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());

        editButton = new Button();
        editButton.addClickListener(e -> editPlatform());
        editButton.setIcon(VaadinIcon.EDIT.create());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(addButton, editButton, deleteButton);

        return horizontalLayout;
    }
    public void clearSelection() {
        list.getSelectionModel().deselectAll();
    }

    private void deletePlatform() {
        if (list.getSelected() != null) {
            GameXDataprovider gameXDataprovider = new GameXDataprovider();
            gameXDataprovider.deleteGamePlatform("platformid", list.getSelected().getId());
            dataProvider.delete(list.getSelected());
            clearSelection();
            dataProvider.refreshAll();
            UI.getCurrent().getPage().reload();
        }
    }

    private void editPlatform() {
        new EditPlatformDialog(dataProvider, list.getSelected()).open();
    }

    private void addPlatform(){
        new EditPlatformDialog(dataProvider, null).open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        AccessControl accessControl = AccessControlFactory.getInstance().createAccessControl();
        if (!accessControl.isUserSignedIn()) {
            UI.getCurrent().navigate("login");
            UI.getCurrent().getPage().reload();
        } else if (!accessControl.isUserInRole(AccessLevel.MODERATOR)) {
            UI.getCurrent().navigate("games");
            UI.getCurrent().getPage().reload();
        }
    }
}
