import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zipper {

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