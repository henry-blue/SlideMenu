package slidemenu.app.com.library;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class SlideMenuLayout extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private ViewGroup mforegroundView;
    private ViewGroup mbackgroundView;

    public SlideMenuLayout(Context context) {
        this(context, null);
    }

    public SlideMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mViewDragHelper = ViewDragHelper.create(SlideMenuLayout.this, new SlideCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() < 2){
            throw new IllegalStateException("SlideMenuLayout must have two child at least.");
        }
        if(!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("Your child must be an instance of ViewGroup");
        }
        mforegroundView = (ViewGroup) getChildAt(0);
        mbackgroundView = (ViewGroup) getChildAt(1);
        bringChildToFront(mforegroundView);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private class SlideCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }
    }
}
