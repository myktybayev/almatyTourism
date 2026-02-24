package kz.informatics.okulik.nalog_app.hotels.module;

/**
 * Optional add-on service (e.g. breakfast, airport transfer).
 */
public class HotelAddOn {
    public final String id;
    public final String name;
    public final int iconRes;
    public final int price;
    /** If true, price is per guest. */
    public final boolean perGuest;

    public HotelAddOn(String id, String name, int iconRes, int price, boolean perGuest) {
        this.id = id;
        this.name = name;
        this.iconRes = iconRes;
        this.price = price;
        this.perGuest = perGuest;
    }
}
