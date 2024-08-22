package test.springboot.demo.service.impl;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import test.springboot.demo.exception.StorageException;
import test.springboot.demo.exception.StorageFileNotFoundException;
import test.springboot.demo.config.StorageConfigure;
import test.springboot.demo.service.StorageService;

@Service
public class FileStorageServiceImpl implements StorageService {

    private final Path rootLocation;
    /**
     * 构造方法
     * @param properties StorageProperties对象
     **/
    @Autowired
    public FileStorageServiceImpl(StorageConfigure properties) {
        // 若下载路径为空，抛出异常
        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }
        // 设置下载路径
        this.rootLocation = Paths.get(properties.getLocation());
    }

    /**
     * 存储多文件对象到下载路径下
     * @param file MultipartFile类型的多文件对象
     * @return 是否存储成功
     * @exception StorageException 存储异常
     **/
    @Override
    public boolean store(MultipartFile file) {
        try {
            // 判断是否为空
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = this.rootLocation.resolve(
                            // Paths.get获取文件名并转为Path对象
                            Paths.get(file.getOriginalFilename()))
                    // 转为绝对路径
                    .normalize().toAbsolutePath();
            // 判断是否与期望的下载路径一致
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            // 拷贝文件,到下载路径
            try (InputStream inputStream = file.getInputStream()) {
                long size = Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                if (size > 0) {
                    return true;
                }
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
        return false;
    }

    /**
     * 获得下载路径下的所有文件Path流
     * @return Path文件流
     * @exception StorageException 存储异常
     **/
    @Override
    public Stream<Path> loadAll() {
        try {
            //获得下载路径下的深度为一的所有文件夹与文件
            return Files.walk(this.rootLocation, 1)
                    // 去掉下载路径的文件夹
                    .filter(path -> !path.equals(this.rootLocation))
                    // 返回处理成相对路径的Path流
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    /**
     * 获得Path文件路径
     * @param filename 文件名
     * @return Path文件路径
     **/
    @Override
    public Path load(String filename) {
        // 获得Path文件路径
        return rootLocation.resolve(filename);
    }

    /**
     * 获得文件资源
     * @param filename 文件名
     * @return 文件资源
     * @exception StorageFileNotFoundException 文件不存在异常
     **/
    @Override
    public Resource loadAsResource(String filename) {
        try {
            // Path 是 Java 7 引入的一个接口，它是 java.nio.file 包的一部分，用于表示文件系统中的路径。
            // Path 接口定义了一些基本的操作，而具体的实现类如 java.nio.file.Paths 提供了创建 Path 对象的方法。

            // 获得本地存储的此文件名的Path
            Path file = load(filename);
            // 获得文件URL，Resource是Spring Framework中的类，用于封装对资源的访问
            // toUri() 方法返回一个 URI 对象，表示文件可访问的网络位置
            Resource resource = new UrlResource(file.toUri());
            // 检测文件是否存在或可读
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    /**
     * 删除下载路径文件夹及其路径下所有文件
     **/
    @Override
    public void deleteAll() {
        // toFile()将下载路径转为File对象,并遍历删除所有文件
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * 初始化下载路径
     * @exception StorageException 存储异常
     **/
    @Override
    public void init() {
        try {
            // 创建下载路径文件夹,若该文件夹已经存在则不做任何操作
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}