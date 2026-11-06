package org.floheller.berix.io;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.floheller.berix.io.bsbj.SvgXmlReader;
import org.json.JSONObject;

public class ASVSerienbrief {



    private HashMap<String,Integer> integerValues = new HashMap<>();
    private HashMap<String,String> stringValues = new HashMap<>();
    private Document xmldoc;


    public ASVSerienbrief(JSONObject jsonData) throws ParserConfigurationException, IOException, SAXException {

        for (String k:Arrays.asList("_exportTable","dokumentation","schularten","kuerzel","beschreibung","clientKey","id","stand","datenbereich","name")) set(k,jsonData.getString(k));
        for (String k:Arrays.asList("berichtMitNoten","autoArchiv","hatFehler","istPaket","modus","istVorlage","zuordSondertaste","istFreigegeben",
        "mengenausgabe","wiedervorlage","deaktiviert")) set(k,jsonData.getInt(k));

        String schablone=jsonData.getString("schabloneJson");
        JSONObject schabloneJSON=new JSONObject(schablone);
        String b64kodiert = schabloneJSON.getJSONObject("mPrintDrawing").getJSONArray("mDrawing").getJSONObject(0).getString("mDrawing");
        byte[] dekodiert = Base64.getDecoder().decode(b64kodiert);
        String decodedStr = new String(dekodiert, StandardCharsets.UTF_8);
        xmldoc = SvgXmlReader.read(decodedStr);


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

    public Document getXMLDocument() {
        return xmldoc;
    }

    public Element getRootNode() {
        return xmldoc.getDocumentElement();
    }


    public void printOut() {
        
        this.printNode(getRootNode(),"");
    }

    public void printNode(Node n, String space) {

        System.out.print(space + "<"+n.getNodeName()+">");
                        System.out.println(n.getTextContent());

        for (int i=0;i<n.getChildNodes().getLength();i++) {
            printNode(n.getChildNodes().item(i), space+"    ");
        }
        System.out.println(space + "</"+n.getNodeName()+">");
        

    }






}
