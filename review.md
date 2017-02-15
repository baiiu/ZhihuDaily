## 重构笔记
2016.7.7

- review with Dagger2,make repository singleton
最新版使用了dagger2,将repository放在application中变成单例,网络部分并没有动,因为封装的比较好,没有再使用Dagger2注入.
- let the recyclerView move fast to top
添加快速滑动到顶部,默认的当距离比较长时滑动太慢.


2017.2.15
- 着手将数据层抽出，合并ListRepository和DetailRepository，作为一个模块Repository看待

数据层所负责的是增删改查，不要参与业务逻辑。
