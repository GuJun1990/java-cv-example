# javacv的实例代码

## 减少依赖项
原始二进制本质上是很大的，应为jar包是给所有平台使用的。所以在确定了使用平台之后应该尽量的排除其他平台的代码，以减少jar包的大小。
打包的时候指定`javacpp.platform`属性，打包命令如下：
```shell script
mvn -Djavacpp.platform=linux-x86_64 ...
```
