package in.srain.cube.views.ptr;

import android.content.Context;
import android.util.AttributeSet;

public class PtrClassicFrameLayout extends PtrFrameLayout {

    private PtrClassicDefaultHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
        //addPtrUIHandler(new PtrUIHandler() {
        //    @Override public void onUIReset(PtrFrameLayout frame) {
        //
        //    }
        //
        //    @Override public void onUIRefreshPrepare(PtrFrameLayout frame) {
        //
        //    }
        //
        //    @Override public void onUIRefreshBegin(PtrFrameLayout frame) {
        //
        //    }
        //
        //    @Override public void onUIRefreshComplete(PtrFrameLayout frame) {
        //        mPtrClassicHeader.toggleInfo(false);
        //    }
        //
        //    @Override public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status,
        //            PtrIndicator ptrIndicator) {
        //
        //    }
        //});
    }

    public PtrClassicDefaultHeader getHeader() {
        return mPtrClassicHeader;
    }

    /**
     * Specify the last update time by this key string
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }
}
