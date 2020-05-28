package lib.games.ui.companies;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.backend.DataService;
import lib.games.data.AccessLevel;
import lib.games.ui.Layout;
import lib.games.ui.additional.CompanyDataProvider;
import lib.games.ui.additional.ShopDataProvider;
import lib.games.ui.games.GamesDataProvider;
import lib.games.ui.shops.EditShopDialog;
import lib.games.ui.shops.ShopList;

@Route(value = "companies", layout = Layout.class)
public class CompaniesView extends VerticalLayout implements BeforeEnterObserver {

    private final CompaniesList list;
    private TextField filter;
    private Button deleteButton;
    private Button addButton;
    private Button editButton;

    private final CompanyDataProvider dataProvider = new CompanyDataProvider();

    public CompaniesView() {
        setSizeFull();
        final HorizontalLayout topLayout = createTopBar();
        list = new CompaniesList();
        list.setDataProvider(dataProvider);

        final VerticalLayout pageLayout = new VerticalLayout();

        H1 title = new H1("Companies");
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
        filter.setPlaceholder("Filter name, country, year");
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
        deleteButton.addClickListener(e ->  deleteCompany());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        deleteButton.setIcon(VaadinIcon.BAN.create());

        addButton = new Button();
        addButton.addClickListener(e -> addCompany());
        addButton.setIcon(VaadinIcon.PLUS_CIRCLE.create());

        editButton = new Button();
        editButton.addClickListener(e -> editCompany());
        editButton.setIcon(VaadinIcon.EDIT.create());

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(addButton, editButton, deleteButton);

        return horizontalLayout;
    }
    public void clearSelection() {
        list.getSelectionModel().deselectAll();
    }

    private void deleteCompany() {

        if (list.getSelected() != null &&  DataService.getInstance().getGamesByCompany(list.getSelected().getId()) == null) {
            dataProvider.delete(list.getSelected());
            clearSelection();
            dataProvider.refreshAll();
            UI.getCurrent().getPage().reload();
        }
        else {
            Notification notification = new Notification(list.getSelected().getName() + " can't be deleted!");
            notification.setDuration(500);
            notification.open();
        }
    }

    private void editCompany() {
        new EditCompanyDialog(dataProvider, list.getSelected()).open();
    }

    private void addCompany(){
        new EditCompanyDialog(dataProvider, null).open();
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
