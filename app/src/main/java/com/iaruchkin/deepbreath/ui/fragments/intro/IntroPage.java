package com.iaruchkin.deepbreath.ui.fragments.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iaruchkin.deepbreath.R;

import androidx.fragment.app.Fragment;

public class IntroPage extends Fragment {

    private int title, description, color, image;

    public static IntroPage newInstance(int image, int title, int description, int color) {
        IntroPage fragmentFirst = new IntroPage();
        Bundle args = new Bundle();
        args.putInt("image", image);
        args.putInt("title", title);
        args.putInt("description", description);
        args.putInt("pageColor", color);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            image = getArguments().getInt("image");
            title = getArguments().getInt("title");
            description = getArguments().getInt("description");
            color = getArguments().getInt("pageColor");

            View view = inflater.inflate(R.layout.intro_fragment, container, false);
            TextView tvLabel = view.findViewById(R.id.page_title);
            TextView tvDesc = view.findViewById(R.id.page_desc);
            ImageView imageView = view.findViewById(R.id.screenshot_img);

            view.setBackgroundColor(getResources().getColor(color));
            imageView.setImageResource(image);
            tvLabel.setText(getResources().getText(title));
            tvDesc.setText(getResources().getText(description));

        return view;
    }
}