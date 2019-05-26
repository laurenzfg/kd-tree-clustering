package de.laurenzgrote.rwth.kdtrees.io;

import java.nio.file.Path;

/**
 * FileMalformatted
 */
public class FileMalformattedException extends Exception {

    public FileMalformattedException(Path path, String clue) {
        super("File " + path.getFileName() + " was malformatted: " + clue);
    }
}