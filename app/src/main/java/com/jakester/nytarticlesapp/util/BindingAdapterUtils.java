package com.jakester.nytarticlesapp.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jakester.nytarticlesapp.models.Image;

import java.util.List;

/**
 * Created by Jake on 9/22/2017.
 */

public class BindingAdapterUtils {
    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }
}
