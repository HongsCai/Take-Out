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



