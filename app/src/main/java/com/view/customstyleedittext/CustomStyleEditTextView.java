package com.view.customstyleedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lloyd on 2017/9/23.
 * 模仿摩拜单车手动输入编号
 */

public class CustomStyleEditTextView extends EditText {

    private static final int defaultContMargin = 5;
    private static final int defaultSplitLineWidth = 10;
    private final float textSize;

    private int borderColor = 0xFFCCCCCC;

    private int totalTextLength = 6;
    private int textColor = 0xFFCCCCCC;
    private int focusBoderColor = 0xCCCCCCCC;
    private float textWidth = 8;

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int currentTextLength;

    private float boxMarge; //字符方块的marge
    private float boxRadius; //字符方块的边角圆弧
    private String text;
    List<RectF> rectFList = new ArrayList<>();

    public CustomStyleEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomStyleEditTextView, 0, 0);
        borderColor = a.getColor(R.styleable.CustomStyleEditTextView_pivBorderColor, borderColor);
        totalTextLength = a.getInt(R.styleable.CustomStyleEditTextView_pivTextLength, totalTextLength);
        textColor = a.getColor(R.styleable.CustomStyleEditTextView_pivTextColor, textColor);
        focusBoderColor = a.getColor(R.styleable.CustomStyleEditTextView_pivFocusBoderColor, focusBoderColor);
        textWidth = a.getDimension(R.styleable.CustomStyleEditTextView_pivTextWidth, textWidth);
        textSize = a.getDimension(R.styleable.CustomStyleEditTextView_pivTextSize, 25);
        a.recycle();

        borderPaint.setColor(borderColor);
        textPaint.setStrokeWidth(textWidth);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        boxRadius = SizeUtils.dp2px(context, 3);
        boxMarge = SizeUtils.dp2px(context, 3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

//        边框
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(defaultSplitLineWidth);
        for (int i = 0; i < totalTextLength; i++) {
            RectF rect2 = generationSquareBoxRectF(height, width, i);
            rectFList.add(rect2);
            if (i == currentTextLength){
                borderPaint.setColor(focusBoderColor);
                canvas.drawRoundRect(rect2, boxRadius, boxRadius, borderPaint);
            }else{
                borderPaint.setColor(borderColor);
                canvas.drawRoundRect(rect2, boxRadius, boxRadius, borderPaint);
            }
        }
//        文字居中
        for (int i = 0; i < currentTextLength; i++) {
            Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
            float baseline = (rectFList.get(i).bottom + rectFList.get(i).top - fontMetrics.bottom - fontMetrics.top) / 2;
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text.substring(i,i+1), rectFList.get(i).centerX(), baseline, textPaint);
        }
    }

    @NonNull
    private RectF generationSquareBoxRectF(int height, int width, int i) {
        float boxWidth = (width / totalTextLength);
        float boxHeight = height;
        float left = boxMarge + boxWidth * i;
        float right = boxWidth * (i + 1) - boxMarge;
        float top = boxMarge;
        float bottom = boxHeight - boxMarge;
        float min = Math.min(boxWidth, boxHeight);
        float dw = (boxWidth - min) / 2F;
        left += dw;
        right -= dw;
        return new RectF(left, top, right, bottom);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.currentTextLength = text.toString().length();
        this.text = text.toString();
        invalidate();
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    public int getPasswordLength() {
        return totalTextLength;
    }

    public void setPasswordLength(int length) {
        this.totalTextLength = length;
        invalidate();
    }

    public int getTextColorColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        textPaint.setColor(textColor);
        invalidate();
    }

    public float getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(float width) {
        this.textWidth = width;
        textPaint.setStrokeWidth(width);
        invalidate();
    }


    private static class SizeUtils {
        public static int dp2px(Context context, float dp) {
            final float density = context.getResources().getDisplayMetrics().density;
            return (int) (dp * density + 0.5);
        }

        public static int px2dp(Context context, float px) {
            final float density = context.getResources().getDisplayMetrics().density;
            return (int) (px / density + 0.5);
        }
    }

}
