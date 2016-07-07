## ZhihuDaily
This is a project to learn how to build an app with different architecture.
The tags in master shows the evolution of the project step-by-step, You will see this project becomes clear after the MVP.The branch shows the other implementation.

该项目是知乎日报的一个实现,主要用来学习各种项目架构.从最基本的代码(全部代码在Activity中)到MVP等,之后依然会持续更新.
使用tag来表明一步一步重构代码进化过程,从这些分支上可以看到使用MVP的代码是多么清晰.


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

- Latest

    - review with Dagger2,make repository singleton
    - let the recyclerView move fast to top
    
    
    - 最新版使用了dagger2,将repository放在application中变成单例,网络部分并没有动,因为封装的比较好,没有再使用Dagger2注入.
    - 添加快速滑动到顶部,默认的当距离比较长时滑动太慢.


## Branches
- `the master` has a lot of tags, and the first version code is on it.
- `the branch_mvp_variant` is a variant of MVP,which use Fragment/Activity as Presenter. In this way, you can do it with no interfaces, using Generic to decoupled(little hard to understand), and I put `clickListeners` in Presenters, so it looks more clearly. (This branch does not merge into master. I prefer the normal one.)

中文版:

- `master`分支上的tag表示每次重构后的完整代码
- `branch_mvp_variant`分支是 MVP变种 分支,使用Fragment作为Presenter,我把点击事件当做逻辑放在Presenter中,代码简化了不少


## ScreenShot
![ZhihuDaily](images/daily.gif "Gif Example")

## ThanksTo
[知乎日报API](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)<br>
[PureZhihuD](https://github.com/laucherish/PureZhihuD)

## 文章总结
[Android 项目框架 使用MVP开发](http://blog.csdn.net/u014099894/article/details/51388170)<br>
[Android 网络框架 Retrofit2.0介绍、使用和封装](http://blog.csdn.net/u014099894/article/details/51441462)<br>
[Android 项目框架 使用RxJava开发](http://blog.csdn.net/u014099894/article/details/51621858)<br>
[Android 依赖注入框架 Dagger2使用](http://blog.csdn.net/u014099894/article/details/51675362)<br>
