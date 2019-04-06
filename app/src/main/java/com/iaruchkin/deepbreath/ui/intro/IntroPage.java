package com.iaruchkin.deepbreath.ui.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaruchkin.deepbreath.R;

import androidx.fragment.app.Fragment;

public class IntroPage extends Fragment {

    private int title, color, image;

    public static IntroPage newInstance(int image, int title, int color) {
        IntroPage fragmentFirst = new IntroPage();
        Bundle args = new Bundle();
        args.putInt("image", image);
        args.putInt("title", title);
        args.putInt("pageColor", color);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            image = getArguments().getInt("image", 0);
            title = getArguments().getInt("title",0);
            color = getArguments().getInt("pageColor", R.color.bg_screen1);

            View view = inflater.inflate(R.layout.intro_fragment, container, false);
            TextView tvLabel = view.findViewById(R.id.page_title);
            ImageView imageView = view.findViewById(R.id.screenshot_img);

            view.setBackgroundColor(getResources().getColor(color));
            imageView.setImageResource(image);
            tvLabel.setText(getResources().getText(title));

        return view;
    }
}