package kz.informatics.okulik.nalog_app.home.module;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import kz.informatics.okulik.R;
import kz.informatics.okulik.nalog_app.home.activities.BrowseActivityByCategories;

/**
 * Single source of all destinations. Add new places here.
 * ExploreFragment & PopularDestinationsActivity: category "all"
 * BrowseActivityByCategories: filter by citylife, nature, parks, spiritual
 */
public class DestinationsRepository {

    private static DestinationsRepository instance;
    private final List<PlaceDefinition> definitions = new ArrayList<>();

    public static DestinationsRepository getInstance() {
        if (instance == null) {
            instance = new DestinationsRepository();
        }
        return instance;
    }

    private DestinationsRepository() {
        initDefinitions();
    }

    private void initDefinitions() {
        definitions.clear();

        // place 1 - Shymbulak
        definitions.add(new PlaceDefinition(
                "1",
                R.string.place_1_title, R.string.place_1_subtitle, R.string.place_1_about,
                4.9f, R.drawable.header_shymbulak,
                new int[]{R.string.tag_skiing, R.string.tag_hiking, R.string.tag_nature},
                new int[]{R.drawable.img_shymbulak1, R.drawable.img_shymbulak2, R.drawable.img_shymbulak6, R.drawable.img_shymbulak3, R.drawable.img_shymbulak4, R.drawable.img_shymbulak5},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE}));

        // place 2 - Kok Tobe
        definitions.add(new PlaceDefinition(
                "2",
                R.string.place_2_title, R.string.place_2_subtitle, R.string.place_2_about,
                4.8f, R.drawable.header_koktobe,
                new int[]{R.string.tag_cable_car, R.string.tag_city_views},
                new int[]{R.drawable.img_koktobe1, R.drawable.img_koktobe2, R.drawable.img_koktobe3, R.drawable.img_koktobe4, R.drawable.img_koktobe5, R.drawable.img_koktobe6},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE}));

        // place 3 - Big Almaty Lake
        definitions.add(new PlaceDefinition(
                "3",
                R.string.place_3_title, R.string.place_3_subtitle, R.string.place_3_about,
                4.9f, R.drawable.header_bigalmaty_lake,
                new int[]{R.string.tag_lake, R.string.tag_photography},
                new int[]{R.drawable.img_almatylake1, R.drawable.img_almatylake2, R.drawable.img_almatylake3, R.drawable.img_almatylake5, R.drawable.img_almatylake6, R.drawable.img_almatylake4},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE}));

        // place 4 - Charyn Canyon
        definitions.add(new PlaceDefinition(
                "4",
                R.string.place_4_title, R.string.place_4_subtitle, R.string.place_4_about,
                4.8f, R.drawable.header_charyn_canyon,
                new int[]{R.string.tag_hiking, R.string.tag_adventure},
                new int[]{R.drawable.img_charyn1, R.drawable.img_charyn2, R.drawable.img_charyn3, R.drawable.img_charyn4, R.drawable.img_charyn5, R.drawable.img_charyn6},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE}));

        // place 5 - Central Mosque
        definitions.add(new PlaceDefinition(
                "5",
                R.string.place_5_title, R.string.place_5_subtitle, R.string.place_5_about,
                4.7f, R.drawable.header_central_mosque,
                new int[]{R.string.tag_architecture, R.string.tag_history},
                new int[]{R.drawable.img_central_mosque4, R.drawable.img_central_mosque2, R.drawable.img_central_mosque6, R.drawable.img_central_mosque1, R.drawable.img_central_mosque3, R.drawable.img_central_mosque5},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_SPIRITUAL}));

        // place 6 - Medeu
        definitions.add(new PlaceDefinition(
                "6",
                R.string.place_6_title, R.string.place_6_subtitle, R.string.place_6_about,
                4.8f, R.drawable.header_medeu,
                new int[]{R.string.tag_ice_skating, R.string.tag_sports},
                new int[]{R.drawable.img_medeu1, R.drawable.img_medeu2, R.drawable.img_medeu3, R.drawable.img_medeu5, R.drawable.img_medeu6, R.drawable.img_medeu4},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE}));

        // place 7 - Green Bazaar
        definitions.add(new PlaceDefinition(
                "7",
                R.string.place_7_title, R.string.place_7_subtitle, R.string.place_7_about,
                4.6f, R.drawable.header_green_bazaar,
                new int[]{R.string.tag_food, R.string.tag_culture},
                new int[]{R.drawable.img_green_bazaar6, R.drawable.img_green_bazaar1, R.drawable.img_green_bazaar4, R.drawable.img_green_bazaar5, R.drawable.img_green_bazaar3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE}));

        // place 8 - Zenkov Cathedral (CityLife only)
        definitions.add(new PlaceDefinition(
                "8",
                R.string.place_8_title, R.string.place_8_subtitle, R.string.place_8_about,
                4.9f, R.drawable.header_central_mosque,
                new int[]{R.string.tag_photography, R.string.tag_history},
                new int[]{R.drawable.img_central_mosque1, R.drawable.img_central_mosque2, R.drawable.img_central_mosque3},
                new String[]{BrowseActivityByCategories.CATEGORY_CITYLIFE}));

        // place 9 - Central State Museum (CityLife only)
        definitions.add(new PlaceDefinition(
                "9",
                R.string.place_9_title, R.string.place_9_subtitle, R.string.place_9_about,
                4.6f, R.drawable.header_koktobe,
                new int[]{R.string.tag_culture, R.string.tag_exhibits},
                new int[]{R.drawable.img_koktobe1, R.drawable.img_koktobe2, R.drawable.img_koktobe3},
                new String[]{BrowseActivityByCategories.CATEGORY_CITYLIFE}));
    }

    /** For ExploreFragment and PopularDestinationsActivity - places with category "all" */
    public List<PopularPlace> getPlacesForExplore(Context context) {
        return getPlacesByCategory(context, "all");
    }

    /** For BrowseActivityByCategories - filter by category */
    public List<PopularPlace> getPlacesByCategory(Context context, String category) {
        List<PopularPlace> result = new ArrayList<>();
        for (PlaceDefinition def : definitions) {
            if (def.hasCategory(category)) {
                result.add(buildPlace(context, def));
            }
        }
        return result;
    }

    private PopularPlace buildPlace(Context context, PlaceDefinition def) {
        String[] tags = new String[def.tagResIds.length];
        for (int i = 0; i < def.tagResIds.length; i++) {
            tags[i] = context.getString(def.tagResIds[i]);
        }
        String title = context.getString(def.titleResId);
        String subtitle = context.getString(def.subtitleResId);
        String about = context.getString(def.aboutResId);
        return new PopularPlace(
                def.id,
                title,
                subtitle,
                about,
                def.rating,
                def.imageRes,
                tags,
                def.galleryResIds,
                def.categories
        );
    }
}
