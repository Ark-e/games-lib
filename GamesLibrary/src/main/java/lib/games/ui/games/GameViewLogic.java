package lib.games.ui.games;

import com.vaadin.flow.component.UI;
import lib.games.authentication.AccessControl;
import lib.games.authentication.AccessControlFactory;
import lib.games.backend.DataService;
import lib.games.data.*;
import lib.games.ui.additional.GameXDataprovider;

import java.util.Set;

public class GameViewLogic {

    private final GamesView view;

    public GameViewLogic(GamesView simpleCrudView) {
        view = simpleCrudView;
    }


    public void init() {
    }

    public void cancelGame() {
        setFragmentParameter("");
        view.clearSelection();
    }


    private void setFragmentParameter(String gameId) {
        String fragmentParameter;
        if (gameId == null || gameId.isEmpty()) {
            fragmentParameter = "";
        } else {
            fragmentParameter = gameId;
        }

        UI.getCurrent().navigate(GamesView.class, fragmentParameter);
    }


    public void enter(String gameId) {
        if (gameId != null && !gameId.isEmpty()) {
            try {
                final Game game = findGame(gameId);
                view.selectRow(game);
            } catch (final NumberFormatException e) {
            }
        } else {
            view.showForm(false);
        }
    }

    private Game findGame(String gameId) {
        return DataService.getInstance().getGame(gameId);
    }


    public void saveGame(Game game, Set<Shop> shopSet, Set<Platform> platformSet, Set<Localisation> localisationSet) {
        view.clearSelection();
        view.updateGame(game, shopSet, platformSet, localisationSet);
        setFragmentParameter("");
    }

    public void deleteGame(Game game) {
        view.clearSelection();
        GameXDataprovider gameXDataprovider = new GameXDataprovider();
        gameXDataprovider.deleteGameLocalisation("gameid", game.getId());
        gameXDataprovider.deleteGameShop("gameid", game.getId());
        gameXDataprovider.deleteGamePlatform("gameid", game.getId());
        DataService.getInstance().deleteCommentsByObject("gameid", game.getId());
        view.removeGame(game);
        setFragmentParameter("");
    }


    public void editGame(Game game) {
        if (game == null) {
            setFragmentParameter("");
        } else {
            setFragmentParameter(game.getId() + "");
        }
        view.editGame(game);
    }


    public void rowSelected(Game game) {
        editGame(game);
    }
}
