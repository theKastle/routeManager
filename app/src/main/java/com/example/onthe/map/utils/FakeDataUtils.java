package com.example.onthe.map.utils;

import com.example.onthe.map.data.Place;

/**
 * Created by phucle on 9/6/17.
 */

public final class FakeDataUtils {
    public static Place[] insertFakePlaces() {
        return new Place[] {
                new Place("Bun bo", "123 Nguyen Dinh Chieu", "123456789", (float)4.2),
                new Place("Banh canh", "234 Ly Chinh Thang", "332324344", (float)4.5),
                new Place("Bun rieu", "421 Ly Thai To", "213213233", (float)4.0),
                new Place("Com tam", "214 Hai Ba Trung", "223456788", (float)4.1),
                new Place("Banh kem", "312 Tran Quoc Toan", "323456780", (float)4.2),
                new Place("Banh cuon", "22 Cong Ly", "523456781", (float)4.3),
                new Place("Ca phe", "54 Tu Do", "523456782", (float)4.8),
                new Place("Banh mi", "76 Tran Hung Dao", "523456785", (float)4.7),
                new Place("Pho", "124 Nguyen Trai", "323456787", (float)4.6),
                new Place("Goi cuon", "20 Le Loi", "623456782", (float)4.5)
        };
    }
}
