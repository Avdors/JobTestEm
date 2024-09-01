package com.example.jobstest.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
    private lateinit var accordanceWithTextView: TextView
    private lateinit var searchEditText: EditText
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
        accordanceWithTextView = view.findViewById(R.id.tw_accordance_with)
        searchEditText = view.findViewById(R.id.field_search_et)


        // Инициализация RecyclerView
        val offerRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_offer)
        offerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val vacancyRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_vacancy)
        vacancyRecyclerView.layoutManager = LinearLayoutManager(context)

        // расстояние между карточками через ItemDecoration
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp8)
        vacancyRecyclerView.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        // Инициализация адаптера
        offerAdapter = OfferAdapter(emptyList()) { link ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            startActivity(browserIntent)
        }
        offerRecyclerView.adapter = offerAdapter

        // Создам адаптер для вакансий с логикой обработки кнопки "Еще вакансий"
        vacancyAdapter = VacancyAdapter(
            emptyList(),
            onVacancyClick = { vacancy ->
                // Переход к CardVacancyFragment
                val bundle = Bundle().apply {
                    putParcelable("vacancy", vacancy)
                }
                findNavController().navigate(
                    R.id.action_searchFragment_to_cardVacancyFragment,
                    bundle
                )
            },
            // клик по кнопке избранное
            onFavoriteClick = { vacancy ->
                jobsViewModel.toggleFavorite(vacancy)
                vacancyAdapter.updateVacancies(jobsViewModel.vacancies.value, isFullListDisplayed, jobsViewModel.vacancies.value.size)
            },
            onShowMoreClick = {
                // Логика загрузки всех вакансий при нажатии на кнопку "Еще вакансий"
                isFullListDisplayed = true
                vacancyAdapter.updateVacancies(jobsViewModel.vacancies.value, true, jobsViewModel.vacancies.value.size)

                // Скрываю верхний список предложений
                offerRecyclerView.visibility = View.GONE

                quantityVacancyTextView.visibility = View.VISIBLE
                accordanceWithTextView.visibility = View.VISIBLE

                // Изменяю drawable в EditText на стрелку назад
                searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.backarrow, 0, 0, 0)

                // Устанавливаю обработчик нажатия на EditText для возврата списка к первоначальному состоянию
                searchEditText.setOnClickListener {
                    if(isFullListDisplayed){
                        resetVacancyList(offerRecyclerView)
                    }

                }


            },
            onApplyClick = { vacancy ->
                val responseDialog = ResponseDialog()
                responseDialog.show(requireActivity().supportFragmentManager, "ResponseDialog")
            }
        )
        vacancyRecyclerView.adapter = vacancyAdapter

        // Наблюдаю за списком offers
        lifecycleScope.launch {
            jobsViewModel.offers.collect { offers ->
                offerAdapter.updateOffers(offers)  // Обновление данных в адаптере
            }
        }

        // Наблюдаю за списком vacancies
        lifecycleScope.launch {
            jobsViewModel.vacancies.collect { vacancies ->
                val totalvacancy = vacancies.size

                val vacancy = wordDeclension.getVacancyCountString(totalvacancy.toInt())

                quantityVacancyTextView.text = "$vacancy"
                // Изначально показываю только первые 3 вакансии и кнопку "Еще вакансий"
                if (vacancies.size > 3 && !isFullListDisplayed) {
                    vacancyAdapter.updateVacancies(vacancies.take(3), false, totalvacancy)
                } else {
                    vacancyAdapter.updateVacancies(vacancies, true, totalvacancy)



                }
            }
        }
    }

    private fun resetVacancyList(offerRecyclerView: RecyclerView?) {

        isFullListDisplayed = false
        // Отображаю только первые 3 вакансии
        vacancyAdapter.updateVacancies(jobsViewModel.vacancies.value.take(3), false, jobsViewModel.vacancies.value.size)

        offerRecyclerView?.visibility = View.VISIBLE

        // Скрываю количество вакансий и текст "Соответствие"
        quantityVacancyTextView.visibility = View.GONE
        accordanceWithTextView.visibility = View.GONE

        // Возвращаю иконку поиска в EditText
        searchEditText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search_icon, 0, 0, 0)


    }
}