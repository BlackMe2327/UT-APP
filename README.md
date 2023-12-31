# UT-APP

<p>
  <a href="https://gitee.com/Lewage59/UT-WeChat"><img src="https://img.shields.io/badge/前端项目-UT_WeChat%20-orange.svg"></a>
  <a href="https://docs.spring.io/spring-boot/docs/2.4.3/reference/html/"><img src="https://img.shields.io/badge/Spring%20Boot-2.4.3-brightgreen.svg"></a>
  <a href="https://www.mysql.com/"><img src="https://img.shields.io/badge/Mysql-5.7-bringhtgreen.svg"></a>
  <a href="https://mp.baomidou.com/"><img src="https://img.shields.io/badge/Mybatis_Plus-3.4.2-blue.svg"></a>
  <a href="https://mp.baomidou.com/"><img src="https://img.shields.io/badge/Netty-4.1.42-brightgreen.svg"></a>
    <a href="https://redis.io/"><img src="https://img.shields.io/badge/redis-5.0.x-red.svg"></a>
    <a href="https://www.layui.com/"><img src="https://img.shields.io/badge/layui-2.4.5-red.svg"></a>
    <a href="https://github.com/google/guava"><img src="https://img.shields.io/badge/Guava-28_jre-ff69b4.svg"></a>
    <a href="https://github.com/looly/hutool"><img src="https://img.shields.io/badge/hutool-5.0.3-yellow.svg"></a>
    <a href="https://developer.qiniu.com/kodo/sdk/1239/java"><img src="https://img.shields.io/badge/七牛云_SDK-7.2.18-blue.svg"></a>
</p>

#### 特别说明

- 公司各种事忙，进度比较随缘

#### 介绍 & 各种杂图

参考我的博客链接：[介绍文档](https://wenjie.store/archives/ut%E7%9A%84%E4%BB%8B%E7%BB%8D%E5%92%8C%E6%9D%82%E5%9B%BE)

#### 实现选型

- 考虑到数据的量级只是一个学校，同时为了降低软件部署的成本，目前仅打算使用MySQL、redis实现后端，最终目标是希望一台2G的服务器+七牛cdn就能流畅运行应用（之后会考虑兼容其它cdn，如阿里oss等）
- 日后等熟悉更多开源组件的原理后，会增加不同的框架，开不同的分支来学习。

#### 开发者须知

- 统一使用Gitee：https://gitee.com/wenjie2018/UT-APP （集成了CI/CD）
- 代码风格：风格约束配置请参考 -> https://halo.run/archives/code-style  不懂的也可以问我。
- 提交PR： 提交的PR后都会经过jenkins的编译、打包测试、代码风格检查，通过了会自动在PR下留言测试结果， **请务必保证测试结果通过** （现阶段PR里面一个失败一个成功，可以参考下）。  
- 自定义环境：如果你因为某种原因，需要更改`application.yml`的一些配置，比如开启debug，那么你完全可以在本地的`{user.home}/.ut`目录下创建`application.yml`文件，它可以有选择性地覆盖项目中默认的配置项。

#### 包结构

```
run.ut.app
├── api -- controller层的接口抽象层，主要用于分离Swagger2文档
├── cache -- 跟缓存有关的业务
├── config -- 项目相关的配置类
├── controller -- 存放controller
├── core -- 目前是放对返回对象的增强的逻辑
├── event -- 自定义各种时间
├── exception -- 异常定义、处理相关。
├── handler -- 一些处理器
├── listener -- 监听事件并处理
├── run.ut.mail -- 邮件相关代码
├── mapper -- MyBaits的mapper
├── model -- 存放DOMAIN、DTO、VO、Param等实体
├── netty -- netty的一些代码，目前只用于搭建WebSocket服务
├── schedule -- 存放定时任务，目前的定时任务大多都是测试用
├── security -- 自定义认证拦截逻辑
├── service -- 业务层代码
└── utils -- 工具类

```

#### 计划实现

- ✅ websocket实时推送消息（Netty实现）
- ✅ 组队
- ✅ bbs
- ✅ 校园活动发布/订阅
- ✅ 用户个人信息的完善（绑定邮箱等）
- ✅ 后台管理可动态修改一些配置，如oss配置等
- ✅ 用搜索引擎替代部分sql查询（在re-build分支完成了）
- ✅ 活动添加分类
- ✅ CI/CD 代码风格检查
- ⏰ 聊天（文字传输✅，其余⏰）
- ⏰ 增加接口测试用例，便于后续测试迭代（目前进行ing⏰）
> 因为未来打算重构前端，所以初步估计半年内应该是不会更新后端了，对IM实现有兴趣的可以参考学习下[野火IM](https://github.com/wildfirechat/server)或者tio的开源，以后我也是打算像他们学习的。
- ❌ ~~队伍要半数或以上同意才解散~~
- ❌ ~~已zookeeper+dubbo为辅助，将消息推送拆出来（在这个分支不打算拆）~~

（到此再稳定下现有的功能后就相当于第一个Release版本了，大概~）

---

- 校园活动内容支持markdown...ing（待讨论）
- 举报机制..ing（计划中，因为客服功能的存在，有可能不需要）
- 活动开始前一天或一段时间，发送邮件推送提醒...ing（待讨论）
- ❌ ~~上传的图片支持存到本地磁盘...（用户体验问题）~~
- ...更多

以上功能优先级从上至下递减

#### 参与贡献

1.  Fork 本仓库
2.  新建 UT_APP_XXX 分支
3.  提交代码
4.  新建 Pull Request

#### 鸣谢

- 特别感谢[@施晓权](https://gitee.com/sxq2017)提供的服务器，用于部署jenkins等应用。

#### 字节内推

- 内推传送门：https://job.toutiao.com/s/evCFDkN



