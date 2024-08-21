package test.springboot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import test.springboot.demo.exception.StorageFileNotFoundException;
import test.springboot.demo.service.StorageService;

@Controller
public class FileUploadController {


    private final StorageService storageService;

    /**
     * 采用构造函数来注入StorageService对象，方便进行单测
     * @param storageService StorageService对象
     **/
     @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    // 与上面的构造函数一样，用于注入StorageService对象
    // @Autowired
    // private StorageService storageService;

    // 做代理，客户端可下载资源或查看资源
    @GetMapping(value = {"/files/{filename:.+}", "/files/{download:.+}/{filename:.+}"})
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable(required = false) String download, @PathVariable String filename) {
        // 获取文件数据
        Resource file = storageService.loadAsResource(filename);
        // 如果文件为空就返回响应404
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        // 创建响应实体，设置状态码为200
        ResponseEntity.BodyBuilder req = ResponseEntity.status(HttpStatus.OK);
        // 如果download不为空，则执行下载，添加消息头attachment
        if (download!=null) {
            req.header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"");
        }
        // 设置默认文件类型为application/octet-stream，二进制流
        String contentType = "application/octet-stream";
        if (file.getFilename() != null) {
            // 获得文件名后缀
            String ext = getFileExtension(file.getFilename());
            switch (ext) {
                case "pdf":
                    contentType = "application/pdf";
                    break;
                case "png", "gif", "jpg":
                    contentType = "image/" + ext;
                    break;
                case "jpeg":
                    contentType = "image/jpeg";
                    break;
                case "ofd", "zip":
                    contentType = "application/" + ext;
                    break;
                case "xlsx":
                    contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                    break;
            }
        }
        // 返回封装好的响应实体
        return req.contentType(MediaType.valueOf(contentType))
                .body(file);
    }

    /**
     * 获得文件名后缀
     * @param fileName 文件名
     * @return 文件后缀
     **/
    public String getFileExtension(String fileName) {
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        else
            return "";
    }

    /**
     * 上传文件
     * @param file
     * @return 上传是否成功
     */
    @PostMapping("/upload")
    @ResponseBody
    public boolean handleFileUpload(@RequestParam("file") MultipartFile file) {
        return storageService.store(file);
    }

    // 用于处理StorageFileNotFoundException异常。
    // 当抛出该异常时，函数会返回一个ResponseEntity对象，其状态码为404 Not Found，
    // 表示找不到指定的文件或资源。
    // 该函数通过@ExceptionHandler注解指定用于处理特定类型的异常
    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}