package lib.games.ui.games;

import com.vaadin.flow.data.provider.ListDataProvider;
import lib.games.backend.DataService;
import lib.games.data.*;
import lib.games.ui.additional.GameXDataprovider;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class GamesDataProvider extends ListDataProvider<Game> {

    private String filterText = "";

    public GamesDataProvider() {
        super(DataService.getInstance().getAllGames());
    }

    public void save(Game game, Set<Shop> shopSet, Set<Platform> platformSet, Set<Localisation> localisationSet) {
        GameXDataprovider gameXDataprovider = new GameXDataprovider();
        gameXDataprovider.deleteGamePlatform("gameid", game.getId());
        gameXDataprovider.deleteGameShop("gameid", game.getId());
        gameXDataprovider.deleteGameLocalisation("gameid", game.getId());

        for (Shop shop: shopSet) {
            gameXDataprovider.saveGameShop(new GameShop(game.getId(), shop.getId()));
        }

        for (Platform platform: platformSet) {
            gameXDataprovider.saveGamePlatform(new GamePlatform(game.getId(), platform.getId()));
        }

        for (Localisation localisation: localisationSet) {
            gameXDataprovider.saveGameLocalisation(new GameLocalisation(game.getId(), localisation.getId()));
        }

        DataService.getInstance().updateGame(game);
    }

    public void delete(Game game) {
        DataService.getInstance().deleteGame(game.getId());
        refreshAll();
    }


    public void setFilter(String filterText) {
        Objects.requireNonNull(filterText, "Filter text cannot be null.");
        if (Objects.equals(this.filterText, filterText.trim())) {
            return;
        }
        this.filterText = filterText.trim().toLowerCase(Locale.ENGLISH);

        setFilter(game -> passesFilter(game.getName(), this.filterText)
                || passesFilter(game.getGenre(), this.filterText)
                || passesFilter(game.getDeveloper(), this.filterText));
    }

    @Override
    public String getId(Game game) {
        Objects.requireNonNull(game,
                "Cannot provide an id for a null product.");

        return game.getId();
    }

    private boolean passesFilter(Object object, String filterText) {
        return object != null && object.toString().toLowerCase(Locale.ENGLISH)
                .contains(filterText);
    }
}
