package test.springboot.demo.exception;

/**
 * 封装'文件未找到'异常信息
 **/
public class StorageFileNotFoundException extends StorageException {

    /**
     * 构造函数
     * @param message 异常原因
     **/
    public StorageFileNotFoundException(String message) {
        super(message);
    }

    /**
     * 构造函数
     * @param message 异常原因
     * @param cause 异常报错
     **/
    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}