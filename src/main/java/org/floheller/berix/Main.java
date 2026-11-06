package org.floheller.berix;
import java.nio.file.Path;

import org.floheller.berix.io.ASVSerienbrief;
import org.floheller.berix.io.Zipper;
import org.floheller.berix.io.bsbj.BSBJReader;

import com.jcabi.xml.XMLDocument;

public class Main {

    public static void main(String[] args) {
        try {
            Path p=Zipper.zipFileInTempDirExtrahieren("/home/florian/tmp/löschen/Bericht Masernschutz/ST156/Nachweis Masernschutz _ 20 Abs_ 9.bsbj");
            Path objectlistPath=p.resolve("Objectliste.json");
            ASVSerienbrief serienbrief=BSBJReader.parseBSJBFilePath(objectlistPath);
            String xml = new XMLDocument(serienbrief.getXMLDocument()).toString();
            System.out.println(xml);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}