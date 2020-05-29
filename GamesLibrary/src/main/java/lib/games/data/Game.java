package lib.games.data;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Объект, описывающий игру.
 */
public class Game implements Serializable {
    /**
     * @param id - уникальный идентификатор
     * @param name - название
     * @param icon - первое изображение
     * @param developer - разработчик
     * @param distributor - издатель
     * @param releaseYear - год выхода
     * @param description - текстовое описание
     * @param localisations - список организаций, занимавшихся локализацией, с указанием языков.
     * @param platformShopList - Список магазинов, в которых возможно приобрести игру для конкретной платформы.
     */
    private final String id;
    private String name;
    private String icon;
    private String developer;
    private String distributor;
    private String releaseYear;
    private String description;
    private String genre;

    public Game(String name, String icon, String developer, String distributor, String releaseYear, String genre) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.icon = icon;
        this.developer = developer;
        this.distributor = distributor;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public Game(String id, String name, String icon, String developer, String distributor, String releaseYear, String genre) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.developer = developer;
        this.distributor = distributor;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
