package com.wy521angel.materialedittextproject;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class MaterialEditText extends AppCompatEditText {
    private static final float TEXT_SIZE = Utils.dp2px(12);//浮动标签字体的大小
    private static final float TEXT_MARGIN = Utils.dp2px(8);//浮动标签字体与EditText的间距
    private static final int TEXT_VERTICAL_OFFSET = (int) Utils.dp2px(38);//浮动标签字体与屏幕上边的间距
    private static final int TEXT_HORIZONTAL_OFFSET = (int) Utils.dp2px(5);//浮动标签字体与屏幕左边的间距
    private static final int VERTICAL_OFFSET_EXTRA = (int) Utils.dp2px(16);//浮动标签字体的偏移距离

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean floatingLabelShown;//之前浮动标签是否已经显示
    private float floatingLabelFraction;//浮动标签改变的数值
    private boolean useFloatingLabel;//是否使用浮动标签
    ObjectAnimator objectAnimator;//提示动画
    Rect backgroundPadding = new Rect();

    public MaterialEditText(Context context) {
        super(context);
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialEditText);
        useFloatingLabel = typedArray.getBoolean(R.styleable.MaterialEditText_useFloatingLabel,
                true);
        typedArray.recycle();

        paint.setTextSize(TEXT_SIZE);
        onUseFloatingLabelChange();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (useFloatingLabel) {
                    if (!floatingLabelShown && !TextUtils.isEmpty(s)) {
                        //显示浮动标签动画
                        floatingLabelShown = true;
                        getAnimator().start();
                    } else if (floatingLabelShown && TextUtils.isEmpty(s)) {
                        //隐藏浮动标签动画
                        floatingLabelShown = false;
                        getAnimator().reverse();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private ObjectAnimator getAnimator() {
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(MaterialEditText.this,
                    "floatingLabelFraction", 0, 1);
        }
        return objectAnimator;
    }

    public float getFloatingLabelFraction() {
        return floatingLabelFraction;
    }

    public void setFloatingLabelFraction(float floatingLabelFraction) {
        this.floatingLabelFraction = floatingLabelFraction;
        invalidate();//重绘
    }

    public void setUseFloatingLabel(boolean useFloatingLabel) {
        if (this.useFloatingLabel != useFloatingLabel) {//如果传入的值和当前值不相等，则说明需要修改布局
            this.useFloatingLabel = useFloatingLabel;
            onUseFloatingLabelChange();
        }
    }

    private void onUseFloatingLabelChange() {
        getBackground().getPadding(backgroundPadding);
        if (useFloatingLabel) {
            setPadding(getPaddingLeft(),
                    (int) (backgroundPadding.top + TEXT_SIZE + TEXT_MARGIN),
                    getPaddingRight(), getPaddingBottom());
        } else {
            setPadding(getPaddingLeft(), backgroundPadding.top,
                    getPaddingRight(), getPaddingBottom());
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        paint.setAlpha((int) (0XFF * floatingLabelFraction));
        canvas.drawText(getHint().toString(), TEXT_HORIZONTAL_OFFSET,
                TEXT_VERTICAL_OFFSET - VERTICAL_OFFSET_EXTRA * floatingLabelFraction, paint);
    }
}
