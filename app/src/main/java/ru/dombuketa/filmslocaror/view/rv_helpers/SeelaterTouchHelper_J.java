package ru.dombuketa.filmslocaror.view.rv_helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import ru.dombuketa.filmslocaror.view.rv_adapters.NotificationListRecyclerAdapter_J;

public class SeelaterTouchHelper_J extends ItemTouchHelper.Callback {

    private NotificationListRecyclerAdapter_J adapter;
    public SeelaterTouchHelper_J(NotificationListRecyclerAdapter_J adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false; //Drag & drop не поддерживается
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;  //Swipe поддерживается
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //Настраиваем флаги для drag & drop и swipe жестов
        return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        adapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
