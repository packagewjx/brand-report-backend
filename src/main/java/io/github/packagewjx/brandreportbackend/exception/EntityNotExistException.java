package io.github.packagewjx.brandreportbackend.exception;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-17
 **/
public class EntityNotExistException extends RuntimeException {
    public EntityNotExistException() {
    }

    public EntityNotExistException(String message) {
        super(message);
    }
}
