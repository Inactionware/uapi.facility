package uapi.net;

public interface IErrorHandler {

    void handle(Exception ex, IResponse response);
}
