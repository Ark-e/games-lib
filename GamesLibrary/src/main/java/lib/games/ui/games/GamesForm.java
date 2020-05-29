package lib.games.ui.games;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lib.games.authentication.AccessControlFactory;
import lib.games.backend.DataService;
import lib.games.data.*;
import lib.games.ui.additional.CompanyDataProvider;

import java.util.*;

public class GamesForm  extends Div {

    private final VerticalLayout content;
    private final VerticalLayout commentsLayout;

    private final TextField name;
    private final TextField genre;
    private final TextField year;
    private final MultiSelectListBox<Platform> platforms;
    private final MultiSelectListBox<Shop> shops;
    private final MultiSelectListBox<Localisation> localisations;
    private final ComboBox<Company> developer;
    private final ComboBox<Company> distributor;
    private final TextArea description;
    private Button showComments;
    private Button hideComments;
    private Button back;
    private Button edit;
    private Button save;
    private Button discard;
    private Button delete;

    private final GameViewLogic viewLogic;
    private final Binder<Game> binder;
    private Game currentGame;

    public GamesForm(GameViewLogic viewLogic) {
        setClassName("game-form");

        content = new VerticalLayout();
        content.setSizeUndefined();
        content.addClassName("game-form-content");
        add(content);

        commentsLayout = new VerticalLayout();
        commentsLayout.getStyle().set("border", "1px solid #9E9E9E");

        this.viewLogic = viewLogic;

        content.add(topButtonsLayout());

        name = new TextField("Name:");
        name.setWidth("100%");
        name.setRequired(true);
        content.add(name);

        genre = new TextField("Genre:");
        genre.setWidth("100%");
        genre.setRequired(true);
        content.add(genre);

        year = new TextField("Release year:");
        year.setWidth("100%");
        year.setRequired(true);
        content.add(year);

        Label platformsLabel = new Label("Platforms:");
        platforms = new MultiSelectListBox<>();
        platforms.setWidth("100%");
        content.add(platformsLabel, platforms);


        platforms.setRenderer(new ComponentRenderer<>(item -> {
            Label label = new Label(item.getName());
            return label;
        }));

        Label shopsLabel = new Label("Shops:");
        shops = new MultiSelectListBox<>();
        shops.setWidth("100%");
        content.add(shopsLabel, shops);

        shops.setRenderer(new ComponentRenderer<>(item -> {
            Label label = new Label(item.getName());
            return label;
        }));

        Label localisationsLabel = new Label("Localisations:");
        localisations = new MultiSelectListBox<>();
        localisations.setWidth("100%");
        content.add(localisationsLabel, localisations);

        localisations.setRenderer(new ComponentRenderer<>(item -> {
            Label label = new Label(item.getLocale());
            return label;
        }));

        developer = new ComboBox<>("Developer:");
        developer.setWidth("100%");
        developer.setRequired(true);
        content.add(developer);

        distributor = new ComboBox<>("Distributor:");
        distributor.setWidth("100%");
        distributor.setRequired(true);
        content.add(distributor);

        description = new TextArea("Description");
        description.setWidth("100%");
        content.add(description);

        setReadOnly(true);

        binder = new Binder<>(Game.class);

        setButtonsShow(AccessControlFactory.getInstance().createAccessControl()
                .isUserInRole(AccessLevel.MODERATOR));

        showComments = new Button("Show Comments", e ->  openComments(true));
        hideComments = new Button("Hide Comments", e -> openComments(false));
        showComments.setWidth("100%");
        hideComments.setWidth("100%");

        openComments(false);
        content.add(showComments, commentsLayout, hideComments);

        addCommentCreation();
    }


    public HorizontalLayout topButtonsLayout() {

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();

        back = new Button("Return", new Icon(VaadinIcon.ARROW_LEFT));
        back.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        back.setWidth("10%");
        back.addClickListener(event -> {
            viewLogic.cancelGame();
            setReadOnly(true);
            save.setEnabled(false);
            save.setVisible(false);
            discard.setVisible(false);
            discard.setEnabled(false);
        });

        edit = new Button("Edit");
        edit.addClickListener(event -> {
            setReadOnly(false);
            save.setEnabled(true);
            save.setVisible(true);
            discard.setEnabled(true);
            discard.setVisible(true);
        });

        delete = new Button("Delete");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_PRIMARY);

