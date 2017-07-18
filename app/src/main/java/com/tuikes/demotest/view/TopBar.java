package com.tuikes.demotest.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tuikes.demotest.R;
import com.tuikes.demotest.utils.CommonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 顶部标题栏
 * Created by chendx on 2017/5/23.
 */

public class TopBar extends FrameLayout {
    /** 左边的文本控件 */
    public static final int FUNCTION_TEXT_LEFT = 1;

    /** 右边的文本控件 */
    public static final int FUNCTION_TEXT_RIGHT = 2;

    /** 中间的文本控件 */
    public static final int FUNCTION_TEXT_TITLE = 4;

    /** 左边的按钮 */
    public static final int FUNCTION_BUTTON_LEFT = 8;

    /** 右边的按钮 */
    public static final int FUNCTION_BUTTON_RIGHT = 16;

    @BindView(R.id.my_status_bar)
    MyStatusBar myStatusBar;

    @BindView(R.id.left_tx)
    TextView leftTx;

    @BindView(R.id.left_btn)
    ImageView leftBtn;

    @BindView(R.id.title_tx)
    TextView titleTx;

    @BindView(R.id.right_tx)
    TextView rightTx;

    @BindView(R.id.right_btn)
    ImageView rightBtn;

    @BindView(R.id.title_container)
    RelativeLayout mContainer;

    /** 默认打开的功能 */
    private int currentFunction = 0;
    private OnTopBarClickListener onTopBarClickListener;

    public void setOnTopBarClickListener(OnTopBarClickListener onTopBarClickListener) {
        this.onTopBarClickListener = onTopBarClickListener;
    }

    public TopBar(@NonNull Context context) {
        this(context, null);
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.view_top_bar, this, true);
        ButterKnife.bind(this, view);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        //状态栏背景色
        setMyStatusBarBgColor(typedArray.getColor(R.styleable.TopBar_bg_statusBar, Color.parseColor("#111111")));
        //标题栏背景
        Drawable bg = typedArray.getDrawable(R.styleable.TopBar_mBackground);
        if (bg == null) {
            bg = new ColorDrawable(Color.parseColor("#0EA3FF"));
        }

        int function = typedArray.getInt(R.styleable.TopBar_function, 0);
        setFunction(function);

        mContainer.setBackground(bg);
        //设置左边文字
        String leftTx = typedArray.getString(R.styleable.TopBar_txLeft);
        setLeftTx(leftTx);
        int colorLeftTx = typedArray.getColor(R.styleable.TopBar_txLeftColor, Color.parseColor("#FFFFFF"));
        setLeftTxColor(colorLeftTx);

        //设置标题文字
        String titleTx = typedArray.getString(R.styleable.TopBar_txTitle);
        setTitleTx(titleTx);
        int colorTitleTx = typedArray.getColor(R.styleable.TopBar_txTitleColor, Color.parseColor("#FFFFFF"));
        setTitleColor(colorTitleTx);

        //设置右边文字
        String rightTx = typedArray.getString(R.styleable.TopBar_txRight);
        setRightTx(rightTx);
        int colorRightTx = typedArray.getColor(R.styleable.TopBar_txRightColor, Color.parseColor("#FFFFFF"));
        setRightTxColor(colorRightTx);

        Drawable drawableLeft = typedArray.getDrawable(R.styleable.TopBar_srcLeft);
        if (drawableLeft == null) {
            drawableLeft = getContext().getResources().getDrawable(R.mipmap.ic_back_normal_gray);
        }
        setLeftSrc(drawableLeft);

        Drawable drawableRight = typedArray.getDrawable(R.styleable.TopBar_srcRight);
        setRightSrc(drawableRight);

        typedArray.recycle();
    }

    public void setRightTxColor(int colorRightTx) {
        this.rightTx.setTextColor(colorRightTx);
    }

    public void setLeftTxColor(int colorLeftTx) {
        this.leftTx.setTextColor(colorLeftTx);
    }

    public void setTitleColor(int colorTitleTx) {
        this.titleTx.setTextColor(colorTitleTx);
    }

    public void setLeftTx(String leftTx) {
        this.leftTx.setText(leftTx);
    }

    public void setTitleTx(String leftTx) {
        this.titleTx.setText(leftTx);
    }

    public void setRightTx(String leftTx) {
        this.rightTx.setText(leftTx);
    }

    public void setLeftSrc(Drawable drawableLeft) {
        leftBtn.setImageDrawable(drawableLeft);
    }

    public void setRightSrc(Drawable drawableRight) {
        rightBtn.setImageDrawable(drawableRight);
    }

    public void removeFunction(int function) {
        setFunction(this.currentFunction & (~function));
    }

    public void addFunction(int function) {
        setFunction(this.currentFunction | function);
    }

    public boolean isAddFunction(int function) {
        return (this.currentFunction & function) == function;
    }

    public void setFunction(int function) {
        if (this.currentFunction == function) {
            return;
        }
        this.currentFunction = function;

        // 添加title
        titleTx.setVisibility(isAddFunction(FUNCTION_TEXT_TITLE) ? VISIBLE : GONE);

        // 添加leftTextView
        leftTx.setVisibility(isAddFunction(FUNCTION_TEXT_LEFT) ? VISIBLE : GONE);

        // 添加rightTextView1
        rightTx.setVisibility(isAddFunction(FUNCTION_TEXT_RIGHT) ? VISIBLE : GONE);

        // 添加leftButton
        leftBtn.setVisibility(isAddFunction(FUNCTION_BUTTON_LEFT) ? VISIBLE : GONE);

        // 添加RightButton
        rightBtn.setVisibility(isAddFunction(FUNCTION_BUTTON_RIGHT) ? VISIBLE : GONE);
    }

    public void setMyStatusBarBgColor(int colorRes) {
        myStatusBar.setBackgroundColor(colorRes);
    }


    @OnClick({R.id.left_tx, R.id.left_btn, R.id.title_tx, R.id.right_tx, R.id.right_btn})
    public void onViewClicked(View view) {
        int function = 0;
        switch (view.getId()) {
            case R.id.left_tx:
                function = FUNCTION_TEXT_LEFT;
                break;
            case R.id.left_btn:
                function = FUNCTION_BUTTON_LEFT;
                break;
            case R.id.title_tx:
                function = FUNCTION_TEXT_TITLE;
                break;
            case R.id.right_tx:
                function = FUNCTION_TEXT_RIGHT;
                break;
            case R.id.right_btn:
                function = FUNCTION_BUTTON_RIGHT;
                break;
        }
        // 根据控件ID处理点击事件
        if (isAddFunction(function) && function != 0) {
            boolean result = true;
            if (onTopBarClickListener != null) {
                result = onTopBarClickListener.onTopBarClickListener(function);
            }
            if (result) {
                // 处理返回事件
                Context context = getContext();
                if (context instanceof Activity) {
                    switch (function) {
                        case FUNCTION_BUTTON_LEFT:
                            CommonUtils.hideSoftPad((Activity) context);
                            ((Activity) context).finish();
                            break;
                        case FUNCTION_TEXT_TITLE:
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public interface OnTopBarClickListener {
        boolean onTopBarClickListener(int function);
    }
}
