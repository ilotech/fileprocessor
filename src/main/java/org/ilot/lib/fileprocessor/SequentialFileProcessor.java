package org.ilot.lib.fileprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SequentialFileProcessor<T> implements FileProcessor<T> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final File file;
    private final RecordHandler<T> recordHandler;

    public SequentialFileProcessor(File file, RecordHandler<T> recordHandler) {
        this.file = file;
        this.recordHandler = recordHandler;
    }

    @Override
    public void process() {
        int counter = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                recordHandler.handle(line);
                counter++;
            }
        } catch (IOException e) {
            logger.error("Exception occurred while processing file: {}. Exception: {}",
                    file.getAbsolutePath(), e.getMessage());
        }
        System.out.println("Processed " + counter + " lines!");
    }
}