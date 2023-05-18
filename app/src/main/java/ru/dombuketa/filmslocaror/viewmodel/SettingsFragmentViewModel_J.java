package ru.dombuketa.filmslocaror.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class SettingsFragmentViewModel_J extends ViewModel {
    //Инжектим интерактор
    @Inject
    Interactor_J interactor_j;
    public MutableLiveData<String> categoryPropertyLiveData = new MutableLiveData<>();

    public SettingsFragmentViewModel_J() {
        App_J.getInstance().daggerj.injectj(this);
        //Получаем категорию при инициализации, чтобы у нас сразу подтягивалась категория
        getCategoryProperty();
    }

    public void putCategoryProperty(String category){
        //Сохраняем в настройки
        interactor_j.saveDefaultCategoryToPreferences(category);
        //И сразу забираем, чтобы сохранить состояние в модели
        getCategoryProperty();
    }
    private void getCategoryProperty() {
        //Кладем категорию в LiveData
        categoryPropertyLiveData.setValue(interactor_j.getDefaultCategoryFromPreferences());
    }
}
