package test.springboot.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import test.springboot.demo.service.StorageService;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/**
	 * 初始化存储，CommandLineRunner是Spring Boot提供的接口，允许在应用启动完成后执行一些操作
	 * 实现该接口的方法会在应用启动后自动运行，通常用于执行启动时的任务，如数据初始化等
	 * @param storageService-存储服务
	 * @return
	 **/
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
			storageService.deleteAll();
			storageService.init();
		};
	}
}
