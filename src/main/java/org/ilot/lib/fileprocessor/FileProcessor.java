package org.ilot.lib.fileprocessor;


@FunctionalInterface
public interface FileProcessor<T> {

    void process() throws Exception;
}