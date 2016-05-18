## ZhihuDaily
This is a project to learn how to build an app with different architecture.
The step-by-step evolution of the project is in one by one different branch and tags in master branch. 

该项目是知乎日报的一个实现,主要用来学习各种项目架构.从最基本的代码(全部代码在Activity中)到MVP等,之后依然会持续更新.
一步一步重构代码进化过程展现在各个分支和master上的tag,从这些分支上可以看到使用MVP的代码是多么清晰.
And more practise to be continued...

文章总结: [Android 项目框架 使用MVP开发](http://blog.csdn.net/u014099894/article/details/51388170)

## Branches
- the `master` branch has a lot of tags, and the first version code is on it.
- the `branch_basic`,which master tag is v1.0,  use an original way to complete the project,which means all code is in the Activity and with no fragments.
- the `branch_allin_fragments`(master tag is v1.1),put all logic in fragments instead of Activity.
- the `branch_mvp` use MVP to review the code, seperating the View and Logic.It is so great,and looks more elegant.
- the `branch_mvp_variant` is a variant of MVP,which use Fragment/Activity as Presenter. In this way, you can do it with no interfaces, using Generic to decoupled(little hard to understand), and I put `clickListeners  ` in Presenters, so it looks more clearly. (This branch does not merge into master. I prefer the normal one.)

中文版:

- `master`分支上的tag表示每次重构后的完整代码
- `branch_basic`分支上的代码是最原始的,所有代码都在Activity中
- `branch_allin_fragment`,将部分逻辑抽到Fragment中,简化了Activity中的代码
- `branch_mvp`分支使用MVP重构了代码,列表页Model层仿照google sample,而详情页model层逻辑简单,写在一个Repository中,你可以看到这两种方式的区别
- `branch_mvp_variant`分支是 MVP变种 分支,使用Fragment作为Presenter,我把点击事件当做逻辑放在Presenter中,代码简化了不少

## Version
- v1.0
version 1.0, the basic code for all variants
全部代码在Activity中,作为重构的基础

- v1.1
将部分代码抽离到Fragment中,为下一步打基础

- v1.2
使用MVP重构,List页面参考Google samples的三层架构,Detail页面没有把remote和locale分开,你可以看到这两者的区别.




## ScreenShot
![ZhihuDaily](images/daily.gif "Gif Example")

## ThanksTo
[知乎日报API](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)<br>
[PureZhihuD](https://github.com/laucherish/PureZhihuD)