# Download-File-Java
## 项目简述

演示如何在springboot框架下，实现文件上传、文件下载与查看；



## 演示环境

- idea集成开发工具
- JDK21
- Apache Maven 3.9.4 



## 接口设计

URL: `http://localhost/files/{filename}`

method: GET

query: filename-文件名

function: 查看文件



URL: `http://localhost/files/download/{filename}`

method: GET

query: filename-文件名

function: 下载文件



URL: `http://localhost/upload`

method: GET

param: file-多文件对象

function: 上传文件



## 单元测试

分别使用MockMvc与TestRestTemplate测试工具类，模拟客户端发送HTTP请求，对项目接口与存储服务层进行了测试；



## 提醒

- 客户端上传的文件均存放在`resources`的`localStorage`目录下，可以在`application.properties`文件内修改`storage.path`来更改下载路径；
- 客户端上传的文件设置了最大文件大小4MB，可以在`application.properties`文件内修改最大文件大小；
- `resources`下的`learningRecord`目录下存放了author的部分学习知识点，仅供参考；
- 本项目参考[spring-guides/gs-uploading-files: Uploading Files :: Learn how to build a Spring application that accepts multi-part file uploads. (github.com)](https://github.com/spring-guides/gs-uploading-files)官方SpringBoot学习文档的章节uploading-files所提供的示例，自行编写代码，撰写了大量注释帮助理解代码。
