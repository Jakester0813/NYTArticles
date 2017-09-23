package com.jakester.nytarticlesapp.util;

/**
 * Created by Jake on 9/22/2017.
 */

public class BindingAdapterUtils {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }
}
