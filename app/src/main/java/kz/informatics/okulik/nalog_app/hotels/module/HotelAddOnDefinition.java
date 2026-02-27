package kz.informatics.okulik.nalog_app.hotels.module;

/**
 * Add-on definition with resource IDs. Resolved to HotelAddOn via HotelsRepository.
 */
public class HotelAddOnDefinition {
    public final String id;
    public final int nameResId;
    public final int iconRes;
    public final int price;
    public final boolean perGuest;

    public HotelAddOnDefinition(String id, int nameResId, int iconRes, int price, boolean perGuest) {
        this.id = id;
        this.nameResId = nameResId;
        this.iconRes = iconRes;
        this.price = price;
        this.perGuest = perGuest;
    }
}
