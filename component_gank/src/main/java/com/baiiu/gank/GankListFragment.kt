package com.baiiu.gank

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.baiiu.common.base.BaseFragment
import kotlinx.android.synthetic.main.gank_fragment_gank.*

/**
 * author: zhuzhe
 * time: 2020-01-08
 * description:
 *
 * LiveData 和 LifecycleOwner 处理生命周期
 *
 * ViewModelProviders封装mViewModelStore，获取ViewModule，destroy时自动释放引用
 * 业务上继承ViewModel，实现业务逻辑，通过观察者模式发出，UI层观察到后进行响应。
 */
class GankListFragment : BaseFragment() {

    val viewModel by lazy {
        ViewModelProviders.of(this).get(SimpleModel::class.java)
    }

    override fun provideLayoutId(): Int {
        return R.layout.gank_fragment_gank
    }


    override fun initOnCreateView() {
        super.initOnCreateView()

        viewModel.start()

        viewModel.strLiveData.observe(this, Observer {
            it?.also {

                android.util.Log.e("mLogU", "observe: $it")

                textView.text = it
            }
        })
    }

}