<a name="readme-top"></a>

# hiprint-transit-java

`hiprint-transit-java` 是一个基于node-hiprint-transit与node-hiprint-pdf基底开发，用于在 `electron-hiprint` 客户端和 `vue-plugin-hiprint`
库之间充当中转服务，实现这些组件之间的无缝连接和打印操作以及用于在 HTML、PDF、图像(PNG)中生成 vue - plugin - hiprint 打印模板的 Java 服务器。

<p align="center">
 <img src="https://img.shields.io/badge/SpringBoot-2.7.18-green.svg"/>
 <img src="https://img.shields.io/badge/Java-8-blue.svg"/>
 <img src="https://img.shields.io/badge/SpringMVC-5.3.31-blue.svg"/>
 <img src="https://img.shields.io/badge/Commons Lang3-3.19.0-blue.svg"/>
 <img src="https://img.shields.io/badge/Commons IO-2.20.0-blue.svg"/>
 <img src="https://img.shields.io/badge/Guava-33.5.0 jre-blue.svg"/>
 <img src="https://img.shields.io/badge/Playwright-1.55.0-blue.svg"/>
 <img src="https://img.shields.io/badge/X EasyPdf PdfBox-1.55.0-blue.svg"/>
 <img src="https://img.shields.io/badge/NettySocketio-2.0.13-red.svg"/>
</p>

## 免费服务-用爱发电

| 版本    | 服务器信息         | 服务商           | 地域 | 有效期        | 服务器地址                       | Token     |
|-------|---------------|---------------|----|------------|-----------------------------|-----------|
| 0.0.5 | 2C2G4M 300G/m | Tencent Cloud | GZ | 2026-07-16 | https://v5.printjs.cn:17521 | hiprint\* |

以上服务免费开放，0.0.3 版本未进行 token 隔离，仅推荐用于开发测试。

本项目免费开源，承诺不会窃取以上免费服务运行中产生的任何数据，但不能保证服务过程中不会受到黑客攻击而导致数据泄露问题。

如果你的服务追求更高的 **稳定性、安全性、可靠性**，建议自行独立部署，有自研能力的团队可以使用后端语言重新编写。

## 使用 JDK 启动项目

### 1. 命令执行

```shell
 nohup java -Dname=hiprint-transporter.jar  -Duser.timezone=Asia/Shanghai -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC -jar hiprint-transporter.jar --spring.config.location=config/application.yml 
```

## 配置

首次使用时，您需要需要修改配置文件

这将会引导你一步步完成初始化

./resources/application-prod.yml

```yaml
hiprint:
  host: 0.0.0.0
  # 服务端端口
  port: 17521
  epoll: false
  # 服务端token
  auth-token: print_mes
  # 附件存储路径（/root/upload/files---liunx配置）
  active: D:/root/upload/files/
```

如果你的配置有误或需要调整修改端口、token值，只需要重启即可。
（eg: 个人习惯都是把配置文件单独放在一个目录，方便修改）

## events打印注意：

1. 从 `hiwebSocket` 中发送 `news`、`render-print` 、`render-jpeg`、`render-pdf` ，目前参数接收为Object参数（其他参数会报错），
2. `hiwebSocket`进行emit时对应提交事件名称(
   参考文档[docs](https://gitee.com/CcSimple/vue-plugin-hiprint/blob/main/apiDoc.md#312-render-api-%E8%8E%B7%E5%8F%96-jpegpdf%E6%89%93%E5%8D%B0))

**eg:（新增render类型事件回调成功、失败数据显示）**

   ```js
// java对应的注册事件注解中引用对应的是emit仲提交事件名称
`@OnEvent("news")`, `@OnEvent("render-print")` , `@OnEvent("render-jpeg")`, `@OnEvent("render-pdf")`
// hiwebSocket之间提交事件
const socket = hiwebSocket.socket;
socket.emit("render-jpeg", {
    template: panel, // 模板对象
    data: printData, // Object打印数据
});
   ```

> 如果你不提供 client 中转服务将抛出一个 error

## Window 系统启动项目

### 1. 下载打包文件

[点击下载](https://gitee.com/Xavier9896/node-hiprint-transit/blob/main/out/transit-setup-0.0.5.exe)

### 2. 指定解压缩路径

### 3. 运行 `start.bat` 脚本

### 如果该项目对你有所帮助，请给我一个 star，谢谢！

## 模板生成 矢量 pdf、image、html

## API {{模板载入}}
**功能描述**: {{该接口模板JSON转换成PDF、图像(PNG)矢量附件，以及HTML结点获取}

**提示**

    基于playwright 实现
    首次使用会自动下载开源浏览器，并初始化
    若转换内容有缺失，可尝试调整页面加载状态
    options模板json数据缓存是两个小时； 
    附件下载与预览url缓存为永久不失效，获取到url时，务必保存，请勿丢失


```java
  // 初步构建html转换器时候
  HtmlConvertor convertor = PdfHandler.getDocumentConvertor().getHtmlConvertor();
  convertor.setPageState(PageLoadState.NETWORKIDLE);
```

### 请求说明
**请求地址**
```http request
POST /api/template/load
```
### 参数说明
**query参数**

| 参数名  |   类型   | 必填 |             描述             |
|:----:|:------:|:--:|:--------------------------:|
| type | string | 否  | 类型参数image、pdf、html（默认为pdf） |


**Body参数**
```Body
{
  "domId": "#hiprintTemplate",    string               节点参数(type为html是，此参数比填)
  "printData":{},                 map<string,object>   模板数据
  "template": {}                  map<string,object>   模板JSON
}
```

**响应参数**
```response
{
  "code": 200,
  "msg": "success",
  "data": {
    "html": "",             string     type为html会返回该参数，节点html
    "downloadUrl": "",      string     附件下载url
    "viewUrl": ""           string     附件预览url
  }
}
```
> eg:
> 考虑构思每日定时清理附件生成问题，期待....

## 周边生态项目

| 项目名称                 | 项目地址                                                                                                                    | 下载地址                                                              | 描述                                                    |
|----------------------|-------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------|-------------------------------------------------------|
| vue-plugin-hiprint   | [github](https://github.com/CcSimple/vue-plugin-hiprint)、[gitee](https://gitee.com/CcSimple/vue-plugin-hiprint)         | [npm](https://www.npmjs.com/package/vue-plugin-hiprint)           | 打印设计器                                                 |
| electron-hiprint     | [github](https://github.com/CcSimple/electron-hiprint)、[gitee](https://gitee.com/CcSimple/electron-hiprint)             | [releases](https://github.com/CcSimple/electron-hiprint/releases) | 直接打印客户端                                               |
| node-hiprint-transit | [github](https://github.com/Xavier9896/node-hiprint-transit)、[gitee](https://gitee.com/Xavier9896/node-hiprint-transit) | -                                                                 | web 与客户端中转服务 Node 实现                                  | | -                                                                 | web 与客户端中转服务 Java 实现                                     |
| uni-app-hiprint      | [github](https://github.com/Xavier9896/uni-app-hiprint)                                                                 | -                                                                 | uni-app 项目通过 webview 使用 vue-plugin-hiprint demo       |
| node-hiprint-pdf     | [github](https://github.com/CcSimple/node-hiprint-pdf)                                                                  | -                                                                 | 提供通过 node 对 vue-plugin-hiprint 模板生成 矢量 pdf、image、html |

