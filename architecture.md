## 组件化需要考虑的问题

1. 各个组件独立运行，可动态配置依赖关系
- 组件单独运行
- 某一个组件依赖别的几个组件运行
- 代码隔离: 不能直接可见方法，必须通过中间件获取暴露的方法

实现：
  注解？
  extension?

2. 组件间通信
- startActivity
- startActivityForResult
- 调用另外组件方法

3. 组件生命周期
- 初始化顺序
- 加载、卸载
- 降维，可配置成h5



> 参考：
https://www.jianshu.com/p/1b1d77f58e84
https://juejin.im/post/5bb9c0d55188255c7566e1e2
https://tech.youzan.com/you-zan-androidzu-jian-hua-bian-yi-ti-xiao-fang-an/