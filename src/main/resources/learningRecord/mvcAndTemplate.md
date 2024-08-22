## RestTemplate



**RestTemplate** 是 Spring 提供的一个用于方便地发送 HTTP 请求的类。它支持多种 HTTP 方法，包括 GET、POST、PUT、DELETE 等，并且能够处理请求和响应体的序列化与反序列化。

以下是一些常用的 RestTemplate 方法及其用途：

- getForObject

用于发送 GET 请求。

返回请求的响应体，通常用于不需要响应头的情况。

示例：

```java
 String result = restTemplate.getForObject(url, String.class);
```



- getForEntity

用于发送 GET 请求。
返回整个 ResponseEntity 对象，包含了状态码、响应头和响应体。
示例：

```java
 ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
```



- postForObject

用于发送 POST 请求。

返回请求的响应体。

示例：

```java
 String result = restTemplate.postForObject(url, requestPayload, String.class);
```



- postForEntity

用于发送 POST 请求。
返回整个 ResponseEntity 对象。
示例：

```java
 ResponseEntity<String> response = restTemplate.postForEntity(url, requestPayload, String.class);
```



- exchange

最通用的方法，可以发送任何类型的 HTTP 请求。
返回整个 ResponseEntity 对象。
示例：

```java
 ResponseEntity<String> response = restTemplate.exchange(
     url,
     HttpMethod.POST,
     new HttpEntity<>(requestPayload),
     String.class
 );
```

这些方法可以帮助开发者轻松地与远程服务进行交互，处理 RESTful API 调用。在实际应用中，根据不同的需求选择合适的方法。



------

## MockMvc



**MockMvc** 是 Spring Framework 提供的一个工具类，用于模拟 HTTP 请求并测试 Spring MVC 控制器的行为。它允许你在不启动整个应用的情况下测试控制器层。



可直接通过注入来使用MockMvc

```java
@Autowired
private MockMvc mvc;
```



下面列出了一些 MockMvc 常用的方法：

- 执行 HTTP 请求

perform(requestBuilder)：执行一个 HTTP 请求构建器，返回一个MockMvcResultActions 对象，可以用来进一步配置预期结果。

- 构建 HTTP 请求

get(url)：构建 GET 请求。
post(url)：构建 POST 请求。
put(url)：构建 PUT 请求。
delete(url)：构建 DELETE 请求。
patch(url)：构建 PATCH 请求。
options(url)：构建 OPTIONS 请求。

- 配置请求参数

param(name, value)：添加 URL 参数。
content(String content)：设置请求体的内容。
contentType(MediaType)：设置请求的 Content-Type。
accept(MediaType)：设置 Accept 头。
header(name, value)：添加自定义请求头。
with(RequestPostProcessor)：使用请求后处理器，例如添加认证信息。

- 验证响应结果

andExpect(status().isStatusCode())：验证 HTTP 状态码。
andExpect(content().string(expectedContent))：验证响应体内容。
andExpect(content().json(expectedJson))：验证 JSON 格式的响应体。
andExpect(content().xml(expectedXml))：验证 XML 格式的响应体。
andExpect(header().string(name, expectedValue))：验证响应头的值。
andExpect(cookie().value(name, expectedValue))：验证 Cookie 的值。
andExpect(jsonPath("$.property", is(expectedValue)))：验证 JSON 路径的值。

- 其他方法

andReturn()：执行请求并返回 MvcResult，可以从中获取响应体、状态码等信息。
andDo(MockMvcResultHandler)：执行一个结果处理程序，例如打印响应体。

下面是一个简单的示例，展示如何使用 MockMvc 执行一个 POST 请求并验证响应：

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest
public class MyControllerTest {

```java
@Autowired
private MockMvc mockMvc;

@Test
public void testPostRequest() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/my-endpoint")
            .content("some request body")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string("Location", "/upload"));
	}
}
```

在这个例子中，我们首先使用 MockMvcRequestBuilders.post 构建了一个 POST 请求，然后使用 andExpect 方法来验证响应的状态码和 Location 响应头。



------

MockMvc 和 TestRestTemplate 都是 Spring 提供的工具，用于测试基于 Spring MVC 的 Web 应用程序。它们各有特点和适用场景，下面是它们之间的比较：

MockMvc

- 用途：主要用于测试控制器（Controller）层的行为。

- 模拟环境：模拟整个 Spring MVC 堆栈，包括 DispatcherServlet 和过滤器链，但不启动真正的服务器。

- 请求处理：通过模拟请求和响应来测试控制器的逻辑，可以验证请求参数、请求体、响应头、状态码以及响应体等。

- 依赖：不需要启动完整的应用，只需要一个 ApplicationContext。

- 优点：可以精确地控制测试环境，模拟复杂的请求场景。不需要启动完整的应用，因此测试速度较快。

- 缺点：不能测试整个应用的集成情况，例如数据库访问或远程服务调用。




TestRestTemplate

- 用途：既可以用于测试控制器层，也可以用于集成测试。

- 模拟环境：通常用于测试实际部署的应用程序，需要启动整个应用或者模拟外部服务。

- 请求处理：发送真实的 HTTP 请求到服务器，可以测试整个应用的端到端行为。

- 依赖：需要启动完整的应用，或者有外部服务可用。

- 优点：可以测试整个应用的集成情况，包括与其他服务的交互。更接近真实的生产环境。

- 缺点：测试速度较慢，因为需要启动整个应用。需要额外的配置来模拟外部服务或数据库。




示例代码

使用 MockMvc 的示例

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class MyControllerTest {
@Autowired
private MockMvc mockMvc;

@Test
public void testGetRequest() throws Exception {
    mockMvc.perform(get("/endpoint"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello, World!"));
	}
}
```

使用 TestRestTemplate 的示例

```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyControllerIntegrationTest {
@Autowired
private TestRestTemplate restTemplate;

@Test
public void testGetRequest() {
    ResponseEntity<String> response = restTemplate.getForEntity("/endpoint", String.class);
    assertThat(response.getStatusCodeValue()).isEqualTo(200);
    assertThat(response.getBody()).isEqualTo("Hello, World!");
	}
}
```

