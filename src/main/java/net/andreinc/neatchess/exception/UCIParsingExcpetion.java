package net.andreinc.neatchess.exception;

public class UCIParsingExcpetion extends RuntimeException {
    public UCIParsingExcpetion(String line) {
        super("Cannot parse line: " + line);
    }
}
