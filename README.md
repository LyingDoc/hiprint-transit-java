<a name="readme-top"></a>

# hiprint-transit-java

`hiprint-transit-java` 是一个基于 node-hiprint-transit 基底开发，用于在 `electron-hiprint` 客户端和 `vue-plugin-hiprint` 库之间充当中转服务，实现这些组件之间的无缝连接和打印操作。

<p align="center">
 <img src="https://img.shields.io/badge/SpringBoot-2.7.18-green.svg"/>
 <img src="https://img.shields.io/badge/Java-8-blue.svg"/>
 <img src="https://img.shields.io/badge/SpringMVC-5.3.31-blue.svg"/>
 <img src="https://img.shields.io/badge/NettySocketio-2.0.13-red.svg"/>
</p>


## 免费服务-用爱发电

| 版本    | 服务器信息     | 服务商        | 地域 | 有效期        | 服务器地址                         | Token              |
|-------| -------------- | ------------- | ---- |------------|-------------------------------| ------------------ |
| 0.0.5 | 2C2G4M 300G/m  | Tencent Cloud | GZ   | 2026-07-16 | https://v5.printjs.cn:17521   | hiprint\*          |

以上服务免费开放，0.0.3 版本未进行 token 隔离，仅推荐用于开发测试。

本项目免费开源，承诺不会窃取以上免费服务运行中产生的任何数据，但不能保证服务过程中不会受到黑客攻击而导致数据泄露问题。

如果你的服务追求更高的 **稳定性、安全性、可靠性**，建议自行独立部署，有自研能力的团队可以使用后端语言重新编写。






##  使用 JDK 启动项目

### 1. 命令执行

```shell
 nohup java -Dname=hiprint-transporter.jar  -Duser.timezone=Asia/Shanghai -Xms512m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps  -XX:+PrintGCDetails -XX:NewRatio=1 -XX:SurvivorRatio=30 -XX:+UseParallelGC -XX:+UseParallelOldGC -jar hiprint-transporter.jar --spring.config.location=config/application.yml 
```



## 配置

首次使用时，您需要需要修改配置文件

这将会引导你一步步完成初始化

```yaml
./resources/application-prod.yml
hiprint:
  host: 0.0.0.0
  # 服务端端口
  port: 17521
  epoll: false
   # 服务端token
  auth-token: print_mes
```

如果你的配置有误或需要调整修改端口、token值，只需要重启即可。
（eg: 个人习惯都是把配置文件单独放在一个目录，方便修改）

## Window 系统启动项目

### 1. 下载打包文件

[点击下载](https://gitee.com/Xavier9896/node-hiprint-transit/blob/main/out/transit-setup-0.0.5.exe)

### 2. 指定解压缩路径

### 3. 运行 `start.bat` 脚本

### 如果该项目对你有所帮助，请给我一个 star，谢谢！

## 周边生态项目

| 项目名称             | 项目地址                                                                                                                 | 下载地址                                                          | 描述                                                               |
| -------------------- | ------------------------------------------------------------------------------------------------------------------------ | ----------------------------------------------------------------- | ------------------------------------------------------------------ |
| vue-plugin-hiprint   | [github](https://github.com/CcSimple/vue-plugin-hiprint)、[gitee](https://gitee.com/CcSimple/vue-plugin-hiprint)         | [npm](https://www.npmjs.com/package/vue-plugin-hiprint)           | 打印设计器                                                         |
| electron-hiprint     | [github](https://github.com/CcSimple/electron-hiprint)、[gitee](https://gitee.com/CcSimple/electron-hiprint)             | [releases](https://github.com/CcSimple/electron-hiprint/releases) | 直接打印客户端                                                     |
| node-hiprint-transit | [github](https://github.com/Xavier9896/node-hiprint-transit)、[gitee](https://gitee.com/Xavier9896/node-hiprint-transit) | -                                                                 | web 与客户端中转服务 Node 实现                                     | | -                                                                 | web 与客户端中转服务 Java 实现                                     |
| uni-app-hiprint      | [github](https://github.com/Xavier9896/uni-app-hiprint)                                                                  | -                                                                 | uni-app 项目通过 webview 使用 vue-plugin-hiprint demo              |
| node-hiprint-pdf     | [github](https://github.com/CcSimple/node-hiprint-pdf)                                                                   | -                                                                 | 提供通过 node 对 vue-plugin-hiprint 模板生成 矢量 pdf、image、html |
