package de.laurenzgrote.rwth.kdtrees.io;

import java.nio.file.Path;

/**
 * A FileMalformattedException is thrown when the file does not meed the specification
 */
public class FileMalformattedException extends Exception {

    private static final long serialVersionUID = 1L;

    public FileMalformattedException(Path path, String clue) {
        super("File " + path.getFileName() + " was malformatted: " + clue);
    }
}