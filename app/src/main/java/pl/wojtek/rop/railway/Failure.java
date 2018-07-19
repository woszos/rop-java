package pl.wojtek.rop.railway;

import java.util.concurrent.Future;

import pl.wojtek.rop.railway.exceptions.FailedResultHasNoValueException;
import pl.wojtek.rop.railway.exceptions.RailwayFutureException;
import pl.wojtek.rop.railway.functions.Consumer;
import pl.wojtek.rop.railway.functions.Function;

public class Failure<TSuccess, TFailure> extends Result<TSuccess, TFailure> {

    private final TFailure error;

    public Failure(final TFailure error)
    {
        this.error = error;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public TSuccess getValue() {
        throw new FailedResultHasNoValueException();
    }

    @Override
    public TFailure getError() {
        return error;
    }

    @Override
    public <T> Result<T, TFailure> map(Function<TSuccess, T> function) {
        return Failure.withError(getError());
    }

    @Override
    public <T> Result<TSuccess, T> mapFailure(Function<TFailure, T> function) {
        return Failure.withError(function.apply(getError()));
    }

    @Override
    public <T> Result<T, TFailure> bind(final Function<TSuccess, Result<T, TFailure>> function) {
        return (Result<T, TFailure>) this;
    }

    @Override
    public <T> Result<T, RailwayFutureException> bindFuture(Function<TSuccess, Future<T>> function) {
        return Failure.withError(new RailwayFutureException(getError()));
    }


    @Override
    public Result<TSuccess, TFailure> tee(Consumer<TSuccess> successFunction) {
        return this;
    }

    @Override
    public Result<TSuccess, TFailure> onSuccess(Consumer<TSuccess> function) {
        return this;
    }

    @Override
    public Result<TSuccess, TFailure> onFailure(Consumer<TFailure> function) {
        function.accept(getError());
        return this;
    }

    @Override
    public Result<TSuccess, TFailure> result(Consumer<TSuccess> successFunction, Consumer<TFailure> failureFunction) {
        failureFunction.accept(getError());
        return this;
    }
}
