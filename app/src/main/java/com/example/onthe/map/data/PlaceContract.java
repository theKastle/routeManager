package com.example.onthe.map.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by phucle on 9/8/17.
 */

public class PlaceContract {
    public static final String CONTENT_AUTHORITY = "com.example.onthe.map";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PLACE_ID = "place_id";

    public static final class PlaceEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_PLACE_ID)
                .build();

        public static final String TABLE_NAME = "place";

        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_RATING = "rating";

        public static Uri buildUriWithId(String placeId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(placeId)
                    .build();
        }
    }
}
