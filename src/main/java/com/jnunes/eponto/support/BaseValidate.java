package com.jnunes.eponto.support;

import com.jnunes.core.commons.ValidationUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseValidate {

    protected void validateNonNull(Object object, Runnable action) {
        ValidationUtils.validateNonNull(object, action);
    }

    protected void validateNull(Object object, Runnable action) {
        ValidationUtils.validateNull(object, action);
    }

    protected <T>void validateNonNullThen(T object, Consumer<T> consumer){
        ValidationUtils.validateNonNullThen(object, consumer);
    }
    protected void validateNonEmpty(Collection<?> collection, Runnable action) {
        validate(CollectionUtils.isNotEmpty(collection), action);
    }

    protected <T>void validateNonEmpty(List<T> collection, Consumer<List<T>> ifConsumer) {
        ValidationUtils.validateNonEmpty(collection, ifConsumer);
    }
    protected <T>void validateEmpty(List<T> collection, Runnable action) {
        ValidationUtils.validateEmpty(collection, action);
    }

    protected <T>void validateNonEmptyOrElse(List<T> collection, Consumer<List<T>> ifConsumer, Runnable elseAction) {
       ValidationUtils.validateNonEmptyOrElse(collection, ifConsumer, elseAction);
    }

    protected <T>List<T> validateNonEmptyThenGetCollection(List<T> collection, Supplier<List<T>> supplier) {
        return ValidationUtils.validateNonEmptyThenGetCollection(collection, supplier);
    }

    protected void validate(Boolean expression, Runnable action) {
        ValidationUtils.validate(expression, action);
    }

    protected void validateOrElse(Boolean expression, Object value, Consumer<Object> ifConsumer, Consumer<Object> orElseConsumer) {
        ValidationUtils.validateOrElse(expression, value, ifConsumer, orElseConsumer);
    }

    protected void validateOrElse(Boolean expression, Object value, Consumer<Object> ifConsumer, Runnable elseAction) {
        ValidationUtils.validateOrElse(expression, value, ifConsumer, elseAction);
    }

    protected Optional<?> validateThenGet(Boolean expression, Supplier<?> supplier) {
        return ValidationUtils.validateThenGet(expression, supplier);
    }

    protected void runnableOrException(Runnable action) {
        ValidationUtils.runnableOrException(action);
    }
}
