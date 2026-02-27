package kz.informatics.okulik.nalog_app.hotels.module;

/**
 * Room definition with resource IDs. Resolved to HotelRoom via HotelsRepository.
 * featureResIds and featureFormatArgs have same length; if featureFormatArgs[i] != null,
 * use getString(featureResIds[i], featureFormatArgs[i]) for format (e.g. room_sqm_format).
 */
public class HotelRoomDefinition {
    public final String id;
    public final int nameResId;
    public final int imageRes;
    public final int pricePerNight;
    public final int[] featureResIds;
    public final String[] featureFormatArgs;

    public HotelRoomDefinition(String id, int nameResId, int imageRes, int pricePerNight,
                               int[] featureResIds, String[] featureFormatArgs) {
        this.id = id;
        this.nameResId = nameResId;
        this.imageRes = imageRes;
        this.pricePerNight = pricePerNight;
        this.featureResIds = featureResIds != null ? featureResIds : new int[0];
        this.featureFormatArgs = featureFormatArgs != null ? featureFormatArgs : new String[0];
    }
}
