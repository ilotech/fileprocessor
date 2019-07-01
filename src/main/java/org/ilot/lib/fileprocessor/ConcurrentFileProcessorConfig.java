package org.ilot.lib.fileprocessor;

public class ConcurrentFileProcessorConfig {
    private final int averageLineSize;
    private final int numberOfThreads;
    private final int chunkSize;
    private final String lineSplitterChar;

    private ConcurrentFileProcessorConfig(int averageLineSize,
                                          int numberOfThreads,
                                          int chunkSize,
                                          String lineSplitterChar) {

        this.averageLineSize = averageLineSize;
        this.numberOfThreads = numberOfThreads;
        this.chunkSize = chunkSize;
        this.lineSplitterChar = lineSplitterChar;
    }

    int getAverageLineSize() {
        return averageLineSize;
    }

    int getNumberOfThreads() {
        return numberOfThreads;
    }

    int getChunkSize() {
        return chunkSize;
    }

    String getLineSplitterChar() {
        return lineSplitterChar;
    }

    public static class ConcurrentFileProcessorConfigBuilder {
        private int averageLineSize = 50;
        private int numberOfThreads = Runtime.getRuntime().availableProcessors();
        private int chunkSize = 10_000;
        private String lineSplitterChar = "\\r";

        public static ConcurrentFileProcessorConfigBuilder getBuilder() {
            return new ConcurrentFileProcessorConfigBuilder();
        }

        public ConcurrentFileProcessorConfigBuilder withAverageLineSize(int averageLineSize) {
            this.averageLineSize = averageLineSize;
            return this;
        }

        public ConcurrentFileProcessorConfigBuilder withNumberOfThreads(int numberOfThreads) {
            this.numberOfThreads = numberOfThreads;
            return this;
        }

        public ConcurrentFileProcessorConfigBuilder withChunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
            return this;
        }

        public ConcurrentFileProcessorConfigBuilder withLineSplitterChar(String lineSplitterChar) {
            this.lineSplitterChar = lineSplitterChar;
            return this;
        }

        public ConcurrentFileProcessorConfig build() {
            return new ConcurrentFileProcessorConfig(averageLineSize, numberOfThreads, chunkSize, lineSplitterChar);
        }
    }
}
