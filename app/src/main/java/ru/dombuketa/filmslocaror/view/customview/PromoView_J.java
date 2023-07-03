package ru.dombuketa.filmslocaror.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import ru.dombuketa.filmslocaror.databinding.MergePromoBinding;
import ru.dombuketa.net_module.entity.ApiConstants;

public class PromoView_J extends FrameLayout {
    public PromoView_J(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    MergePromoBinding binding = MergePromoBinding.inflate(LayoutInflater.from(getContext()), this);
    Button watchButton = binding.watchButton;

    void setLinkForPoster(String link){
        Glide.with(binding.getRoot())
            .load(ApiConstants.IMAGES_URL + "w500" + link)
            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(35)))
            .into(binding.poster);
    }
}
