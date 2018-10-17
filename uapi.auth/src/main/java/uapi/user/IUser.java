package uapi.user;

public interface IUser {

    String ANONYMOUS    = "__Anonymous";

    String name();

    IRole[] roles();
}
