package com.lwkandroid.hxlibs.utils.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by LWK
 * TODO Glide实现的加载图片
 * 2016/9/23
 */
public class GlideImageDisplayer implements ImageDisplayer
{
    @Override
    public void display(Context context, ImageView imageView, String url, int maxWidth, int maxHeight)
    {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .override(maxWidth, maxHeight)
                .into(imageView);
    }

    @Override
    public void display(Context context, ImageView imageView, String url, int maxWidth, int maxHeight, int holderImgResId, int errorImgResId)
    {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(holderImgResId)
                .error(errorImgResId)
                .override(maxWidth, maxHeight)
                .into(imageView);
    }

    @Override
    public void displayBlurImage(Context context, ImageView imageView, String url, int radius, int sampling)
    {
        Glide.with(context)
                .load(url)
                .bitmapTransform(new BlurTransformation(context, radius, sampling))
                .into(imageView);
    }
}
