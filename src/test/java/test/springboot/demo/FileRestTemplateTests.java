package test.springboot.demo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import test.springboot.demo.service.StorageService;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * 使用TestRestTemplate测试，模拟Spring Boot应用程序的运行环境，任意端口
 **/
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileRestTemplateTests {

	// TestRestTemplate是Spring提供的一个简化HTTP请求的工具类，
	// 常用于集成测试中模拟客户端向服务端发送HTTP请求。
	// 在这个上下文中，它将被用来执行文件上传和下载的HTTP请求操作。
	@Autowired
	private TestRestTemplate restTemplate;

	// StorageService的mock对象，用于模拟存储服务，避免对实际服务层进行调用
	@MockBean
	private StorageService storageService;

	// 获得应用的端口，用于测试
	@LocalServerPort
	private int port;


	/**
	 * 测试文件上传功能
	 **/
	@Test
	public void shouldUploadFile() throws Exception {
		// 获得本地存储的的test.txt文件
		ClassPathResource resource = new ClassPathResource("test.txt");
		// 放入多文件集合请求中
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", resource);
		// 执行上传操作
		ResponseEntity<String> response = this.restTemplate.postForEntity("/upload", map,
				String.class);
		// 判断是否存储文件成功
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	/**
	 * 测试文件下载功能
	 **/
	@Test
	public void shouldDownloadFile() throws Exception {
		// 获得本地存储的的test.txt文件
		ClassPathResource resource = new ClassPathResource("test.txt");
		// mock设定调用loadAsResource("test.txt")时返回本地存储的此文件
		given(this.storageService.loadAsResource("test.txt")).willReturn(resource);
		// 使用RestTemplate对象发起GET请求，下载文件,返回数据类型指定为string
		ResponseEntity<String> response = this.restTemplate
				.getForEntity("/files/{filename}", String.class, "test.txt");

		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		assertThat(response.getBody()).isEqualTo("hello world!");
	}

}
