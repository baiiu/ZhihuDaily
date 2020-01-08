package com.baiiu.gank

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.baiiu.common.base.BaseFragment
import kotlinx.android.synthetic.main.gank_fragment_gank.*

/**
 * author: zhuzhe
 * time: 2020-01-08
 * description:
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