package org.floheller.berix.io.bsbj;
import org.floheller.berix.io.ASVSerienbrief;
import org.floheller.berix.io.bsbj.SvgXmlReader.BJBElement;
import org.floheller.berix.io.bsbj.SvgXmlReader.BJBNode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Klasse zum Lesen der BJB-Dateien
 * BJBReader
 */



public class BSJBReader {
    
    public static JSONObject readObjectlisteJSONObject(Path filepath) throws IOException {
        try (Reader reader = Files.newBufferedReader(filepath, StandardCharsets.UTF_8)) {
            JSONObject json = new JSONObject(new JSONTokener(reader));
            return json;   
        }
    }

        public static JSONArray readObjectlisteJSONArray(Path filepath) throws IOException {
        try (Reader reader = Files.newBufferedReader(filepath, StandardCharsets.UTF_8)) {
            JSONArray json = new JSONArray(new JSONTokener(reader));
            return json;   
        }
    }

    public static JSONObject parseBSJBFilePath(Path filepath) throws IOException, BJBParseException, ParserConfigurationException, SAXException {
        JSONArray arr=readObjectlisteJSONArray(filepath);
        if (arr.length()>1) BJBParseException.throwMultipleListElementsError("Gesamte Datei "+filepath+" besteht auf oberster Ebene aus mehreren Listeneinträgen.");
        JSONObject obj=arr.getJSONObject(0);
        ASVSerienbrief serienbrief=new ASVSerienbrief(obj);


        String schablone=obj.getString("schabloneJson");
        JSONObject schabloneJSON=new JSONObject(schablone);
        obj.put("schabloneJson",schabloneJSON);
        String b64kodiert = schabloneJSON.getJSONObject("mPrintDrawing").getJSONArray("mDrawing").getJSONObject(0).getString("mDrawing");
        byte[] dekodiert = Base64.getDecoder().decode(b64kodiert);
        String decodedStr = new String(dekodiert, StandardCharsets.UTF_8);

        BJBNode document = SvgXmlReader.read(decodedStr);
        printBJBDocument(document.elements());
        return null;

    }

    private static void printBJBDocument(List<BJBElement> list) {
        for (BJBElement elem:list) {
            if ("textArea".equals(elem.type())) 
                for (Map.Entry<String, String> eintrag : elem.attributes().entrySet()) {
                    String key=eintrag.getKey();
                    String value=eintrag.getValue();
                    if ("events".equals(key)) {
                        if (value.trim().startsWith("onLoad")) {
                            System.out.println(elem.attributes().get("name"));
                            System.out.println("-------------------------------------");
                            String b64string=value.substring(value.indexOf(":")+1);
                            String decodedStr = new String(Base64.getDecoder().decode(b64string), StandardCharsets.UTF_8);
                            System.out.println(decodedStr);
                        }
                    }
                }

        }
    }
}




