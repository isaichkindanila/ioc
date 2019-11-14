# Simple annotation-based IoC/DI container

Available in [Maven Central](https://mvnrepository.com/artifact/com.github.isaichkindanila/ioc)

### Usage:
1. Annotate your components with `@Component`
0. Create container with `ComponentContainer.newInstance(String, Object...)`
0. Get components with `container.getComponent(Class)`
