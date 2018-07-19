package pl.wojtek.rop.railway;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import pl.wojtek.rop.railway.exceptions.RailwayFutureException;
import pl.wojtek.rop.railway.exceptions.SuccessfulResultHasNoErrorException;
import pl.wojtek.rop.railway.functions.Consumer;
import pl.wojtek.rop.railway.functions.Function;

public class Success<TSuccess, TFailure> extends Result<TSuccess, TFailure> {

    private TSuccess value;

    public Success(final TSuccess value) {
        this.value = value;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public TSuccess getValue() {
        return value;
    }

    @Override
    public TFailure getError() {
        throw new SuccessfulResultHasNoErrorException();
    }

    @Override
    public <T> Result<T, TFailure> map(Function<TSuccess, T> function) {
        return Success.withValue(function.apply(getValue()));
    }

    @Override
    public <T> Result<TSuccess, T> mapFailure(Function<TFailure, T> function) {
        return Success.withValue(getValue());
    }

    @Override
    public <T> Result<T, TFailure> bind(final Function<TSuccess, Result<T, TFailure>> function) {
        return function.apply(getValue());
    }

    @Override
    public <T> Result<T, RailwayFutureException> bindFuture(Function<TSuccess, Future<T>> function) {
        Result<T, RailwayFutureException> value = null;
        try {
            value = Success.withValue(function.apply(getValue()).get());
        } catch (Exception e) {
            value = Failure.withError(new RailwayFutureException(e));
        }
        return value;
    }

    @Override
    public Result<TSuccess, TFailure> tee(Consumer<TSuccess> function) {
        function.accept(getValue());
        return this;
    }

    @Override
    public Result<TSuccess, TFailure> onSuccess(Consumer<TSuccess> function) {
        function.accept(getValue());
        return this;
    }

    @Override
    public Result<TSuccess, TFailure> onFailure(Consumer<TFailure> function) {
        return this;
    }

    @Override
    public Result<TSuccess, TFailure> result(Consumer<TSuccess> successFunction, Consumer<TFailure> failureFunction) {
        successFunction.accept(getValue());
        return this;
    }


}
