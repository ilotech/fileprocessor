package org.ilot.lib.fileprocessor;

public interface RecordHandler<T> {
    void handle(String line);
}