        delete.addClickListener(event -> {
            if (currentGame != null) {
                viewLogic.deleteGame(currentGame);
                UI.getCurrent().getPage().reload();
            }
        });

        save = new Button("Save");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addClickListener(event -> confirmChanges());

        discard = new Button("Discard", event -> viewLogic.editGame(currentGame));
        horizontalLayout.add(back, edit, save, discard, delete);
        return  horizontalLayout;
    }

    public void editGame(Game game) {
        currentGame = game;
        binder.readBean(game);

        if (currentGame != null){
            name.setValue(currentGame.getName());
            genre.setValue(currentGame.getGenre());
            year.setValue(currentGame.getReleaseYear());

            refreshComments();

            additionalInfoViewMode(AccessControlFactory.getInstance().createAccessControl()
                    .isUserInRole(AccessLevel.MODERATOR));

            CompanyDataProvider companyDataProvider = new CompanyDataProvider();
            developer.setDataProvider(companyDataProvider);
            developer.setValue(DataService.getInstance().getCompany(currentGame.getDeveloper()));
            developer.setItemLabelGenerator(Company::getName);

            distributor.setDataProvider(companyDataProvider);
            distributor.setValue(DataService.getInstance().getCompany(currentGame.getDistributor()));
            distributor.setItemLabelGenerator(Company::getName);
        }
    }

    public void additionalInfoViewMode(boolean show) {

        List gameShop = DataService.getInstance().getGameShopsBy("gameid",currentGame.getId());
        Set<Shop> shopList = new HashSet<>();
        for (Object i: gameShop){
            GameShop gameshop = (GameShop)i;
            shopList.add(DataService.getInstance().getShop(gameshop.getShopId()));
        }

        List gamePlatform = DataService.getInstance().getGamePlatformsBy("gameid",currentGame.getId());
        Set<Platform> platformList = new HashSet<>();
        for (Object i: gamePlatform){
            GamePlatform gameplatform = (GamePlatform) i;
            platformList.add(DataService.getInstance().getPlatform(gameplatform.getPlatformId()));
        }

        List gameLocalisation = DataService.getInstance().getGameLocalisationsBy("gameid",currentGame.getId());
        Set<Localisation> localisationList = new HashSet<>();
        for (Object i: gameLocalisation){
            GameLocalisation gamelocalisation = (GameLocalisation) i;
            localisationList.add(DataService.getInstance().getLocalisations(gamelocalisation.getLocalisationId()));
        }

        if (show) {
            List<Shop> fullShopList = DataService.getInstance().getAllShops();
            List<Platform> fullPlatformList = DataService.getInstance().getAllPlatforms();
            List<Localisation> fullLocalisationList = DataService.getInstance().getAllLocalisations();

            shops.setItems(fullShopList);
            platforms.setItems(fullPlatformList);
            localisations.setItems(fullLocalisationList);

            if (shopList.size() > 0) {
                for (Shop shop: fullShopList) {
                    for (Shop shopUsed: shopList) {
                        if (shop.getId().equals(shopUsed.getId())) {
                            shops.select(shop);
                        }
                    }
                }

            }
            if (platformList.size() > 0) {
                for (Platform platform: fullPlatformList) {
                    for (Platform platformUsed: platformList) {
                        if (platform.getId().equals(platformUsed.getId())) {
                            platforms.select(platform);
                        }
                    }
                }

            }
            if (localisationList.size() > 0) {
                for (Localisation localisation: fullLocalisationList) {
                    for (Localisation localisationUsed: localisationList) {
                        if (localisation.getId().equals(localisationUsed.getId())) {
                            localisations.select(localisation);
                        }
                    }
                }

            }
        } else {
            shops.setItems(shopList);
            platforms.setItems(platformList);
            localisations.setItems(localisationList);
        }
    }

    public void confirmChanges() {
        if (currentGame != null
                && binder.writeBeanIfValid(currentGame)) {
            if (!name.isEmpty()) {
                currentGame.setName(name.getValue());
            }
            if (!genre.isEmpty()) {
                currentGame.setGenre(genre.getValue());
            }
            if (!year.isEmpty()) {
                currentGame.setReleaseYear(year.getValue());
            }
            if (!developer.isEmpty()) {
                currentGame.setDeveloper(developer.getValue().getId());
            }
            if (!distributor.isEmpty()) {
                currentGame.setDistributor(distributor.getValue().getId());
            }
            save.setEnabled(false);
            save.setVisible(false);
            discard.setVisible(false);
            discard.setEnabled(false);
            viewLogic.saveGame(currentGame, shops.getSelectedItems(), platforms.getSelectedItems(), localisations.getSelectedItems());
        }
    }

    public void refreshComments() {
        commentsLayout.removeAll();
        List comments = DataService.getInstance().getCommentsByObject(currentGame.getId(), "gameid");
        for (Object i: comments){
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            Comment comment = (Comment)i;
            String username = DataService.getInstance().getUser(comment.getUserId()).getUsername();
            String text = comment.getText();
            TextArea field = new TextArea("User: " + username);
            field.setWidthFull();
            field.setReadOnly(true);
            field.setValue(text);

            Button delete = new Button("Delete", e -> {
                DataService.getInstance().deleteComment(comment.getId());
                refreshComments();
            });
            delete.setWidth("100px");

            if (AccessControlFactory.getInstance().createAccessControl().getUser().getId().equals(comment.getUserId()) ||
                        AccessControlFactory.getInstance().createAccessControl().isUserInRole(AccessLevel.MODERATOR)) {
                delete.setVisible(true);
                delete.setEnabled(true);
            } else {
                delete.setVisible(false);
                delete.setEnabled(false);
            }

            //horizontalLayout.getStyle().set("background", "#FF00FF");
            horizontalLayout.setSizeFull();
            horizontalLayout.add(field, delete);
            commentsLayout.add(horizontalLayout);
        }
    }

    public  void addCommentCreation() {
        if (AccessControlFactory.getInstance().createAccessControl().getUser() != null) {
            VerticalLayout verticalLayout = new VerticalLayout();
            HorizontalLayout commentButtonsLayout = new HorizontalLayout();
            TextArea textArea = new TextArea("Add comment as " + AccessControlFactory.getInstance().createAccessControl().getUser().getUsername());
            Button confirmButton = new Button("Add comment", e -> {
                DataService.getInstance().addComment(new Comment(AccessControlFactory.getInstance().createAccessControl().getUser().getId(),
                                currentGame.getId(), textArea.getValue().replaceAll("\'", "\"")));
                refreshComments();
                textArea.clear();
            });
            Button discardButton = new Button("Discard", e -> textArea.clear());

            commentButtonsLayout.setSizeFull();
            verticalLayout.setSizeFull();
            commentButtonsLayout.add(confirmButton, discardButton);
            verticalLayout.add(textArea, commentButtonsLayout);
            textArea.setWidth("100%");
            content.add(verticalLayout);
        }
    }

    public void openComments(boolean open) {
        showComments.setVisible(!open);
        showComments.setEnabled(!open);
        hideComments.setEnabled(open);
        hideComments.setVisible(open);
        commentsLayout.setVisible(open);
        commentsLayout.setEnabled(open);
    }

    public void setReadOnly(Boolean read){
        name.setReadOnly(read);
        genre.setReadOnly(read);
        year.setReadOnly(read);
        platforms.setReadOnly(read);
        shops.setReadOnly(read);
        localisations.setReadOnly(read);
        developer.setReadOnly(read);
        distributor.setReadOnly(read);
        description.setReadOnly(read);
    }

    public void setButtonsShow(Boolean show){
        edit.setEnabled(show);
        edit.setVisible(show);
        delete.setVisible(show);
        delete.setEnabled(show);
        save.setVisible(false);
        save.setEnabled(false);
        discard.setVisible(false);
        discard.setEnabled(false);
    }
}
