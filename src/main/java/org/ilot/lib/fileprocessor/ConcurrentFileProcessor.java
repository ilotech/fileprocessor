package org.ilot.lib.fileprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;

import static java.lang.Math.toIntExact;

public class ConcurrentFileProcessor<T> implements FileProcessor<T> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final File file;
    private final RecordHandler<T> recordHandler;

    private ConcurrentFileProcessorConfig config;

    private LongAdder lineAdder = new LongAdder();

    public ConcurrentFileProcessor(File file, RecordHandler<T> recordHandler) {
        this.file = file;
        this.recordHandler = recordHandler;
    }

    @Override
    public final void process() throws Exception {
//        file.setWritable(true);
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel channel = fileInputStream.getChannel();

//        FileLock fileLock = channel.lock();

        long remainingSize = channel.size(); //get the total number of bytes in the file
        int chunkSize = config.getChunkSize();
        int numberOfThreads = config.getNumberOfThreads();

        //thread pool
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        long startLocation = 0;
        while (remainingSize >= chunkSize)
        {
            executor.execute(new FileReaderTask(startLocation, chunkSize, channel));
            remainingSize = remainingSize - chunkSize;
            startLocation = startLocation + chunkSize;
        }

        //load the last piece
        executor.execute(new FileReaderTask(startLocation, toIntExact(remainingSize), channel));

        executor.shutdown();

        //Wait for all threads to finish
        while (!executor.isTerminated())
        {
            //wait for infinity time
        }
        System.out.println("Finished all threads");
        fileInputStream.close();
//        fileLock.release();

        System.out.println("Lines processed: " + lineAdder.sum());
    }

    public void configure(ConcurrentFileProcessorConfig config) {
        this.config = config;
    }

    public class FileReaderTask implements Runnable {
        private FileChannel channel;
        private long startLocation;
        private int size;

        public FileReaderTask(long startLocation, int size, FileChannel channel) {
            this.startLocation = startLocation;
            this.size = size;
            this.channel = channel;
        }

        @Override
        public void run() {
            try {
                System.out.println("Reading the channel: " + startLocation + ":" + size);
                System.out.println("Thread ---> " + Thread.currentThread());

                //allocate JVM buffer
                ByteBuffer buff = ByteBuffer.allocate(size);

                if (startLocation != 0L) {
                    startLocation = startLocation - config.getAverageLineSize();
                }

                //read file chunk to buffer
                channel.read(buff, startLocation);

                //buffer to string
                String string_chunk = new String(buff.array(), Charset.forName("UTF-8"));


                List<String> lines = Arrays.asList(string_chunk.split("\\r"));

                lines.forEach(recordHandler::handle);
                lineAdder.add(lines.size());
                System.out.println("Done Reading the channel: " + startLocation + ":" + size);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}