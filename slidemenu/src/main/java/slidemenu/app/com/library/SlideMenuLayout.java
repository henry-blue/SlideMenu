package slidemenu.app.com.library;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;


public class SlideMenuLayout extends FrameLayout {

    private ViewDragHelper mViewDragHelper;
    private ViewGroup mforegroundView;
    private ViewGroup mbackgroundView;
    private int mHeight;
    private int mWidth;
    private int mRange;
    private OnSlideMenuStatusListener mListener;
    private Status mStatus;

    public static enum Status {
        Open, Close, Draging
    }

    public interface OnSlideMenuStatusListener {
        void OnStartOpenMenu();
        void OnOpenedMenu();
        void OnStartCloseMenu();
        void OnClosedMenu();
        void OnSlidingMenu();
    }

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
        mStatus = Status.Close;
        mViewDragHelper = ViewDragHelper.create(SlideMenuLayout.this, new SlideCallback());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setLayout(false);
        bringChildToFront(mforegroundView);
    }

    private void setLayout(boolean isOpen) {
        Rect frontRect = computeFrontViewRect(isOpen);
        mforegroundView.layout(frontRect.left, frontRect.top, frontRect.right, frontRect.bottom);
        Rect backRect = computeBackViewViaFront(frontRect);
        mbackgroundView.layout(backRect.left, backRect.top, backRect.right, backRect.bottom);
    }

    private Rect computeFrontViewRect(boolean isOpen) {
        int left = 0;
        if (isOpen) {
            left = -mRange;
        }
        return new Rect(left, 0, left + mWidth, mHeight);
    }

    private Rect computeBackViewViaFront(Rect frontRect) {
        int left = frontRect.right;
        return new Rect(left, 0, left + mRange, mHeight);
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
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = mforegroundView.getMeasuredHeight();
        mWidth = mforegroundView.getMeasuredWidth();
        mRange = mbackgroundView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mViewDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    protected void dispatchSwipeEvent() {
        if (mListener != null) {
            mListener.OnSlidingMenu();
        }

        Status lastStatus = mStatus;
        mStatus = updateStatus();
        if (lastStatus != mStatus && mListener != null) {
            if (mStatus == Status.Close) {
                mListener.OnClosedMenu();
            } else if (mStatus == Status.Open) {
                mListener.OnOpenedMenu();
            } else if (mStatus == Status.Draging) {
                if (lastStatus == Status.Close) {
                    mListener.OnStartOpenMenu();
                } else if (lastStatus == Status.Open) {
                    mListener.OnStartCloseMenu();
                }
            }
        }
    }

    private Status updateStatus() {
        int left = mforegroundView.getLeft();
        if (left == 0) {
            return Status.Close;
        } else if (left == -mRange) {
            return Status.Open;
        }
        return Status.Draging;
    }

    public void SetOnSlideMenuStatusListener(OnSlideMenuStatusListener listener) {
        mListener = listener;
    }

    public void openMenu(boolean isSmooth) {
        int finalLeft = -mRange;
        if (isSmooth) {
            if (mViewDragHelper.smoothSlideViewTo(mforegroundView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            setLayout(true);
        }
    }

    public void closeMenu(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            if (mViewDragHelper.smoothSlideViewTo(mforegroundView, finalLeft, 0)) {
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            setLayout(false);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void openMenu() {
        openMenu(true);
    }

    private void closeMenu() {
        closeMenu(true);
    }

    private class SlideCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int clampRange = left;
            if (child == mforegroundView) {
                if (left > 0) {
                    clampRange = 0;
                } else if (left < -mRange) {
                    clampRange = -mRange;
                }
            } else if (child == mbackgroundView) {
                if (left > mWidth) {
                    clampRange = mWidth;
                } else if (left < mWidth - mRange) {
                    clampRange = mWidth - mRange;
                }
            }
            return clampRange;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView == mforegroundView) {
                mbackgroundView.offsetLeftAndRight(dx);
            } else if (changedView == mbackgroundView) {
                mforegroundView.offsetLeftAndRight(dx);
            }
            dispatchSwipeEvent();
            invalidate();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel == 0 && mforegroundView.getLeft() < -mRange / 2.0f) {
                openMenu();
            } else if (xvel < 0) {
                openMenu();
            } else {
                closeMenu();
            }
        }
    }

}
