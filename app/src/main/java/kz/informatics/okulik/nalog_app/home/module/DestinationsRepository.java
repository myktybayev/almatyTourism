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

        // CATEGORY_NATURE
        definitions.add(new PlaceDefinition(
                "1",
                R.string.place_1_title, R.string.place_1_subtitle, R.string.place_1_about,
                4.9f, R.drawable.header_shymbulak,
                new int[]{R.string.tag_skiing, R.string.tag_hiking, R.string.tag_nature},
                new int[]{R.drawable.img_shymbulak1, R.drawable.img_shymbulak2, R.drawable.img_shymbulak6, R.drawable.img_shymbulak3, R.drawable.img_shymbulak4, R.drawable.img_shymbulak5},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"mountains"}, "43.11978,77.07094"));

        definitions.add(new PlaceDefinition(
                "2",
                R.string.place_2_title, R.string.place_2_subtitle, R.string.place_2_about,
                4.8f, R.drawable.header_koktobe,
                new int[]{R.string.tag_cable_car, R.string.tag_city_views},
                new int[]{R.drawable.img_koktobe1, R.drawable.img_koktobe2, R.drawable.img_koktobe3, R.drawable.img_koktobe4, R.drawable.img_koktobe5, R.drawable.img_koktobe6},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE, BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"mountains"}, "43.233056,76.976111"));

        definitions.add(new PlaceDefinition(
                "3",
                R.string.place_3_title, R.string.place_3_subtitle, R.string.place_3_about,
                4.9f, R.drawable.header_bigalmaty_lake,
                new int[]{R.string.tag_lake, R.string.tag_photography},
                new int[]{R.drawable.img_almatylake1, R.drawable.img_almatylake2, R.drawable.img_almatylake3, R.drawable.img_almatylake5, R.drawable.img_almatylake6, R.drawable.img_almatylake4},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"lakes"}, "43.052173,76.983940"));

        definitions.add(new PlaceDefinition(
                "4",
                R.string.place_4_title, R.string.place_4_subtitle, R.string.place_4_about,
                4.8f, R.drawable.header_charyn_canyon,
                new int[]{R.string.tag_hiking, R.string.tag_adventure},
                new int[]{R.drawable.img_charyn1, R.drawable.img_charyn2, R.drawable.img_charyn3, R.drawable.img_charyn4, R.drawable.img_charyn5, R.drawable.img_charyn6},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"canyons"}, "43.379167,79.123333"));

        definitions.add(new PlaceDefinition(
                "6",
                R.string.place_6_title, R.string.place_6_subtitle, R.string.place_6_about,
                4.8f, R.drawable.header_medeu,
                new int[]{R.string.tag_ice_skating, R.string.tag_sports},
                new int[]{R.drawable.img_medeu1, R.drawable.img_medeu2, R.drawable.img_medeu3, R.drawable.img_medeu5, R.drawable.img_medeu6, R.drawable.img_medeu4},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"mountains"}, "43.166667,77.033333"));

        definitions.add(new PlaceDefinition(
                "14",
                R.string.place_14_title, R.string.place_14_subtitle, R.string.place_14_about,
                4.8f, R.drawable.header_issyk,
                new int[]{R.string.tag_lake, R.string.tag_picnic, R.string.tag_nature},
                new int[]{R.drawable.img_issyk1, R.drawable.img_issyk2, R.drawable.img_issyk3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"lakes"}, "43.253056,77.484722"));

        definitions.add(new PlaceDefinition(
                "15",
                R.string.place_15_title, R.string.place_15_subtitle, R.string.place_15_about,
                4.6f, R.drawable.header_kapchagay,
                new int[]{R.string.tag_beach, R.string.tag_water, R.string.tag_lake},
                new int[]{R.drawable.img_kapchagay1, R.drawable.img_kapchagay2, R.drawable.img_kapchagay3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"lakes"}, "43.816667,77.483333"));

        definitions.add(new PlaceDefinition(
                "17",
                R.string.place_17_title, R.string.place_17_subtitle, R.string.place_17_about,
                4.8f, R.drawable.header_turgen,
                new int[]{R.string.tag_waterfall, R.string.tag_hiking, R.string.tag_nature},
                new int[]{R.drawable.img_turgen1, R.drawable.img_turgen2, R.drawable.img_turgen3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"canyons"}, "43.260000,77.970000"));

        definitions.add(new PlaceDefinition(
                "18",
                R.string.place_18_title, R.string.place_18_subtitle, R.string.place_18_about,
                4.7f, R.drawable.header_tamgaly,
                new int[]{R.string.tag_petroglyphs, R.string.tag_culture, R.string.tag_nature},
                new int[]{R.drawable.img_tamgaly1, R.drawable.img_tamgaly2, R.drawable.img_tamgaly3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_NATURE},
                new String[]{"canyons"}, "43.800000,75.533333"));


        // CATEGORY_CITYLIFE
        definitions.add(new PlaceDefinition(
                "7",
                R.string.place_7_title, R.string.place_7_subtitle, R.string.place_7_about,
                4.6f, R.drawable.header_green_bazaar,
                new int[]{R.string.tag_food, R.string.tag_culture},
                new int[]{R.drawable.img_green_bazaar6, R.drawable.img_green_bazaar1, R.drawable.img_green_bazaar4, R.drawable.img_green_bazaar5, R.drawable.img_green_bazaar3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"shopping"}, "43.26440966403416, 76.95481496988478"));

        definitions.add(new PlaceDefinition(
                "102",
                R.string.place_102_title, R.string.place_102_subtitle, R.string.place_102_about,
                4.7f, R.drawable.header_esentai,
                new int[]{R.string.tag_shopping, R.string.tag_luxury, R.string.tag_entertainment},
                new int[]{R.drawable.img_esentai1, R.drawable.img_esentai2, R.drawable.img_esentai3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"shopping"}, "43.21860168057112, 76.9276774257024"));

        definitions.add(new PlaceDefinition(
                "103",
                R.string.place_103_title, R.string.place_103_subtitle, R.string.place_103_about,
                4.6f, R.drawable.header_dostyk,
                new int[]{R.string.tag_mall, R.string.tag_cinema, R.string.tag_food},
                new int[]{R.drawable.img_dostyk1, R.drawable.img_dostyk2, R.drawable.img_dostyk3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"shopping"}, "43.233565238351844, 76.95685178337517"));

        definitions.add(new PlaceDefinition(
                "104",
                R.string.place_104_title, R.string.place_104_subtitle, R.string.place_104_about,
                4.7f, R.drawable.header_mega,
                new int[]{R.string.tag_mall, R.string.tag_family, R.string.tag_entertainment},
                new int[]{R.drawable.img_mega1, R.drawable.img_mega2, R.drawable.img_mega3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"malls"}, "43.20374552171364, 76.89313242861593"));

        definitions.add(new PlaceDefinition(
                "105",
                R.string.place_105_title, R.string.place_105_subtitle, R.string.place_105_about,
                4.6f, R.drawable.header_forum,
                new int[]{R.string.tag_mall, R.string.tag_modern, R.string.tag_shopping},
                new int[]{R.drawable.img_forum1, R.drawable.img_forum2, R.drawable.img_forum3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"malls"}, "43.23420137039341, 76.93578416803099"));

        definitions.add(new PlaceDefinition(
                "106",
                R.string.place_106_title, R.string.place_106_subtitle, R.string.place_106_about,
                4.4f, R.drawable.header_asiapark,
                new int[]{R.string.tag_mall, R.string.tag_shopping, R.string.tag_family},
                new int[]{R.drawable.img_asia1, R.drawable.img_asia2, R.drawable.img_asia3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"malls"}, "43.244564507310116, 76.83589062755604"));

        definitions.add(new PlaceDefinition(
                "107",
                R.string.place_107_title, R.string.place_107_subtitle, R.string.place_107_about,
                4.8f, R.drawable.header_arena,
                new int[]{R.string.tag_sports, R.string.tag_events, R.string.tag_entertainment},
                new int[]{R.drawable.img_arena1, R.drawable.img_arena2, R.drawable.img_arena3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"entertainment"}, "43.26377659187725, 76.81321533069324"));

        definitions.add(new PlaceDefinition(
                "108",
                R.string.place_108_title, R.string.place_108_subtitle, R.string.place_108_about,
                4.7f, R.drawable.header_palace,
                new int[]{R.string.tag_concerts, R.string.tag_culture, R.string.tag_events},
                new int[]{R.drawable.img_palace1, R.drawable.img_palace2, R.drawable.img_palace3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"entertainment"}, "43.24330097578393, 76.95935071391163"));

        definitions.add(new PlaceDefinition(
                "109",
                R.string.place_109_title, R.string.place_109_subtitle, R.string.place_109_about,
                4.9f, R.drawable.header_auezov,
                new int[]{R.string.tag_theatre, R.string.tag_culture, R.string.tag_history},
                new int[]{R.drawable.img_theatre1, R.drawable.img_theatre2, R.drawable.img_theatre3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_CITYLIFE},
                new String[]{"entertainment"}, "43.24145863763653, 76.91793628522794"));


        // CATEGORY_PARKS
        definitions.add(new PlaceDefinition(
                "19",
                R.string.place_19_title, R.string.place_19_subtitle, R.string.place_19_about,
                4.8f, R.drawable.header_panfilov,
                new int[]{R.string.tag_history, R.string.tag_walks, R.string.tag_citypark},
                new int[]{R.drawable.img_panfilov1, R.drawable.img_panfilov2, R.drawable.img_panfilov3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"city_parks"}, "43.258966099448834, 76.95416378337637"));

        definitions.add(new PlaceDefinition(
                "20",
                R.string.place_20_title, R.string.place_20_subtitle, R.string.place_20_about,
                4.7f, R.drawable.header_first_president_park,
                new int[]{R.string.tag_fountain, R.string.tag_walks, R.string.tag_citypark},
                new int[]{R.drawable.img_firstpark1, R.drawable.img_firstpark2, R.drawable.img_firstpark3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"city_parks"}, "43.193777817309815, 76.88681636265045"));

        definitions.add(new PlaceDefinition(
                "21",
                R.string.place_21_title, R.string.place_21_subtitle, R.string.place_21_about,
                4.6f, R.drawable.header_central_park,
                new int[]{R.string.tag_lake, R.string.tag_attractions, R.string.tag_family},
                new int[]{R.drawable.img_centralpark1, R.drawable.img_centralpark2, R.drawable.img_centralpark3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"city_parks"}, "43.26147611137244, 76.96520819495602"));

        definitions.add(new PlaceDefinition(
                "22",
                R.string.place_22_title, R.string.place_22_subtitle, R.string.place_22_about,
                4.7f, R.drawable.header_botanical_garden,
                new int[]{R.string.tag_botanical, R.string.tag_nature, R.string.tag_walks},
                new int[]{R.drawable.img_botanical1, R.drawable.img_botanical2, R.drawable.img_botanical3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"botanical"}, "43.2196742,76.9107654"));

        definitions.add(new PlaceDefinition(
                "23",
                R.string.place_23_title, R.string.place_23_subtitle, R.string.place_23_about,
                4.6f, R.drawable.header_japanese_garden,
                new int[]{R.string.tag_garden, R.string.tag_relax, R.string.tag_botanical},
                new int[]{R.drawable.img_japan1, R.drawable.img_japan2, R.drawable.img_japan3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"botanical"}, "43.1850658,76.8840397"));

        definitions.add(new PlaceDefinition(
                "24",
                R.string.place_24_title, R.string.place_24_subtitle, R.string.place_24_about,
                4.5f, R.drawable.header_baum_grove,
                new int[]{R.string.tag_forest, R.string.tag_nature, R.string.tag_walks},
                new int[]{R.drawable.img_baum1, R.drawable.img_baum2, R.drawable.img_baum3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"botanical"}, "43.31037363819219, 76.95007937148087"));

        definitions.add(new PlaceDefinition(
                "25",
                R.string.place_25_title, R.string.place_25_subtitle, R.string.place_25_about,
                4.6f, R.drawable.header_fantasy_world,
                new int[]{R.string.tag_amusement, R.string.tag_family, R.string.tag_entertainment},
                new int[]{R.drawable.img_fantasy1, R.drawable.img_fantasy2, R.drawable.img_fantasy3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"family"}, "43.240102319903905, 76.91872312570337"));

        definitions.add(new PlaceDefinition(
                "26",
                R.string.place_26_title, R.string.place_26_subtitle, R.string.place_26_about,
                4.5f, R.drawable.header_zoo,
                new int[]{R.string.tag_animals, R.string.tag_family, R.string.tag_education},
                new int[]{R.drawable.img_zoo1, R.drawable.img_zoo2, R.drawable.img_zoo3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"family"}, "43.26378509921485, 76.97630446988478"));

        definitions.add(new PlaceDefinition(
                "27",
                R.string.place_27_title, R.string.place_27_subtitle, R.string.place_27_about,
                4.7f, R.drawable.header_halyk_arena,
                new int[]{R.string.tag_sports, R.string.tag_events, R.string.tag_family},
                new int[]{R.drawable.img_halyk1, R.drawable.img_halyk2, R.drawable.img_halyk3},
                new String[]{BrowseActivityByCategories.CATEGORY_PARKS},
                new String[]{"family"}, "43.28740694717575, 76.99034211221377"));

        // CATEGORY_SPIRITUAL
        definitions.add(new PlaceDefinition(
                "5",
                R.string.place_5_title, R.string.place_5_subtitle, R.string.place_5_about,
                4.7f, R.drawable.header_central_mosque,
                new int[]{R.string.tag_architecture, R.string.tag_history},
                new int[]{R.drawable.img_central_mosque4, R.drawable.img_central_mosque2, R.drawable.img_central_mosque6, R.drawable.img_central_mosque1, R.drawable.img_central_mosque3, R.drawable.img_central_mosque5},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"mosques"}, "43.268056,76.952778"));

        definitions.add(new PlaceDefinition(
                "29",
                R.string.place_29_title, R.string.place_29_subtitle, R.string.place_29_about,
                4.8f, R.drawable.header_baiken_mosque,
                new int[]{R.string.tag_mosque, R.string.tag_modern, R.string.tag_spiritual},
                new int[]{R.drawable.img_baiken1, R.drawable.img_baiken2, R.drawable.img_baiken3},
                new String[]{BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"mosques"}, "43.22759933293378, 76.89171934104697"));

        definitions.add(new PlaceDefinition(
                "30",
                R.string.place_30_title, R.string.place_30_subtitle, R.string.place_30_about,
                4.8f, R.drawable.header_nurmubarak,
                new int[]{R.string.tag_mosque, R.string.tag_islamic, R.string.tag_spiritual},
                new int[]{R.drawable.img_nurmubarak1, R.drawable.img_nurmubarak2, R.drawable.img_nurmubarak3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"mosques"}, "43.211944,76.917222"));

        definitions.add(new PlaceDefinition(
                "8",
                R.string.place_8_title, R.string.place_8_subtitle, R.string.place_8_about,
                4.9f, R.drawable.header_zenkov,
                new int[]{R.string.tag_cathedral, R.string.tag_architecture, R.string.tag_history},
                new int[]{R.drawable.img_zenkov1, R.drawable.img_zenkov2, R.drawable.img_zenkov3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"cathedrals"}, "43.25886869966127, 76.95286699686824"));

        definitions.add(new PlaceDefinition(
                "32",
                R.string.place_32_title, R.string.place_32_subtitle, R.string.place_32_about,
                4.7f, R.drawable.header_st_nicholas,
                new int[]{R.string.tag_cathedral, R.string.tag_orthodox, R.string.tag_spiritual},
                new int[]{R.drawable.img_nicholas1, R.drawable.img_nicholas2, R.drawable.img_nicholas3},
                new String[]{"all", BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"cathedrals"}, "43.24875069880352, 76.92812816803163"));

        definitions.add(new PlaceDefinition(
                "33",
                R.string.place_33_title, R.string.place_33_subtitle, R.string.place_33_about,
                4.9f, R.drawable.header_ascension,
                new int[]{R.string.tag_cathedral, R.string.tag_architecture, R.string.tag_history},
                new int[]{R.drawable.img_ascension1, R.drawable.img_ascension2, R.drawable.img_ascension3},
                new String[]{BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"cathedrals"}, "43.25890776682174, 76.95290991221246"));

        definitions.add(new PlaceDefinition(
                "34",
                R.string.place_34_title, R.string.place_34_subtitle, R.string.place_34_about,
                4.8f, R.drawable.header_state_museum,
                new int[]{R.string.tag_museum, R.string.tag_history, R.string.tag_culture},
                new int[]{R.drawable.img_statemuseum1, R.drawable.img_statemuseum2, R.drawable.img_statemuseum3},
                new String[]{BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"museums"}, "43.23603506757101, 76.95075342570317"));

        definitions.add(new PlaceDefinition(
                "35",
                R.string.place_35_title, R.string.place_35_subtitle, R.string.place_35_about,
                4.7f, R.drawable.header_music_museum,
                new int[]{R.string.tag_museum, R.string.tag_music, R.string.tag_culture},
                new int[]{R.drawable.img_musicmuseum1, R.drawable.img_musicmuseum2, R.drawable.img_musicmuseum3},
                new String[]{BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"museums"}, "43.259573324848695, 76.95689501406486"));

        definitions.add(new PlaceDefinition(
                "36",
                R.string.place_36_title, R.string.place_36_subtitle, R.string.place_36_about,
                4.8f, R.drawable.header_kasteev,
                new int[]{R.string.tag_museum, R.string.tag_art, R.string.tag_culture},
                new int[]{R.drawable.img_kasteev1, R.drawable.img_kasteev2, R.drawable.img_kasteev3},
                new String[]{BrowseActivityByCategories.CATEGORY_SPIRITUAL},
                new String[]{"museums"}, "43.23401285363191, 76.91922193919495"));

    }

    /**
     * For ExploreFragment and PopularDestinationsActivity - places with category "all"
     */
    public List<PopularPlace> getPlacesForExplore(Context context) {
        return getPlacesByCategory(context, "all");
    }

    /**
     * For BrowseActivityByCategories - filter by category
     */
    public List<PopularPlace> getPlacesByCategory(Context context, String category) {
        return getPlacesByCategoryAndSubFilter(context, category, "all");
    }

    /**
     * For BrowseActivityByCategories - filter by category and sub-filter (all, museums, parks, shopping)
     */
    public List<PopularPlace> getPlacesByCategoryAndSubFilter(Context context, String category, String subFilter) {
        List<PopularPlace> result = new ArrayList<>();
        for (PlaceDefinition def : definitions) {
            if (def.hasCategory(category) && def.hasSubcategory(subFilter)) {
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
                def.categories,
                def.location
        );
    }
}
