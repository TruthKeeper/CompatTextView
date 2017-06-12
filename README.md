# CompatTextView

## 前言

在项目开发过程中在drawable目录下会逐渐被shape，selector等xml文件充斥，到项目后期会变得难以维护，还在一定程度上增加了安装包的大小，基于这个现象，CompatTextView借此而生

## 特点

CompatTextView继承自AppCompatTextView，目前支持了以下功能：
- 支持shape的solid、stroke、gradient、radius的配置
- 支持enabled、pressed、selected、unenabled共计4种状态的配置
- 不再需要写大量的shape、selector文件配置
- 支持上下左右的drawable大小配置，SVG支持、Tint着色支持
- 支持上下左右的drawable的对齐方式配置

上图：

![](https://github.com/TruthKeeper/CompatTextView/blob/master/GIF.gif)

自定义属性一览：

```
<declare-styleable name="CompatTextView">
        <!--四个位置的圆角，传入对应宽度或者高度的一半就是圆形-->
        <attr name="ctv_topLeftRadius" format="dimension" />
        <attr name="ctv_topRightRadius" format="dimension" />
        <attr name="ctv_bottomLeftRadius" format="dimension" />
        <attr name="ctv_bottomRightRadius" format="dimension" />
        <!--边框宽度-->
        <attr name="ctv_strokeWidth" format="dimension" />
        <!--边框线颜色-->
        <attr name="ctv_strokeColor" format="color" />
        <attr name="ctv_strokePressedColor" format="color" />
        <attr name="ctv_strokeSelectedColor" format="color" />
        <attr name="ctv_strokeUnenabledColor" format="color" />
        <!--内容颜色-->
        <attr name="ctv_solidColor" format="color" />
        <attr name="ctv_solidPressedColor" format="color" />
        <attr name="ctv_solidSelectedColor" format="color" />
        <attr name="ctv_solidUnenabledColor" format="color" />
        <!--文本颜色-->
        <attr name="ctv_textColor" format="color" />
        <attr name="ctv_textPressedColor" format="color" />
        <attr name="ctv_textSelectedColor" format="color" />
        <attr name="ctv_textUnenabledColor" format="color" />
        <!--渐变颜色-->
        <attr name="ctv_gradientStartColor" format="color" />
        <attr name="ctv_gradientStartPressedColor" format="color" />
        <attr name="ctv_gradientStartSelectedColor" format="color" />
        <attr name="ctv_gradientStartUnenabledColor" format="color" />

        <attr name="ctv_gradientEndColor" format="color" />
        <attr name="ctv_gradientEndPressedColor" format="color" />
        <attr name="ctv_gradientEndSelectedColor" format="color" />
        <attr name="ctv_gradientEndUnenabledColor" format="color" />
        <!--渐变方向-->
        <attr name="ctv_gradientDirection" format="enum">
            <enum name="topToBottom" value="0" />
            <enum name="leftToRight" value="1" />
            <enum name="topLeftToBottomRight" value="2" />
            <enum name="bottomLeftToTopRight" value="3" />
        </attr>
        <attr name="ctv_gradientDirectionUnenabled" format="enum">
            <enum name="topToBottom" value="0" />
            <enum name="leftToRight" value="1" />
            <enum name="topLeftToBottomRight" value="2" />
            <enum name="bottomLeftToTopRight" value="3" />
        </attr>
        <attr name="ctv_gradientDirectionPressed" format="enum">
            <enum name="topToBottom" value="0" />
            <enum name="leftToRight" value="1" />
            <enum name="topLeftToBottomRight" value="2" />
            <enum name="bottomLeftToTopRight" value="3" />
        </attr>
        <attr name="ctv_gradientDirectionSelected" format="enum">
            <enum name="topToBottom" value="0" />
            <enum name="leftToRight" value="1" />
            <enum name="topLeftToBottomRight" value="2" />
            <enum name="bottomLeftToTopRight" value="3" />
        </attr>
        <!--Tint着色支持与drawable细腻配置-->
        <attr name="ctv_tintLeft" format="color" />
        <attr name="ctv_tintDrawableLeft" format="reference" />
        <attr name="ctv_tintDrawableLeftWidth" format="dimension" />
        <attr name="ctv_tintDrawableLeftHeight" format="dimension" />

        <attr name="ctv_tintRight" format="color" />
        <attr name="ctv_tintDrawableRight" format="reference" />
        <attr name="ctv_tintDrawableRightWidth" format="dimension" />
        <attr name="ctv_tintDrawableRightHeight" format="dimension" />

        <attr name="ctv_tintTop" format="color" />
        <attr name="ctv_tintDrawableTop" format="reference" />
        <attr name="ctv_tintDrawableTopWidth" format="dimension" />
        <attr name="ctv_tintDrawableTopHeight" format="dimension" />

        <attr name="ctv_tintBottom" format="color" />
        <attr name="ctv_tintDrawableBottom" format="reference" />
        <attr name="ctv_tintDrawableBottomWidth" format="dimension" />
        <attr name="ctv_tintDrawableBottomHeight" format="dimension" />
        <!--drawable对齐方式-->
        <attr name="ctv_drawableLeftAlign" format="enum">
            <enum name="top" value="0" />
            <enum name="center" value="1" />
            <enum name="bottom" value="2" />
        </attr>
        <attr name="ctv_drawableRightAlign" format="enum">
            <enum name="top" value="0" />
            <enum name="center" value="1" />
            <enum name="bottom" value="2" />
        </attr>
        <attr name="ctv_drawableTopAlign" format="enum">
            <enum name="left" value="0" />
            <enum name="center" value="1" />
            <enum name="right" value="2" />
        </attr>
        <attr name="ctv_drawableBottomAlign" format="enum">
            <enum name="left" value="0" />
            <enum name="center" value="1" />
            <enum name="right" value="2" />
        </attr>
        <!--切换的渐变，毫秒，默认0，无效果-->
        <attr name="ctv_fadeDuring" format="integer" />
    </declare-styleable>
```

## 使用

```
dependencies {
    compile 'com.tk.compattextview:library:1.0'
}
```

顾名思义，[CompatTextView](https://github.com/TruthKeeper/CompatTextView)被用来扩展、兼容、简化一些开发作业，如果您需要一些炫酷的功能，可以看看以下基于TextView扩展的开源项目：

#### [SuperTextView](https://github.com/chenBingX/SuperTextView)

#### [Fancybuttons](https://github.com/medyo/Fancybuttons)
