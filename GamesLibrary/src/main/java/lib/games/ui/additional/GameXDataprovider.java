package lib.games.ui.additional;

import lib.games.backend.DataService;
import lib.games.data.Game;
import lib.games.data.GameLocalisation;
import lib.games.data.GamePlatform;
import lib.games.data.GameShop;

import java.util.List;

public class GameXDataprovider {

    public GameXDataprovider() {

    }

    public void saveGamePlatform(GamePlatform gamePlatform) {
        DataService.getInstance().addGamePlatform(gamePlatform);
    }

    public void deleteGamePlatform(String type, String id) {
        DataService.getInstance().deleteGamePlatform(type, id);
    }

    public List getGamePlatformBy(String type, String id) {
        return DataService.getInstance().getGamePlatformsBy(type, id);
    }

    public void saveGameShop(GameShop gameShop) {
        DataService.getInstance().addGameShop(gameShop);
    }

    public void deleteGameShop(String type, String id) {
        DataService.getInstance().deleteGameShop(type, id);
    }

    public List getGameShopBy(String type, String id) {
        return DataService.getInstance().getGameShopsBy(type, id);
    }

    public void saveGameLocalisation(GameLocalisation gameLocalisation) {
        DataService.getInstance().addGameLocalisation(gameLocalisation);
    }

    public void deleteGameLocalisation(String type, String id) {
        DataService.getInstance().deleteGameLocalisation(type, id);
    }

    public List getGameLocalisationBy(String type, String id) {
        return DataService.getInstance().getGameLocalisationsBy(type, id);
    }

}
