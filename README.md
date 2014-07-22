#ThinkAndroid 交流平台
* QQ群：367818514(交流群) 
* 网址：http://blog.csdn.net/hrx3627

----

#  BlueskyAndroid简介
BlueskyAndroid 是一个积累了将近几年Android开发经验，经过不断修改、优化、总结而形成的Android开发库。现将它开源共享，希望广大技术同胞来一起进行优化、修改、打造出一个很好的快速开发Android客户端的开源库。让更多的Android从业者有更多经历和时间放在业务了解上。
BlueskyAndroid 宗旨：用心写代码，态度决定出路。
BlueskyAndroid 愿景：让更多Android从业者轻松搞定工作。
BlueskyAndroid 包含：支持http请求、数据库操作、图片下载（缓存）、软件更新下载等不同模块以及Android开发过程中所用的各种工具类。
BlueskyAndroid 兼容：android 2.1 (api level 7)

#  目前BlueskyAndroid主要模块：
## 数据库模块：

  1、android中的orm框架，使用了线程池对sqlite进行操作。
  2、直接保存对象。
  3、调用简单，几行代驾就可以保存数据。
  4、支持数据库增删改查。


---
## 更新下载模块：

android软件检测更新；
支持检测更新，可以进行强制更新和选择更新两种方式；
实现更新，根据URL下载新版本APK进行安装；


----
## HttpUtils模块：
支持异步方式的请求；
支持大文件上传，上传大文件不会oom；
支持GET，POST，PUT，MOVE，COPY，DELETE，HEAD，OPTIONS，TRACE，CONNECT请求；
支持自定义头部文件
请求简单、结构清晰

----

## 图片下载（缓存）模块：
加载bitmap的时候无需考虑bitmap加载过程中出现的oom和android容器快速滑动时候出现的图片错位等现象；
支持加载网络图片及缓存本地；
内存管理使用lru算法，更好的管理bitmap内存；


----
## 其他工具类：
包含大量实用工具类
字符串工具类：StringUtils
MD5加密工具类
网络判断工具类嘞和强制打开GPS和网络工具类
文件操作工具类
总之，很多实用工具类，用了你就知道
...



----
# 关于作者【Bluesky】
*  BlueskyAndroid交流网站：http://blog.csdn.net/hrx3627
*  BlueskyAndroid交流QQ群 ：367818514

