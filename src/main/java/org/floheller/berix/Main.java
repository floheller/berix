package org.floheller.berix;
import java.io.IOException;
import java.nio.file.Path;

import org.floheller.berix.io.Zipper;
import org.floheller.berix.io.bjb.BSJBReader;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hallo");
        try {
            Path p=Zipper.zipFileInTempDirExtrahieren("/home/florian/tmp/löschen/Bericht Masernschutz/ST156/Nachweis Masernschutz _ 20 Abs_ 9.bsbj");
            System.out.println(p.toString());
            Path objectlistPath=p.resolve("Objectliste.json");
            System.out.println(objectlistPath);
            System.out.println(BSJBReader.parseBSJBFilePath(objectlistPath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}