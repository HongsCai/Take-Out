# 2025年12月4日

## 登录失败无报错提示信息

问题：

![image-20251204182051002](./assets/image-20251204182051002.png)

输入错误密码无错误提示，且有报错

![image-20251204182224874](./assets/image-20251204182224874.png)



解决：

异常未继承BaseException，但调用了BaseException的方法

![image-20251204182554228](./assets/image-20251204182554228.png)

全局异常处理器使用ExceptionHandler注解，默认捕获RuntimeException异常

![image-20251204182352897](./assets/image-20251204182352897.png)

指定捕获异常类型

![image-20251204183102134](./assets/image-20251204183102134.png)



# 2025年12月5日

## 使用Knife4j出现文档请求异常

springboot3.x以上就需要换knife4j依赖为4.x版本的knife4j-openapi3-jakarta-spring-boot-starter依赖

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.4.0</version>
</dependency>
```

由于从Swagger2变更到Openapi3的标准，故注解以及配置类全部重新该

> 由于Springboot版本为3.5.8不支持Knife4j最新的4.4.0版本，故将Springboot版本降低为3.2.5（悲

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.5</version>
    </parent>
```

![image-20251205184952133](./assets/image-20251205184952133.png)
