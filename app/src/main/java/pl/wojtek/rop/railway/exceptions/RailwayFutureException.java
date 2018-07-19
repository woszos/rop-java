package pl.wojtek.rop.railway.exceptions;

public class RailwayFutureException extends Exception {

    private Object error;

    public RailwayFutureException(Object error) {
        this.error = error;
    }

    public Object getObject() {
        return error;
    }
}
