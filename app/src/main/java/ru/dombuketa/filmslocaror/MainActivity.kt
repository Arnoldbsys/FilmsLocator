package ru.dombuketa.filmslocaror

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val filmsDataBase = mutableListOf<Film>()
    private lateinit var filmsAdapter: FilmsListRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initTopBar()
        initNavigationMenu()
        initData()
        initRV()

    }

    fun initTopBar() {
        val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)
        topAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        Toast.makeText(this, R.string.mode_night, Toast.LENGTH_SHORT).show()
                        it.setIcon(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.ic_mode_night
                            )
                        )
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        Toast.makeText(this, R.string.mode_day, Toast.LENGTH_SHORT).show()
                        it.setIcon(
                            ContextCompat.getDrawable(
                                applicationContext,
                                R.drawable.ic_mode_light
                            )
                        )
                    }
//
                    true
                }
                else -> false
            }
        }
    }

    fun initNavigationMenu() {
        val bottomNavMenu = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavMenu.setOnNavigationItemSelectedListener() {
            when (it.itemId) {
                R.id.favorites -> {
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.watch_later -> {
                    Toast.makeText(this, "Посмотреть позже", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.casts -> {
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    fun initData(){
        filmsDataBase.add(Film("Титаник",R.drawable.poster_1,"Очень хороший фильм про любовь, но я его не смотрел."))
        filmsDataBase.add(Film("Тар",R.drawable.poster_2,"Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел."))
        filmsDataBase.add(Film("ГудВилХантер",R.drawable.poster_3,"Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего."))
        filmsDataBase.add(Film("Шининг",R.drawable.poster_4,"Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора."))
        filmsDataBase.add(Film("Завтрак клуб",R.drawable.poster_5,"Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность."))
        filmsDataBase.add(Film("Криминальное чтиво",R.drawable.poster_01,"Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей."))
        filmsDataBase.add(Film("Звездные войны",R.drawable.poster_02,"2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее."))
        filmsDataBase.add(Film("Е.Т.",R.drawable.poster_03,"Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел."))
        filmsDataBase.add(Film("Назад в будующее",R.drawable.poster_04,"Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза."))
    }

    private fun initRV() {
        val main_recycler = findViewById<RecyclerView>(R.id.main_recycler)
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        main_recycler.apply {
            filmsAdapter = FilmsListRecyclerAdapter(object : FilmsListRecyclerAdapter.OnItemClickListener{
                override fun click(film: Film) {
                    //Создаем бандл и кладем туда объект с данными фильма
                    val bundle = Bundle()
                    //Первым параметром указывается ключ, по которому потом будем искать, вторым сам
                    //передаваемый объект
                    bundle.putParcelable("film", film)
                    //Запускаем наше активити
                    val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                    //Прикрепляем бандл к интенту
                    intent.putExtras(bundle)
                    //Запускаем активити через интент
                    startActivity(intent)
                }
            })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager=LinearLayoutManager(this@MainActivity)
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)

        }
        filmsAdapter.addItems(filmsDataBase)
    }

}