package lib.games.ui.platforms;

import com.vaadin.flow.component.grid.Grid;
import lib.games.data.Platform;
import lib.games.data.Shop;

public class PlatformList extends Grid<Platform> {

    public PlatformList() {
        setSizeFull();

        addColumn(Platform::getName).setHeader("Name").setSortable(true).setKey("name");

        setHeightByRows(true);
    }


    public Platform getSelected() {
        return asSingleSelect().getValue();
    }

}
