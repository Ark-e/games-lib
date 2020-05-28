package lib.games.ui.personal;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import lib.games.backend.DataService;
import lib.games.data.Comment;
import lib.games.data.User;


public class CommentList extends Div {

    public CommentList (User user) {
        ListBox<Comment> listBox = new ListBox<>();
        CommentDataProvider commentDataProvider = new CommentDataProvider(user.getId(), "userid");
        listBox.setDataProvider(commentDataProvider);
        listBox.setReadOnly(true);

        listBox.setRenderer(new ComponentRenderer<>(comment -> {
            Div header = new Div();
            header.getStyle().set("font-weight", "bold");
            header.setText("To game: " + DataService.getInstance().getGame(comment.getGameId()).getName());

            Div body = new Div();
            body.setText(comment.getText());
            Div div = new Div(header, body);
            return div;
        }));
        add(listBox);
    }
}
