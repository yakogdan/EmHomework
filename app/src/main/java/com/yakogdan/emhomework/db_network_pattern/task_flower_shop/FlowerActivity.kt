package com.yakogdan.emhomework.db_network_pattern.task_flower_shop

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yakogdan.emhomework.databinding.ActivityFlowerBinding
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.AppDatabase
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.BouquetsDAO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dao.FlowersDAO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.BouquetDBO
import com.yakogdan.emhomework.db_network_pattern.task_flower_shop.db.dbo.FlowersDBO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlowerActivity : AppCompatActivity() {

    val popularFlowers: List<FlowersDBO> = listOf(
        FlowersDBO(id = 1, name = "Роза", remainingQuantity = 120),
        FlowersDBO(id = 2, name = "Тюльпан", remainingQuantity = 80),
        FlowersDBO(id = 3, name = "Лилия", remainingQuantity = 36),
        FlowersDBO(id = 4, name = "Хризантема", remainingQuantity = 64),
        FlowersDBO(id = 5, name = "Гвоздика", remainingQuantity = 48),
        FlowersDBO(id = 6, name = "Гербера", remainingQuantity = 42),
        FlowersDBO(id = 7, name = "Альстромерия", remainingQuantity = 55),
        FlowersDBO(id = 8, name = "Эустома", remainingQuantity = 30),
        FlowersDBO(id = 9, name = "Пион", remainingQuantity = 24),
        FlowersDBO(id = 10, name = "Гортензия", remainingQuantity = 18)
    )

    val bouquets: List<BouquetDBO> = listOf(
        BouquetDBO(
            id = 1,
            bouquetName = "Весенний микс",
            bouquetComponents = listOf(
                FlowersDBO(id = 1, name = "Роза") to 7,
                FlowersDBO(id = 2, name = "Тюльпан") to 5,
                FlowersDBO(id = 3, name = "Лилия") to 3
            )
        ),
        BouquetDBO(
            id = 2,
            bouquetName = "Яркий праздник",
            bouquetComponents = listOf(
                FlowersDBO(id = 4, name = "Хризантема") to 7,
                FlowersDBO(id = 5, name = "Гвоздика") to 5,
                FlowersDBO(id = 6, name = "Гербера") to 5
            )
        ),
        BouquetDBO(
            id = 3,
            bouquetName = "Нежная облачность",
            bouquetComponents = listOf(
                FlowersDBO(id = 7, name = "Альстромерия") to 7,
                FlowersDBO(id = 8, name = "Эустома") to 5,
                FlowersDBO(id = 9, name = "Пион") to 3,
                FlowersDBO(id = 10, name = "Гортензия") to 2
            )
        )
    )

    val appDatabase: AppDatabase by lazy {
        AppDatabase.getInstance(context = applicationContext)
    }

    val flowersDao: FlowersDAO by lazy {
        appDatabase.flowersDao()
    }

    val bouquetsDao: BouquetsDAO by lazy {
        appDatabase.bouquetDao()
    }

    val getBouquetQuantityUseCase by lazy {
        GetBouquetQuantityUseCase(
            bouquetsDAO = bouquetsDao,
            flowersDAO = flowersDao,
        )
    }

    private var _binding: ActivityFlowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityFlowerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom,
            )
            insets
        }

        binding.btnAddFlowers.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                flowersDao.addFlowers(flowers = popularFlowers)
            }
        }

        binding.btnGetFlowers.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                val data = flowersDao
                    .getFlowers()
                    .map { "${it.name} ${it.remainingQuantity}" }
                    .toString()

                withContext(Dispatchers.Main) {
                    binding.tvFlower.text = data
                }
            }
        }

        binding.btnRemoveFlowers.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                flowersDao.removeFlowers(flowerId = 1, flowersQuantity = 5)
            }
        }

        binding.btnAddBouquets.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                bouquetsDao.addBouquets(bouquets = bouquets)
            }
        }

        binding.btnGetBouquets.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                val data = bouquetsDao
                    .getBouquets()
                    .map { bouquet ->
                        val remainingQuantity =
                            getBouquetQuantityUseCase.invoke(bouquetId = bouquet.id)

                        "${bouquet.bouquetName} - осталось: $remainingQuantity"
                    }

                withContext(Dispatchers.Main) {
                    binding.tvBouquet.text = data.toString()
                }
            }
        }
    }
}