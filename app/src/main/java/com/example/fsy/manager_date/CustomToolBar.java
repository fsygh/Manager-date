package com.example.fsy.manager_date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by zhoufazhan on 2017/7/25.
 * 自定义控件-组合控件
 */

public class CustomToolBar extends RelativeLayout implements View.OnClickListener {
    private float leftTextSize;
    private float middleTextSize;
    private float rightTextSize;
    private int leftTextColor;
    private int middleTextColor;
    private int rightTextColor;
    private String leftText;
    private String middleText;
    private String rightText;
    private int leftImage;
    private int rightImage;
    private TextView leftView;
    private TextView middleView;
    private TextView rightView;

    public CustomToolBar(Context context) {
        this(context, null);
    }

    public CustomToolBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar, defStyleAttr, 0);

        initView(typedArray);
        typedArray.recycle();

    }

    public void initView(TypedArray typedArray) {
        LayoutInflater.from(getContext()).inflate(R.layout.toolbar, this);
        leftView = (TextView) findViewById(R.id.left);
        leftView.setOnClickListener(this);
        middleView = (TextView) findViewById(R.id.middle);
        rightView = (TextView) findViewById(R.id.right);
        rightView.setOnClickListener(this);
        leftText = typedArray.getString(R.styleable.CustomToolBar_leftText);
        rightText = typedArray.getString(R.styleable.CustomToolBar_rightText);
        middleText = typedArray.getString(R.styleable.CustomToolBar_middleText);
        leftTextSize = typedArray.getDimension(R.styleable.CustomToolBar_leftTextSize, 20);

        middleTextSize = typedArray.getDimension(R.styleable.CustomToolBar_middleTextSize, 20);
        rightTextSize = typedArray.getDimension(R.styleable.CustomToolBar_rightTextSize, 20);

        leftTextColor = typedArray.getColor(R.styleable.CustomToolBar_leftTextColor, getResources().getColor(R.color.colorPrimary));
        middleTextColor = typedArray.getColor(R.styleable.CustomToolBar_middleTextColor, getResources().getColor(R.color.colorPrimary));
        rightTextColor = typedArray.getColor(R.styleable.CustomToolBar_rightTextColor, getResources().getColor(R.color.colorPrimary));

        leftImage = typedArray.getResourceId(R.styleable.CustomToolBar_leftImage, 0);
        rightImage = typedArray.getResourceId(R.styleable.CustomToolBar_rightImage, 0);

        if (leftImage > 0) {
            setLeftImage(leftImage);
        } else {
            setLeftText(leftText);
        }
        if (rightImage > 0) {
            setRightImage(rightImage);
        } else {
            setRightText(rightText);
        }
        setLeftTextSize(leftTextSize);
        setLeftTextColor(leftTextColor);
        setMiddleTextColor(middleTextColor);
        setMiddleTextSize(middleTextSize);
        setMiddleText(middleText);
        setRightTextColor(rightTextColor);
        setRightTextSize(rightTextSize);
    }


    public CustomToolBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                if (toolBarClick != null) {
                    toolBarClick.leftClick();
                }
                break;
            case R.id.right:
                if (toolBarClick != null) {
                    toolBarClick.rightClick();
                }
                break;
        }
    }

    public void setLeftTextSize(float leftTextSize) {
        leftView.setTextSize(leftTextSize);
    }

    public void setMiddleTextSize(float middleTextSize) {
        middleView.setTextSize(middleTextSize);
    }

    public void setRightTextSize(float rightTextSize) {
        rightView.setTextSize(rightTextSize);
    }

    public void setLeftTextColor(int leftTextColor) {
        leftView.setTextColor(leftTextColor);
    }

    public void setMiddleTextColor(int middleTextColor) {
        middleView.setTextColor(middleTextColor);
    }

    public void setRightTextColor(int rightTextColor) {
        rightView.setTextColor(rightTextColor);
    }

    public void setLeftText(String leftText) {
        leftView.setText(leftText);
    }

    public void setMiddleText(String middleText) {
        middleView.setText(middleText);
    }

    public void setRightText(String rightText) {
        rightView.setText(rightText);
    }

    public void setLeftImage(int leftImage) {
        setLeftText(leftText);
        Drawable drawable = getResources().getDrawable(leftImage, null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
        leftView.setCompoundDrawables(drawable, null, null, null);
    }

    public void setRightImage(int rightImage) {
        setRightText(rightText);
        Drawable drawable = getResources().getDrawable(rightImage, null);
        drawable.setBounds(0, 0, drawable.getMinimumWidth() / 2, drawable.getMinimumHeight() / 2);
        rightView.setCompoundDrawables(null, null, drawable, null);
    }

    interface ToolBarClick {
        void leftClick();

        void rightClick();
    }

    public ToolBarClick toolBarClick;

    public void setToolBarClick(ToolBarClick toolBarClick) {
        this.toolBarClick = toolBarClick;
    }
}
