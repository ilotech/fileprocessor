package org.ilot.lib.fileprocessor;

import java.io.File;

public class FileProcessorSimpleFactory<T> {

    public static FileProcessor create(FileProcessorType type) {
        switch (type) {
            case SEQUENTIAL: return new SequentialFileProcessor(new File("C:\\Users\\lceka\\development\\test\\fileprocessor\\Sacramentorealestatetransactions.csv"), line -> {});
            case CONCURRENT: return new ConcurrentFileProcessor(new File("C:\\Users\\lceka\\development\\test\\fileprocessor\\Sacramentorealestatetransactions.csv"), line -> {});
            case REACTIVE: return null;
            default: throw new IllegalArgumentException("Unknown file processor type: " + type);
        }
    }
}
