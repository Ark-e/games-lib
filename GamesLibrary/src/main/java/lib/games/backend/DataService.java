package lib.games.backend;

import lib.games.data.*;
import java.sql.SQLException;
import java.util.List;

public class DataService {

    private static DataService instance;

    public DataService() {

    }

    public synchronized static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public User login(String username, String password) {
        try
        {
            final List list = DatabaseManager.getInstance ().get (
                    "select * from public.users where " +
                            "username = '" + username + "' and " +
                            "password = '" + password + "'", User.class );
            if ( list.size () == 1 )
            {
                final User user = ( User ) list.get ( 0 );
                return user;
            }
        }
        catch ( SQLException e )
        {
            e.printStackTrace ();
        }

        return null;
    }

    public List getAllGames() {
        try {
            return DatabaseManager.getInstance().get("select * from public.games ORDER BY name", Game.class );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Game getGame(String gameId) {
        final List list;
        try {
            list =  DatabaseManager.getInstance().get("select * from public.games where id = '" + gameId+ "' ", Game.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (Game) list.get(0);
    }

    public List getGamesByCompany(String companyId) {
        final List list;
        try {
            list =  DatabaseManager.getInstance().get("select * from public.games where developerid = '" + companyId + "' or distributorid = ' " + companyId + "'", Game.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        return list;
    }



    public void addGame(Game game) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.games(" +
                    "   id, name, icon, description, developerid, distributorid, release, genre)\n"+
                    "VALUES ('"
                    + game.getId() + "', '"
                    + game.getName() + "', '"
                    + game.getIcon() + "', '"
                    + game.getDescription() + "', '"
                    + game.getDeveloper() + "', '"
                    + game.getDistributor() +"', '"
                    + game.getReleaseYear() +"', '"
                    + game.getGenre() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteGame(String gameId) {
        try {
            DatabaseManager.getInstance().executeUpdate("DELETE FROM public.games where id ='"+ gameId +"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateGame(Game game) {
        try {
            DatabaseManager.getInstance().executeUpdate("UPDATE public.games " +
                    "set "
                    + "name = '" + game.getName() + "', "
                    + "icon = '" + game.getIcon() + "', "
                    + "description = '" + game.getDescription() + "', "
                    + "developerid = '" + game.getDeveloper() + "', "
                    + "distributorid = '" + game.getDistributor() + "', "
                    + "release = '" + game.getReleaseYear() + "', "
                    + "genre = '" + game.getGenre() + "' "
                    + "where id = '" + game.getId()+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List getAllCompanies() {
        try {
            return DatabaseManager.getInstance().get("select * from public.companies ORDER BY name", Company.class );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Company getCompany(String companyId) {
        final List list;
        try {
            list =  DatabaseManager.getInstance().get("select * from public.companies where id = '" + companyId+ "'", Company.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (Company) list.get(0);
    }


    public void addCompany(Company company) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.companies(" +
                    "   id, name, countryid, foundation)\n"+
                    "VALUES ('"
                    + company.getId() + "', '"
                    + company.getName() + "', '"
                    + company.getCountry() + "', '"
                    + company.getFoundationYear() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteCompany(String companyId) {
        try {
            DatabaseManager.getInstance().executeUpdate("DELETE FROM public.companies where id ='"+ companyId +"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateCompany(Company company) {
        try {
            DatabaseManager.getInstance().executeUpdate("UPDATE public.companies " +
                    "set "
                    + "name = '" + company.getName() + "', "
                    + "foundation = '" + company.getFoundationYear() + "' "
                    + "where id = '" + company.getId()+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public User getUser(String name) {
        final List list;
        try {
            list =  DatabaseManager.getInstance().get("select * from public.users where id = '" + name+ "'", User.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (User) list.get(0);
    }


    public void addUser(User user) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.users(" +
                    "   id, username, realname, password, email, access)\n"+
                    "VALUES ('"
                    + user.getId() + "', '"
                    + user.getUsername() + "', '"
                    + user.getFullName() + "', '"
                    + user.getPassword() + "', '"
                    + user.getEmail() + "', '"
                    + user.getAccessLevel().getId() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List getCommentsByObject(String name, String objectName) {

        try {
            return DatabaseManager.getInstance().get("SELECT * FROM public.comments\n" +
                    "where " + objectName + " = '" + name + "'", Comment.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateCommentsByUser(String userId) {
        try {
            DatabaseManager.getInstance().executeUpdate("UPDATE public.comments " +
                    "set "
                    + "userid = 'DELETED' "
                    + "where userid = '" + userId+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addComment(Comment comment) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.comments(" +
                    "   id, userid, gameid, text)\n"+
                    "VALUES ('"
                    + comment.getId() + "', '"
                    + comment.getUserId() + "', '"
                    + comment.getGameId() + "', '"
                    + comment.getText() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateComment(Comment comment) {
        try {
            DatabaseManager.getInstance().executeUpdate("UPDATE public.comments " +
                    "set "
                    + "text = '" + comment.getText() + "' "
                    + "where id = '" + comment.getId()+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteComment(String commentId) {
        try {
            DatabaseManager.getInstance().executeUpdate("DELETE FROM public.comments where id ='"+ commentId +"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCommentsByObject(String type, String id) {
        try {
            DatabaseManager.getInstance().executeUpdate("DELETE FROM public.comments where " + type + " ='"+ id +"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List getAllUsers() {
        try {
            return DatabaseManager.getInstance().get("SELECT * FROM public.users\n WHERE id<>'DELETED'\n ORDER BY access;", User.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateUser(User user) {
        try {
            DatabaseManager.getInstance().executeUpdate("UPDATE public.users " +
                    "set "
                    + "username = '" + user.getUsername() + "', "
                    + "realname = '" + user.getFullName() + "', "
                    + "password = '" + user.getPassword() + "', "
                    + "email = '" + user.getEmail() + "', "
                    + "access = '" + user.getAccessLevel().getId() + "' "
                    + "where id = '" + user.getId()+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteUser(String userId) {
        try {
            DatabaseManager.getInstance().executeUpdate("DELETE FROM public.users where id ='"+ userId +"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Platform getPlatform(String id) {
        final List list;
        try {
            list =  DatabaseManager.getInstance().get("select * from public.platforms where id = '" + id+ "'", Platform.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (Platform) list.get(0);
    }


    public List getAllPlatforms() {
        try {
            return DatabaseManager.getInstance().get("select * from public.platforms ORDER BY name", Platform.class );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void addPlatform(Platform platform) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.platforms(" +
                    "   id, name)\n"+
                    "VALUES ('"
                    + platform.getId() + "', '"
                    + platform.getName() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deletePlatform(String id) {
        try {
            DatabaseManager.getInstance().executeUpdate("DELETE FROM public.platforms where id ='"+ id +"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updatePlatform(Platform platform) {
        try {
            DatabaseManager.getInstance().executeUpdate("UPDATE public.platforms " +
                    "set "
                    + "name = '" + platform.getName() + "' "
                    + "where id = '" + platform.getId()+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Shop getShop(String id) {
        final List list;
        try {
            list =  DatabaseManager.getInstance().get("select * from public.shops where id = '" + id+ "'", Shop.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (Shop) list.get(0);
    }


    public List getAllShops() {
        try {
            return DatabaseManager.getInstance().get("select * from public.shops ORDER BY name", Shop.class );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void addShop(Shop shop) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.shops(" +
                    "   id, name, url)\n"+
                    "VALUES ('"
                    + shop.getId() + "', '"
                    + shop.getName() + "', '"
                    + shop.getUrl() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteShop(String id) {
        try {
            DatabaseManager.getInstance().executeUpdate("DELETE FROM public.shops where id ='"+ id +"';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateShop(Shop shop) {
        try {
            DatabaseManager.getInstance().executeUpdate("UPDATE public.shops " +
                    "set "
                    + "name = '" + shop.getName() + "', "
                    + "url = '" + shop.getUrl() + "' "
                    + "where id = '" + shop.getId()+ "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List getGamePlatformsBy(String objectName, String id) {
        try {
            return DatabaseManager.getInstance().get("SELECT * FROM public.gameplatforms\n" +
                    "where " + objectName + " = '" + id + "'", GamePlatform.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteGamePlatform(String idType, String id) {
        try {
            DatabaseManager.getInstance().executeUpdate("Delete FROM public.gameplatforms\n" +
                    "where " + idType + " = '" + id + "'");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addGamePlatform(GamePlatform gamePlatform) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.gameplatforms(" +
                    "   id, gameid, platformid)\n"+
                    "VALUES ('"
                    + gamePlatform.getId() + "', '"
                    + gamePlatform.getGameId() + "', '"
                    + gamePlatform.getPlatformId() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List getGameShopsBy(String objectName, String id) {
        try {
            return DatabaseManager.getInstance().get("SELECT * FROM public.gameshops\n" +
                    "where " + objectName + " = '" + id + "'", GameShop.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void deleteGameShop(String idType, String id) {
        try {
            DatabaseManager.getInstance().executeUpdate("Delete FROM public.gameshops\n" +
                    "where " + idType + " = '" + id + "'");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addGameShop(GameShop gameShop) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.gameshops(" +
                    "   id, gameid, shopid)\n"+
                    "VALUES ('"
                    + gameShop.getId() + "', '"
                    + gameShop.getGameId() + "', '"
                    + gameShop.getShopId() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List getGameLocalisationsBy(String objectName, String id) {
        try {
            return DatabaseManager.getInstance().get("SELECT * FROM public.gamelocalisations\n" +
                    "where " + objectName + " = '" + id + "'", GameLocalisation.class);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void deleteGameLocalisation(String idType, String id) {
        try {
            DatabaseManager.getInstance().executeUpdate("Delete FROM public.gamelocalisations\n" +
                    "where " + idType + " = '" + id + "'");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void addGameLocalisation(GameLocalisation gameLocalisation) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.gamelocalisations(" +
                    "   id, gameid, localisationid)\n"+
                    "VALUES ('"
                    + gameLocalisation.getId() + "', '"
                    + gameLocalisation.getGameId() + "', '"
                    + gameLocalisation.getLocalisationId() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List getAllLocalisations() {
        try {
            return DatabaseManager.getInstance().get("select * from public.localisations ORDER BY locale", Localisation.class );
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Localisation getLocalisations(String id) {
        List list;
        try {
            list =  DatabaseManager.getInstance().get("select * from public.localisations where id ='"
                    + id +"';", Localisation.class );
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        if (list.isEmpty()) {
            return null;
        }
        return (Localisation) list.get(0);
    }

    public void addLocalisation(Localisation localisation) {
        try {
            DatabaseManager.getInstance().execute("INSERT INTO public.localisations(" +
                    "   id, locale)\n"+
                    "VALUES ('"
                    + localisation.getId() + "', '"
                    + localisation.getLocale() +"');");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLocalisation(String id) {
        try {
            DatabaseManager.getInstance().executeUpdate("Delete FROM public.localisations\n" +
                    "where id = '" + id + "'");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
