package org.floheller.berix.io;

import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONObject;

public class ASVSerienbrief {

    HashMap<String,Integer> integerValues = new HashMap<>();
    HashMap<String,String> stringValues = new HashMap<>();


    public ASVSerienbrief(JSONObject data) {

        for (String k:Arrays.asList("_exportTable","dokumentation","schularten","kuerzel","beschreibung","clientKey","id","stand","datenbereich","name")) set(k,data.getString(k));
        for (String k:Arrays.asList("berichtMitNoten","autoArchiv","hatFehler","istPaket","modus","istVorlage","zuordSondertaste","istFreigegeben",
        "mengenausgabe","wiedervorlage","deaktiviert")) set(k,data.getInt(k));

//"schabloneJson"
//  "paketTeilschablonen": []
    }


    public int getInteger(String key) {
        return integerValues.get(key);       
    }

    public String getString(String key) {
        return stringValues.get(key);       
    }


    public void set(String key, int value) {
        integerValues.put(key,value);
    }
    public void set(String key, String value) {
        stringValues.put(key,value);
    }




}
