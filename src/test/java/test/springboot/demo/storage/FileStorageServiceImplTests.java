package test.springboot.demo.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import test.springboot.demo.service.impl.FileStorageServiceImpl;
import test.springboot.demo.exception.StorageException;
import test.springboot.demo.config.StorageConfigure;

import java.util.Random;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 用于测试本地文件存储的服务层
 **/
public class FileStorageServiceImplTests {

	private StorageConfigure properties = new StorageConfigure();
	private FileStorageServiceImpl service;

	@BeforeEach
	public void init() {
		properties.setLocation("src/main/resources/localStorage/files/" + Math.abs(new Random().nextLong()));
		service = new FileStorageServiceImpl(properties);
		service.init();
	}
	@AfterEach
	public void terminate() {
		properties.setLocation("src/main/resources/localStorage/files/");
		service = new FileStorageServiceImpl(properties);
		service.deleteAll();
	}

    @Test
    public void emptyUploadLocation() {
        service = null;
        properties.setLocation("");
        assertThrows(StorageException.class, () -> {
            service = new FileStorageServiceImpl(properties);
		});
    }

	@Test
	public void loadNonExistent() {
		assertThat(service.load("foo.txt")).doesNotExist();
	}

	@Test
	public void saveAndLoad() {
		service.store(new MockMultipartFile("admin", "admin.txt", MediaType.TEXT_PLAIN_VALUE,
				"I am cool boy！".getBytes()));
		assertThat(service.load("admin.txt")).exists();
	}

	@Test
	public void saveRelativePathNotPermitted() {
		assertThrows(StorageException.class, () -> {
			service.store(new MockMultipartFile("admin", "../admin.txt",
					MediaType.TEXT_PLAIN_VALUE, "I am cool boy！".getBytes()));
		});
	}

	@Test
	public void savePermitted() {
		service.store(new MockMultipartFile("amdin", "localStorage/../admin.txt",
				MediaType.TEXT_PLAIN_VALUE, "I am cool boy！".getBytes()));
	}

}
