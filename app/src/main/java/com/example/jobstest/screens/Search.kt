package com.example.jobstest.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobstest.R
import com.example.jobstest.adapters.OfferAdapter
import com.example.jobstest.adapters.VacancyAdapter
import com.example.jobstest.utils.SpacesItemDecoration
import com.example.jobstest.utils.WordDeclension
import com.example.jobstest.viewmodel.JobsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class Search : Fragment() {


    private val jobsViewModel: JobsViewModel by viewModel()
    private val wordDeclension = WordDeclension()
    private lateinit var offerAdapter: OfferAdapter
    private lateinit var vacancyAdapter: VacancyAdapter
    private lateinit var quantityVacancyTextView: TextView
    private var isFullListDisplayed = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quantityVacancyTextView = view.findViewById(R.id.quantity_vacancy)

        // Инициализация RecyclerView
        val offerRecyclerView = view.findViewById<RecyclerView>(R.id.search_recycler_offer)
        offerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val vacancyRecyclerView = view.findViewById<RecyclerView>(R.id.search_recycler_vacancy)
        vacancyRecyclerView.layoutManager = LinearLayoutManager(context)

        // расстояние между карточками через ItemDecoration
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp8)
        vacancyRecyclerView.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        // Инициализация адаптера с текущими данными, если они уже загружены
        offerAdapter = OfferAdapter(emptyList()) { link ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
        offerRecyclerView.adapter = offerAdapter

        // Создам адаптер для вакансий с логикой обработки кнопки "Еще вакансий"
        vacancyAdapter = VacancyAdapter(
            emptyList(),
            onVacancyClick = { vacancyId ->
                // Реализуем логику перехода на страницу вакансии
            },
            // клик по кнопке избранное
            onFavoriteClick = { vacancy ->
                jobsViewModel.toggleFavorite(vacancy)
                // Перезагружаем список вакансий, сохраняя текущее состояние (полный список или сокращенный)
                vacancyAdapter.updateVacancies(jobsViewModel.vacancies.value, isFullListDisplayed, jobsViewModel.vacancies.value.size)
            },
            onShowMoreClick = {
                // Логика загрузки всех вакансий при нажатии на кнопку "Еще вакансий"
                isFullListDisplayed = true
                vacancyAdapter.updateVacancies(jobsViewModel.vacancies.value, true, jobsViewModel.vacancies.value.size)

                // Скрываем верхний список предложений
                offerRecyclerView.visibility = View.GONE


                // Обновляем текст с количеством вакансий
//                val totalVacancyCount = jobsViewModel.vacancies.value.size
//                val vacancy = wordDeclension.getVacancyCountString(totalVacancyCount.toInt())
//                quantityVacancyTextView.text = "$vacancy"
            }
        )
        vacancyRecyclerView.adapter = vacancyAdapter

        // Наблюдаем за списком offers
        lifecycleScope.launch {
            jobsViewModel.offers.collect { offers ->
                offerAdapter.updateOffers(offers)  // Обновление данных в адаптере
            }
        }

        // Наблюдаем за списком vacancies
        lifecycleScope.launch {
            jobsViewModel.vacancies.collect { vacancies ->
                val totalvacancy = vacancies.size
                Log.d("VacancyAdapter", "totalvacancy: ${totalvacancy}")
                // Изначально показываем только первые 3 вакансии и кнопку "Еще вакансий"
                if (vacancies.size > 3 && !isFullListDisplayed) {
                    vacancyAdapter.updateVacancies(vacancies.take(3), false, totalvacancy)
                } else {
                    vacancyAdapter.updateVacancies(vacancies, true, totalvacancy)

                    // Обновляем текст с количеством вакансий, если список уже полон
                    if (isFullListDisplayed) {
                        val vacancy = wordDeclension.getVacancyCountString(totalvacancy.toInt())
                        quantityVacancyTextView.text = "$vacancy"
                    }
                }
            }
        }
    }
}