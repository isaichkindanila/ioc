# Simple IoC-container

### Usage:
1. Annotate your beans with `@Bean`
0. Compile with annotation processing enabled
0. Create bean container with `BeanContainer.newInstance()`
0. Add some necessary base beans with `beanContainer.addBean(Object)`
0. Create beans of annotated classes with `beanContainer.init()`
0. Get beans with `beanContainer.getBean(Class)`
