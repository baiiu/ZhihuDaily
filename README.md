## ZhihuDaily
This is a project to learn how to build an app with different architecture.
The tags in master shows the evolution of the project step-by-step, You will see this project becomes more clear than before after using the MVP.The branch shows the other implementation.
As well, the project is continue refactoring.

该项目是知乎日报的一个实现,主要用来学习各种项目架构.从最基本的代码(全部代码在Activity中)到MVP等,之后依然会持续更新.
使用tag来表明一步一步重构代码进化过程,从这些分支上可以看到使用MVP的代码是多么清晰.
项目代码会不断更新，不断进行重构。


## Version
- v1.0

    version 1.0,all codes are in the Activity and with no fragments, the basic code for all variants.
    全部代码在Activity中,作为重构的基础

- v1.1

    pull all codes out from activity into fragment
    将部分代码抽离到Fragment中,为下一步打基础

- v1.2

    review with mvp,making the View and Logic separating from each other.It is so great,and looks more elegant.
    使用MVP重构,List页面参考Google samples的三层架构,Detail页面没有把remote和locale分开,你可以看到这两者的区别.

- v1.3

    review with RxJava, based on the MVP,also with Retrofit
    在MVP基础上使用RxJava重构,同时网络框架替换为Retrofit.

     - 添加Presenter生命周期方法,attachView()和 detachView()方法对 `Subscription` 解绑.
     - 此中不把Activity当做Controller,因为这样做限制太大,直接在Fragment中进行创建和attachView

- v1.4

    make it componentization, better than before. But there is still some problem with component.
    The component cannot compile other components,it must be run as a single application, which means the project
    must run as all in application or an application with other modules.
    将项目初步进行组件化。
    这种结构下的组件化不能做到组件依赖其他组件运行。因为当前项目运行的状态要么是全是application，或者是一个application和其他module.

- v1.5

  make the componentization more beautiful to run. every component can run alone, and can be run
  with other components, also each components is separate from each other.
  完善组件化。通过配置runalone.json使各个组件能单独运行；各个组件能选取依赖项；各个组件互相隔离；


## Branches
- `the master` has a lot of tags, and the first version code is on it.
- `the branch_mvp_variant` is a variant of MVP,which use Fragment/Activity as Presenter. In this way, you can do it with no interfaces, using Generic to decoupled(little hard to understand), and I put `clickListeners` in Presenters, so it looks more clearly. (This branch does not merge into master. I prefer the normal one.)
- `the branch_rxcache` replace GreenDao with RxCache to save data 

中文版:

- `master`分支上的tag表示每次重构后的完整代码
- `branch_mvp_variant`分支是 MVP变种 分支,使用Fragment作为Presenter,我把点击事件当做逻辑放在Presenter中,代码简化了不少
- `the branch_rxcache` 替换了GreenDao，使用RxCache来做缓存

## ScreenShot
![ZhihuDaily](images/daily.gif "Gif Example")

## TODO
- 17/10/26
使用GreenDao造成了多个实体类，如在项目中GreenDao生成的SavedStory一系列，和Story重复，而此时又没有使用Clean架构来使用该bean，造成重复和难以理解。
所以这也算GreenDao的一个弊端吧。
将会使用liteOrm或其他一些数据库来解决该问题。

- 17/11/1
在v1.4版本基础上，需要再次进行组件化，使用gradle插件将组件完全分离。
模仿[DDComponentForAndroid](https://github.com/luojilab/DDComponentForAndroid)


## ThanksTo
[知乎日报API](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)<br>
[PureZhihuD](https://github.com/laucherish/PureZhihuD)

## 文章总结
[Android 项目框架 使用MVP开发](http://blog.csdn.net/u014099894/article/details/51388170)<br>
[Android 网络框架 Retrofit2.0介绍、使用和封装](http://blog.csdn.net/u014099894/article/details/51441462)<br>
[Android 项目框架 使用RxJava开发](http://blog.csdn.net/u014099894/article/details/51621858)<br>
[Android 依赖注入框架 Dagger2使用](http://blog.csdn.net/u014099894/article/details/51675362)<br>

## Download
[fir.im](http://fir.im/159600?release_id=57a8a7e2959d6913c8000022)
![downLoad](images/ZhihuDaily.png "download")