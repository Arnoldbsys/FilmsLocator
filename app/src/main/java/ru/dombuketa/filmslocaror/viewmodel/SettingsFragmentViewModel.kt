package ru.dombuketa.filmslocaror.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.dombuketa.filmslocaror.App
import ru.dombuketa.filmslocaror.domain.Interactor
import javax.inject.Inject

class SettingsFragmentViewModel : ViewModel() {
    //Инжектим интерактор
    @Inject lateinit var interactor: Interactor
    val categoryPropertyLifeData: MutableLiveData<String> = MutableLiveData()
    init {
        App.instance.dagger.injectt(this)
        //Получаем категорию при инициализации, чтобы у нас сразу подтягивалась категория
        getCategoryProperty()
    }

    fun putCategoryProperty(category: String){
        //Сохраняем в настройки
        interactor.savaDefaultCategoryToPreferences(category)
        //И сразу забираем, чтобы сохранить состояние в модели
        getCategoryProperty()
    }

    private fun getCategoryProperty(){
        //Кладем категорию в LiveData
        categoryPropertyLifeData.value = interactor.getDefaultCategoryFromPreferences()
    }
}