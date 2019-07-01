package org.ilot.lib.fileprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class FileProcessorApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(FileProcessorApplication.class, args);

//		ConcurrentFileProcessor concurrentFileProcessor = new ConcurrentFileProcessor(new File("C:\\Users\\lceka\\development\\test\\fileprocessor\\Sacramentorealestatetransactions.csv"), o -> true, s -> null);
//		concurrentFileProcessor.process();

		FileProcessor fileProcessor = FileProcessorSimpleFactory.create(FileProcessorType.CONCURRENT);
		fileProcessor.process();
	}
}