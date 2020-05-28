package lib.games.ui.shops;

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
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.data.AccessLevel;
import lib.games.ui.Layout;
import lib.games.ui.additional.GameXDataprovider;
import lib.games.ui.additional.ShopDataProvider;

@Route(value = "shops", layout = Layout.class)
public class ShopsView extends VerticalLayout implements BeforeEnterObserver {

    private final ShopList list;
    private TextField filter;
    private Button deleteButton;
    private Button addButton;
    private Button editButton;

    private final ShopDataProvider dataProvider = new ShopDataProvider();

    public ShopsView() {
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        list = new ShopList();
        list.setDataProvider(dataProvider);

        final VerticalLayout pageLayout = new VerticalLayout();

        H1 title = new H1("Shops");
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
        deleteButton.addClickListener(e ->  deleteShop());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        deleteButton.setIcon(VaadinIcon.BAN.create());

        addButton = new Button();
        addButton.addClickListener(e -> addShop());
        addButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());

        editButton = new Button();
        editButton.addClickListener(e -> editShop());
        editButton.setIcon(VaadinIcon.EDIT.create());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(addButton, editButton, deleteButton);

        return horizontalLayout;
    }
    public void clearSelection() {
        list.getSelectionModel().deselectAll();
    }

    private void deleteShop() {
        if (list.getSelected() != null) {
            GameXDataprovider gameXDataprovider = new GameXDataprovider();
            gameXDataprovider.deleteGameShop("shopid", list.getSelected().getId());
            dataProvider.delete(list.getSelected());
            clearSelection();
            dataProvider.refreshAll();
            UI.getCurrent().getPage().reload();
        }
    }

    private void editShop() {
        new EditShopDialog(dataProvider, list.getSelected()).open();
    }

    private void addShop(){
        new EditShopDialog(dataProvider, null).open();
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
