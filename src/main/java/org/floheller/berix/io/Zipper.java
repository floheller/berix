package org.floheller.berix.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Zipper {

    /**
    * Extrahiert ein Zip-Archiv in ein neu angelegtes temporäres Verzeichnis und liefert dieses zurück.
    * @param zipFilePath Pfad zum Zip-Archiv (z.B. .\Eigene Dateien\zip-folder\super.zip)
    * @throws IOException
    */

    public static Path  zipFileInTempDirExtrahieren(String zipFilePath) throws IOException {
        Path temp=TempDirectories.createTempDirectory();
        zipFileInsVerzeichnisExtrahieren(zipFilePath,temp.toString());
        return temp;
    }


/**
 * Extrahiert ein Zip-Archiv in das angegebene Zielverzeichnis
 * @param zipFilePath Pfad zum Zip-Archiv
 * @param outputDir Zielverzeichnis
 * @throws IOException
 */
    
    public static void zipFileInsVerzeichnisExtrahieren(String zipFilePath, String outputDir) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;
            byte[] buffer = new byte[8192];
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(outputDir, entry.getName());

                // Ordner ggf. anlegen
                if (entry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    outFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zis.closeEntry();
            }
        }
    }

}
