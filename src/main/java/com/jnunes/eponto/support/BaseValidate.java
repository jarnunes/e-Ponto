package com.jnunes.eponto.support;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseValidate {

    protected void validateNonNull(Object object, Runnable action) {
        validate(Objects.nonNull(object), action);
    }

    protected void validateNull(Object object, Runnable action) {
        validate(Objects.isNull(object), action);
    }

    protected void validateNonEmpty(Collection<?> collection, Runnable action) {
        validate(CollectionUtils.isNotEmpty(collection), action);
    }

    protected void validateNonEmpty(Collection<?> collection, Consumer<Collection<?>> ifConsumer) {
        if(CollectionUtils.isNotEmpty(collection))
            ifConsumer.accept(collection);
    }

    protected void validate(Boolean expression, Runnable action) {
        if (expression) {
            action.run();
        }
    }

    protected void validateOrElse(Boolean expression, Object value, Consumer<Object> ifConsumer, Consumer<Object> orElseConsumer) {
        if (Objects.nonNull(value)) {
            if (expression) {
                ifConsumer.accept(value);
            } else {
                orElseConsumer.accept(value);
            }
        }
    }

    protected void validateOrElse(Boolean expression, Object value, Consumer<Object> ifConsumer, Runnable elseAction) {
        if (expression && Objects.nonNull(value)) {
            ifConsumer.accept(value);
        } else {
            elseAction.run();
        }
    }

    protected Optional<?> validateThenGet(Boolean expression, Supplier<?> supplier) {
        return expression ? Optional.of(supplier.get()) : Optional.empty();
    }
}
