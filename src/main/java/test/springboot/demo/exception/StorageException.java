package test.springboot.demo.exception;

/**
 * 封装'存储失败'异常信息
 **/
public class StorageException extends RuntimeException {

    /**
     * 构造函数
     * @param message 异常原因
     **/
    public StorageException(String message) {
        super(message);
    }
    
    /**
     * 构造函数
     * @param message 异常原因
     * @param cause 异常报错
     **/
    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}