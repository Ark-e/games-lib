package lib.games.ui.platforms;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lib.games.data.Platform;
import lib.games.ui.additional.PlatformDataProvider;

public class EditPlatformDialog extends Dialog {

    Platform platform;

    public EditPlatformDialog(PlatformDataProvider platformDataProvider, Platform demoPlatform) {
        VerticalLayout verticalLayout = new VerticalLayout();

        Label info = new Label("Enter platform name!");
        info.setVisible(false);
        TextField name = new TextField("Name:");

        platform = demoPlatform;

        if (platform != null) {
            name.setValue(platform.getName());
        }

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button confirmButton = new Button("Confirm", event -> {
            if(name.getValue().isEmpty()) {
                info.setVisible(true);
            }
            else {
                if (platform == null) {
                    platform = new Platform(name.getValue());
                    platformDataProvider.saveNew(platform);
                    close();
                    UI.getCurrent().getPage().reload();
                }
                else {
                    platform.setName(name.getValue());
                    platformDataProvider.save(platform);
                    close();
                    platformDataProvider.refreshAll();
                }
            }
        });

        Button cancelButton = new Button("Cancel", event -> {
            close();
        });

        horizontalLayout.add(confirmButton, cancelButton);

        verticalLayout.add(info, name, horizontalLayout);
        add(verticalLayout);
    }
}
