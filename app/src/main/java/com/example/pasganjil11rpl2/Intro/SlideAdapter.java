package com.example.pasganjil11rpl2.Intro;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.pasganjil11rpl2.R;

public class SlideAdapter extends PagerAdapter {

    Context CN;
    LayoutInflater layoutInflater;

    public SlideAdapter(Context CN) {
        this.CN = CN;
    }

    public int[] slide_images = {
            R.drawable.securitylogo,
            R.drawable.phonelogo,
            R.drawable.newspaperlogo
    };

    public String[] slide_headings = {
            "SECURE",
            "CROSS-PLATFORM",
            "DIGITAL-AGE"
    };

    public String[] slide_desc = {
            "Kami mengutamakan keselamatan kami pengguna aplikasi dengan penuh perhatian dan kesungguhan",
            "Kami menjamin fleksibilitas pengguna kami dengan menyediakan aplikasi kami di berbagai perangkat",
            "Dengan kenyamanan yang diberikan aplikasi kami, tiada lagi keperluan anda akan surat kabar"

};

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) CN.getSystemService(CN.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slidelayout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slide_image);
        TextView slideHeader = (TextView) view.findViewById(R.id.slide_header);
        TextView slideDesc = (TextView) view.findViewById(R.id.slide_desc);

        slideImageView.setImageResource(slide_images[position]);
        slideHeader.setText(slide_headings[position]);
        slideDesc.setText(slide_desc[position]);

        container.addView(view);

        return view;
     }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
