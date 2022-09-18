package com.example.dpit2020navem.Intro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.dpit2020navem.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {

    Context context;
    List<IntroScreenItem> introScreenItems;

    public IntroViewPagerAdapter(Context context, List<IntroScreenItem> introScreenItems) {
        this.context = context;
        this.introScreenItems = introScreenItems;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutIntroScreen = inflater.inflate(R.layout.layout_intro_screen, null);

        TextView introText = layoutIntroScreen.findViewById(R.id.introText);
        ImageView introPicture = layoutIntroScreen.findViewById(R.id.introBackgroud);

        introText.setText(introScreenItems.get(position).getIntroText());
        introPicture.setImageResource(introScreenItems.get(position).getIntroPicture());

        container.addView(layoutIntroScreen);

        return  layoutIntroScreen;
    }

    @Override
    public int getCount() {
        return introScreenItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);
    }
}
