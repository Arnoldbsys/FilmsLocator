package ru.dombuketa.filmslocaror.utils;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//Нам нужны будут отступы между элементами, поэтому создадим небольшой класс для этого:
public class TopSpacingItemDecoration_J extends RecyclerView.ItemDecoration {
    private int paddingInDp;
    public TopSpacingItemDecoration_J(int paddingInDp) {
        this.paddingInDp = (int)(paddingInDp * Resources.getSystem().getDisplayMetrics().density);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = paddingInDp;
        outRect.right = paddingInDp;
        outRect.left = paddingInDp;
    }


}
