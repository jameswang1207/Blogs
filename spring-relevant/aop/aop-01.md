## spring aop 源码解析

#### 资源连接
* [博客](https://blog.csdn.net/moreevan/article/details/11977115/)

#### uml 中类间的关系
* Thank in uml 很详细

#### AOP使用场景
* Authentication 权限
* Caching 缓存
* Context passing 内容传递
* Error handling 错误处理
* Lazy loading　懒加载
* Debugging　　调试
* Logging, tracing, profiling and monitoring　记录跟踪　优化　校准
* Performance optimization　性能优化
* Persistence　　持久化
* Resource pooling　资源池
* Synchronization　同步
* Transactions 事务

#### 相关概念
* 方面（Aspect）：`一个关注点的模块化，这个关注点实现可能另外横切多个对象`。事务管理是J2EE应用中一个很好的横切关注点例子。方面用Spring的Advisor或拦截器实现。

* 连接点（Joinpoint）: `程序执行过程中明确的点，如方法的调用或特定的异常被抛出`。

* 通知（Advice）: `在特定的连接点，AOP框架执行的动作。各种类型的通知包括“around”、“before”和“throws”通知`。通知类型将在下面讨论。许多AOP框架包括Spring都是以拦截器做通知模型，维护一个“围绕”连接点的拦截器链。Spring中定义了四个advice:BeforeAdvice, AfterAdvice, ThrowAdvice和DynamicIntroductionAdvice

* 切入点（Pointcut）: `指定一个通知将被引发的一系列连接点的集合`AOP框架必须允许开发者指定切入点：例如，使用正则表达式。 Spring定义了Pointcut接口，用来组合MethodMatcher和ClassFilter，可以通过名字很清楚的理解， MethodMatcher是用来检查目标类的方法是否可以被应用此通知，而ClassFilter是用来检查Pointcut是否应该应用到目标类上

* Advisor是Pointcut和Advice的配置器.

* 引入（Introduction）: `添加方法或字段到被通知的类。`Spring允许引入新的接口到任何被通知的对象。例如，你可以使用一个引入使任何对象实现IsModified接口，来简化缓存。Spring中要使用Introduction, 可有通过DelegatingIntroductionInterceptor来实现通知，通过DefaultIntroductionAdvisor来配置Advice和代理类要实现的接口

* 目标对象（Target Object）:`包含连接点的对象。`也被称作被通知或被代理对象。POJO

* AOP代理（AOP Proxy）: `AOP框架创建的对象，`包含通知。在Spring中，AOP代理可以是JDK动态代理或者CGLIB代理。

* 织入（Weaving）: `组装方面来创建一个被通知对象。`这可以在编译时完成（例如使用AspectJ编译器），也可以在运行时完成。Spring和其他纯JavaAOP框架一样，在运行时完成织入。

#### 主要类说明

![] (./images/aop-class-relveant.png)

#### 一些问题理解