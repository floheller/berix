package org.floheller.berix.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Erstellt und löscht temporäre Verzeichnisse.
 *
 * <p>Das temporäre Verzeichnis wird im vom System vorgesehenen
 * Standardverzeichnis für temporäre Dateien angelegt. Für die Bereinigung
 * ist der aufrufende Code verantwortlich; sie erfolgt nicht automatisch.</p>
 *
 * <p>Beispiel:</p>
 * <pre>
 * Path tempDirectory = TempDirectories.createTempDirectory();
 * try {
 *     // Dateien im temporären Verzeichnis verwenden
 * } finally {
 *     TempDirectories.deleteTempDirectory(tempDirectory);
 * }
 * </pre>
 */
public final class TempDirectories {

    /**
     * Verhindert, dass diese Hilfsklasse instanziiert wird.
     */
    private TempDirectories() {
    }

    /**
     * Erstellt ein neues, leeres temporäres Verzeichnis.
     *
     * <p>Der Verzeichnisname beginnt mit {@code app-}; die restlichen
     * Zeichen werden vom System erzeugt, damit der Name eindeutig ist.</p>
     *
     * @return der Pfad zum neu erstellten Verzeichnis
     * @throws IOException wenn das Verzeichnis nicht erstellt werden kann
     * @throws SecurityException wenn der Zugriff durch einen
     *         Sicherheitsmechanismus verweigert wird
     */
    public static Path createTempDirectory() throws IOException {
        return Files.createTempDirectory("Berix-Temp-Dir-");
    }

    /**
     * Löscht das angegebene Verzeichnis und alle darin enthaltenen Dateien
     * und Unterverzeichnisse.
     *
     * <p>Symbolische Links innerhalb des Verzeichnisses werden nicht verfolgt:
     * Der Link selbst wird gelöscht, nicht das Verzeichnis oder die Datei,
     * auf die er verweist.</p>
     *
     * <p>Ist {@code directory} {@code null} oder existiert der angegebene
     * Pfad nicht, führt die Methode nichts aus.</p>
     *
     * @param directory Pfad zum zu löschenden Verzeichnis
     * @throws IOException wenn ein Eintrag nicht gelöscht werden kann oder
     *         ein Fehler beim Durchlaufen des Verzeichnisbaums auftritt
     * @throws SecurityException wenn der Zugriff durch einen
     *         Sicherheitsmechanismus verweigert wird
     */
    public static void deleteTempDirectory(Path directory) throws IOException {
        if (directory == null || !Files.exists(directory)) {
            return;
        }

        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(
                    Path file, BasicFileAttributes attributes) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(
                    Path dir, IOException exception) throws IOException {
                if (exception != null) {
                    throw exception;
                }
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
