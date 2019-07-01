package org.ilot.lib.fileprocessor;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractRecordHandler<T> implements RecordHandler<T> {
    private final Function<String, T> mappingFunction = getMappingFunction();
    private final Predicate<T> validator = getValidator();
    private final Consumer<T> failedRecordHandler = getFailedRecordHandler();
    private final Consumer<T> recordHandler = getRecordHandler();

    protected abstract Function<String, T> getMappingFunction();
    protected abstract Predicate<T> getValidator();
    protected abstract Consumer<T> getFailedRecordHandler();
    protected abstract Consumer<T> getRecordHandler();

    @Override
    public void handle(String line) {
        T object = mappingFunction.apply(line);
        if (!validator.test(object)) {
            failedRecordHandler.accept(object);
            return;
        }
        recordHandler.accept(object);
    }
}