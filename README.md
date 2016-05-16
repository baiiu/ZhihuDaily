## ZhihuDaily
This is a project to learn how to build an app with different architecture.
The step-by-step evolution of the project is in one by one different branch and tags in master branch. 
该项目是知乎日报的一个实现,主要用来学习各种项目架构.从最基本的代码(全部代码在Activity中)到MVP等,之后依然会持续更新.
一步一步重构代码进化过程展现在各个分支和master上的tag,从这些分支上可以看到使用MVP的代码是多么清晰.

## Branches
- the `master` branch has a lot of tags, and the first version code is on it.
- the `branch_basic`,which master tag is v1.0,  use an original way to complete the project,which means all code is in the Activity and with no fragments.
- the `branch_allin_fragments(master tag is v1.1)`,put all logic in fragments instead of Activity.
- the `branch_mvp` use MVP to review the code, seperating the View and Logic.It is so great,and looks more elegant.
- the `branch-mvp_variant` is a variant of MVP,which use Fragment/Activity as Presenter. In this way, you can do it with no interfaces, using Generic to decoupled(little hard to understand), and I put `clickListeners  ` in Presenters, so it looks more clearly. (This branch does not merge into master. I prefer the normal one.)

## ScreenShot
![ZhihuDaily](images/daily.gif "Gif Example")

## ThanksTo
[知乎日报API](https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90)
[PureZhihuD](https://github.com/laucherish/PureZhihuD)