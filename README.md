# Simple annotation-based IoC/DI container

Available in [Maven Central](https://mvnrepository.com/artifact/com.github.isaichkindanila/ioc)

### Usage:
1. Annotate your beans with `@Bean`
0. Compile with annotation processing enabled
0. Create bean container with `BeanContainer.newInstance(Object...)`
0. Get beans with `beanContainer.getBean(Class)`
