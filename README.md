## ZhihuDaily
This is a project to learn how to build an app with different architecture.
The step-by-step evolution of the project is in one by one different branch and tags in master branch. 

该项目是知乎日报的一个实现,主要用来学习各种项目架构.从最基本的代码(全部代码在Activity中)到MVP等,之后依然会持续更新.
一步一步重构代码进化过程展现在各个分支和master上的tag,从这些分支上可以看到使用MVP的代码是多么清晰.

## Branches
- the `master` branch has a lot of tags, and the first version code is on it.
master分支上的tag表示每次重构后的完整代码
- the `branch_basic`,which master tag is v1.0,  use an original way to complete the project,which means all code is in the Activity and with no fragments.
branch_basic分支上的代码是最原始的,所有代码都在Activity中
- the `branch_allin_fragments`(master tag is v1.1),put all logic in fragments instead of Activity.
branch_allin_fragment,将部分逻辑抽到Fragment中,简化了Activity中的代码
- the `branch_mvp` use MVP to review the code, seperating the View and Logic.It is so great,and looks more elegant.
branch_mvp分支使用MVP重构了代码,列表页Model层仿照google sample,而详情页model层逻辑简单,写在一个Repository中,你可以看到这两种方式的区别
- the `branch_mvp_variant` is a variant of MVP,which use Fragment/Activity as Presenter. In this way, you can do it with no interfaces, using Generic to decoupled(little hard to understand), and I put `clickListeners  ` in Presenters, so it looks more clearly. (This branch does not merge into master. I prefer the normal one.)
branch_mvp_variant分支是 MVP变种 分支,使用Fragment作为Presenter,我把点击事件当做逻辑放在Presenter中,代码简化了不少

## ScreenShot
![ZhihuDaily](images/daily.gif "Gif Example")

## ThanksTo
[知乎日报API](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)
[PureZhihuD](https://github.com/laucherish/PureZhihuD)