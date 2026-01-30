package kz.informatics.okulik.nalog_app.home.module;

public class PopularPlace {
    public String id;
    public String title;
    public String subtitle;
    public float rating;
    public int imageRes;
    public String[] tags;
    public int[] listOfGalleryPhotos; // Gallery photos array

    public PopularPlace(String title, String subtitle, float rating, int imageRes, String[] tags) {
        this.id = title;
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = new int[0]; // Empty by default
    }

    public PopularPlace(String id, String title, String subtitle, float rating, int imageRes, String[] tags) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = new int[0]; // Empty by default
    }

    public PopularPlace(String id, String title, String subtitle, float rating, int imageRes, String[] tags, int[] listOfGalleryPhotos) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.rating = rating;
        this.imageRes = imageRes;
        this.tags = tags;
        this.listOfGalleryPhotos = listOfGalleryPhotos != null ? listOfGalleryPhotos : new int[0];
    }
}
