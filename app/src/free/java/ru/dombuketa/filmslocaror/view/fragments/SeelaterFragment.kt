package ru.dombuketa.filmslocaror.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.dombuketa.db_module.dto.Notification
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.databinding.FragmentSeelaterBinding
import ru.dombuketa.filmslocaror.utils.AnimationHelper
import ru.dombuketa.filmslocaror.utils.AutoDisposable
import ru.dombuketa.filmslocaror.utils.TopSpacingItemDecoration
import ru.dombuketa.filmslocaror.utils.addTo
import ru.dombuketa.filmslocaror.view.rv_adapters.NotificationListRecyclerAdapter
import ru.dombuketa.filmslocaror.view.rv_helpers.SeelaterTouchHelper
import ru.dombuketa.filmslocaror.viewmodel.SeelaterFragmentViewModel
import java.util.Date

class SeelaterFragment : Fragment() {
    private lateinit var binding: FragmentSeelaterBinding
    private lateinit var notificationsAdapter: NotificationListRecyclerAdapter
    private lateinit var layoutManagerRV: LinearLayoutManager
    private val autoDisposable = AutoDisposable()
    private val viewModel by lazy {
        ViewModelProvider.NewInstanceFactory().create(SeelaterFragmentViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSeelaterBinding.inflate(inflater, container, false)
        autoDisposable.bindTo(lifecycle)
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(binding.seelaterFragmentRoot, requireActivity(),3)

        viewModel.notificationsListRxJavaData.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { list ->
                notificationsAdapter.addItems(list)
            }
            .addTo(autoDisposable)
        //Log.i("SeeLater", Date().time.toString())
        initSeelaterRV()
        if (viewModel.getEvaluatePeriodState()){
            //binding.evalLabel.visibility = View.GONE
            binding.btnReminders.visibility = View.VISIBLE
            binding.seelaterRecycler.visibility = View.VISIBLE
        }
        else{
            //binding.evalLabel.visibility = View.VISIBLE
            Toast.makeText(requireContext(),  EVAL_MESSAGE, Toast.LENGTH_SHORT).show()
            binding.btnReminders.visibility = View.GONE
            binding.seelaterRecycler.visibility = View.GONE
        }


        binding.btnReminders.setOnClickListener {
            viewModel.clearAllNotifications()
            Log.i("fragSeeLater", "click clear all")
        }
    }

    private fun initSeelaterRV() {
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        binding.seelaterRecycler.apply {
            notificationsAdapter =
                NotificationListRecyclerAdapter(object :
                    NotificationListRecyclerAdapter.OnItemClickListener {
                    override fun click(notif: Notification) {
                        Log.i("seeLaterFragment", "!!! Click")
                        val n = viewModel.getFilm(notif.filmId)
                        n.observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                            App.instance.dagger.getNotifyHelper().notificatioSet(requireContext(), it)
                        },{
                            println("!!! Seelater - initRV/ Click" + it.message)
                        })

                    }
                })

            //Присваиваем адаптер
            adapter = notificationsAdapter
            //Удаление через смахивание
            val callback = SeelaterTouchHelper(notificationsAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)
            //Присвои layoutmanager
            layoutManager = LinearLayoutManager(requireContext())
            layoutManagerRV = layoutManager as LinearLayoutManager
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }
    }

    companion object{
        const val EVAL_MESSAGE = "Пробный период окончился, Напоминания доступны в полнофункциональной версии программы."
    }

}