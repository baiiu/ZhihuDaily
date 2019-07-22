package com.baiiu.common.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.baiiu.common.base.mvp.MvpView;
import com.baiiu.common.util.UIUtil;

/**
 * Created by baiiu on 2015/11/16.
 * Base
 */
public abstract class BaseFragment extends Fragment implements MvpView {
    /**
     * 绑定到当前的attach的activity上.可强转
     */
    public Context mContext;
    protected View view;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(provideLayoutId(), container, false);
        }
        initView(view);
        initOnCreateView();
        return view;
    }

    protected void initView(View view) {
    }

    public abstract int provideLayoutId();

    protected void initOnCreateView() {
    }

    @Override public boolean isAlive() {
        //noinspection SimplifiableIfStatement
        if (getActivity() != null && !getActivity().isFinishing()) {
            return true;
        }

        return isAdded();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        //        ButterKnife.unbind(this);不要回收,会造成空指针异常
    }


    @Override public void onDestroy() {
        super.onDestroy();
    }
}
