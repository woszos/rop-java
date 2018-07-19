package pl.wojtek.rop.railway;

import java.util.concurrent.Future;

import pl.wojtek.rop.railway.exceptions.RailwayFutureException;
import pl.wojtek.rop.railway.functions.Consumer;
import pl.wojtek.rop.railway.functions.Function;

public abstract class Result<TSuccess, TFailure> {

    public static <TSuccess, TFailure> Result<TSuccess, TFailure> withError(final TFailure error) {
        assertParameterNotNull(error, "Error");
        return new Failure<>(error);
    }

    public static <TFailure> Result<Void, TFailure> withoutValue() {
        return new Success<>(null);
    }

    protected static void assertParameterNotNull(final Object parameter, final String name) {
        if (parameter == null)
        {
            throw new IllegalArgumentException(String.format("%s may not be null.", name));
        }
    }

    public static <TSuccess, TFailure> Result<TSuccess, TFailure> withValue(final TSuccess value) {
        assertParameterNotNull(value, "Value");
        return new Success<>(value);
    }

    public static <TSuccess, TFailure> Result<TSuccess, TFailure> with(final TSuccess value, final TFailure error) {
        Result result = null;
        if (value != null) {
            result = withValue(value);
        }
        result = withError(error);
        return result;
    }

    public abstract boolean isFailure();

    public final boolean isSuccess() {
        return !isFailure();
    }

    public abstract TSuccess getValue();

    public abstract TFailure getError();

    public abstract <T> Result<T, TFailure> map(final Function<TSuccess, T> function);

    public abstract <T> Result<TSuccess, T> mapFailure(final Function<TFailure, T> function);

    public abstract <T> Result<T, TFailure> bind(final Function<TSuccess, Result<T, TFailure>> function);

    public abstract <T> Result<T, RailwayFutureException> bindFuture(final Function<TSuccess, Future<T>> function);

    public abstract Result<TSuccess, TFailure> tee(final Consumer<TSuccess> successFunction);

    public abstract Result<TSuccess, TFailure> onSuccess(final Consumer<TSuccess> function);

    public abstract Result<TSuccess, TFailure> onFailure(final Consumer<TFailure> function);

    public abstract Result<TSuccess, TFailure> result(final Consumer<TSuccess> successFunction, final Consumer<TFailure> failureFunction);

}
