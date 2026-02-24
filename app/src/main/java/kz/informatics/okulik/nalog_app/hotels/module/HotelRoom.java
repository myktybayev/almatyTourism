package kz.informatics.okulik.nalog_app.hotels.module;

/**
 * Room type within a hotel.
 */
public class HotelRoom {
    public final String id;
    public final String name;
    public final int imageRes;
    public final int pricePerNight;
    public final String[] features; // e.g. "42 m²", "King Bed", "Garden View"

    public HotelRoom(String id, String name, int imageRes, int pricePerNight, String[] features) {
        this.id = id;
        this.name = name;
        this.imageRes = imageRes;
        this.pricePerNight = pricePerNight;
        this.features = features != null ? features : new String[0];
    }
}
