package test.springboot.demo;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import test.springboot.demo.exception.StorageFileNotFoundException;
import test.springboot.demo.service.StorageService;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 使用MockMvc测试
 **/
@AutoConfigureMockMvc
@SpringBootTest
public class FileMockMvcTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private StorageService storageService;

	/**
	 * 测试上传文件
	 **/
	@Test
	public void shouldSaveUploadedFile() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
				"text/plain", "hello world".getBytes());
		this.mvc.perform(multipart("/upload").file(multipartFile))
				.andExpect(status().isOk());

		then(this.storageService).should().store(multipartFile);
	}

	/**
	 * 测试文件不存在时抛出404错误
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked") // 抑制编译器对方法体内可能出现的未经检查的警告
	@Test
	public void should404WhenMissingFile() throws Exception {
		given(this.storageService.loadAsResource("test.txt"))
				.willThrow(StorageFileNotFoundException.class);

		this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
	}

}
