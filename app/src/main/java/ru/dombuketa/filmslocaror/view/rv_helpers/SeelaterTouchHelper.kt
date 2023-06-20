package ru.dombuketa.filmslocaror.view.rv_helpers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.dombuketa.filmslocaror.view.rv_adapters.NotificationListRecyclerAdapter

class SeelaterTouchHelper(val adapter: NotificationListRecyclerAdapter) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        //Drag & drop не поддерживается
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        //Swipe поддерживается
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        //Настраиваем флаги для drag & drop и swipe жестов
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //Удаляем элемент из списка после жеста swipe
        //adapter.items.removeAt(viewHolder.adapterPosition)
        //Or DiffUtil
        //adapter.notifyItemRemoved(viewHolder.adapterPosition);

        adapter.onItemDismiss(viewHolder.adapterPosition)
    }
}