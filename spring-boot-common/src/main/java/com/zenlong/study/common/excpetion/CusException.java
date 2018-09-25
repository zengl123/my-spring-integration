package com.zenlong.study.common.excpetion;

/**
 * @Project my-spring-integration
 * @Package com.zenlong.study.common.excpetion
 * @ClassName CusException
 * @Author ZENLIN
 * @Date 2018-09-13 23:38
 * @Description TODO
 * @Version
 * @Modified By
 */
public class CusException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public CusException() {
    }

    public CusException(String message) {
        super(message);
    }

    public CusException(String msgTemplate, Object... args) {
        super(String.format(msgTemplate, args));
    }

    public CusException(String message, Throwable cause) {
        super(message, cause);
    }

    public CusException(Throwable cause, String msgTemplate, Object... args) {
        super(String.format(msgTemplate, args), cause);
    }

    public CusException(Throwable cause) {
        super(cause);
    }

}
