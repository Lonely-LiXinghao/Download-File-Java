java.nio.file.Path 是 Java NIO (New I/O) 包中的一个接口，它代表了一个文件系统的路径。

Path 接口定义了一系列的方法来处理文件路径。

下面列出了 Path 接口中的一些常用方法及其简要说明：

1. 获取路径信息

- getName(int index) - 返回指定索引位置的名称。

- getFileName() - 返回路径的最后一部分，通常是指定文件的名称。

- getParent() - 返回路径的父路径。

- getRoot() - 返回路径的根部分。
- getNameCount() - 返回路径中的名称数量。
- startsWith(Path other) - 判断此路径是否以给定路径开始。
- startsWith(String other) - 判断此路径是否以给定字符串开始。
- endsWith(Path other) - 判断此路径是否以给定路径结束。
- endsWith(String other) - 判断此路径是否以给定字符串结束。
- normalize() - 将路径规范化，移除多余的元素如 "." 和 ".."。
- toAbsolutePath() - 将路径转换为绝对路径。
- toRealPath(LinkOption... options) - 解析符号链接，返回真实的文件路径。
- toUri() - 将路径转换为 URI。
- toFile() - 将路径转换为 File 对象。



2. 路径操作

- resolve(Path other) - 将给定的路径附加到当前路径之后。

- resolve(String other) - 将给定的字符串附加到当前路径之后。

- resolveSibling(Path other) - 解析相对于当前路径同级目录的路径。

- relativize(Path other) - 计算从当前路径到给定路径的相对路径。

- compareTo(Path other) - 比较此路径与另一个路径。



3. 格式化和解析

- format(String format) - 格式化路径。
- parse(String text) - 解析路径文本。



4. 文件系统相关

- getFileSystem() - 获取路径所属的文件系统。
- isAbsolute() - 判断路径是否为绝对路径。
- equals(Object obj) - 判断路径是否与另一个对象相等。
- hashCode() - 返回路径的哈希码值。



5. 其他

- toString() - 返回路径的字符串表示形式。



Path 接口本身不实现任何方法，而是由其实现类如 java.nio.file.Paths 提供具体实现。此外，一些操作可能需要通过 java.nio.file.Files 类来完成，例如读取文件内容、写入文件、删除文件等。

