package com.example.onthe.map.utils;

import com.example.onthe.map.data.Place;

/**
 * Created by phucle on 9/6/17.
 */

public final class FakeDateUtils {
    public static Place[] insertFakePlaces() {
        return new Place[] {
                new Place("Bun bo", "123 Nguyen Dinh Chieu", "123456789", 4.2, null),
                new Place("Banh canh", "234 Ly Chinh Thang", "332324344", 4.5, null),
                new Place("Bun rieu", "421 Ly Thai To", "213213233", 4.0, null),
                new Place("Com tam", "214 Hai Ba Trung", "223456788", 4.1, null),
                new Place("Banh kem", "312 Tran Quoc Toan", "323456780", 4.2, null),
                new Place("Banh cuon", "22 Cong Ly", "523456781", 4.3, null),
                new Place("Ca phe", "54 Tu Do", "523456782", 4.8, null),
                new Place("Banh mi", "76 Tran Hung Dao", "523456785", 4.7, null),
                new Place("Pho", "124 Nguyen Trai", "323456787", 4.6, null),
                new Place("Goi cuon", "20 Le Loi", "623456782", 4.5, null),
        };
    }
}
