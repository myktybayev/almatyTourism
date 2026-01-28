package kz.informatics.okulik.nalog_app.home.module;

public class PopularPlace {
    public String id;
    public String title;
    public String subtitle;
    public float rating;
    public int imageRes;
    public String[] tags;

    public PopularPlace(String title, String subtitle, float rating, int imageRes, String[] tags) {
        this.id = title;
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
    }

    public PopularPlace(String id, String title, String subtitle, float rating, int imageRes, String[] tags) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
    }
}
