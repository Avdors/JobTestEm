package com.example.jobstest.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobstest.R
import com.example.jobstest.adapters.FavoritesAdapter
import com.example.jobstest.utils.SpacesItemDecoration
import com.example.jobstest.utils.WordDeclension
import com.example.jobstest.viewmodel.JobsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class Favorites : Fragment() {
    private val jobsViewModel: JobsViewModel by viewModel()
    private val wordDeclension = WordDeclension()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("BottomNav", "Favorites start")
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val vacancyRecyclerView = view.findViewById<RecyclerView>(R.id.favorites_recycler_vacancy)
        vacancyRecyclerView.layoutManager = LinearLayoutManager(context)

        // расстояние между карточками через ItemDecoration
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.dp8)
        vacancyRecyclerView.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        favoritesAdapter = FavoritesAdapter(
            emptyList(),
            onVacancyClick = { vacancy ->
                // Переход к CardVacancyFragment
                val fragment = CardVacancy().apply {
                    arguments = Bundle().apply {
                        putParcelable("vacancy", vacancy)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.content, fragment)
                    .addToBackStack(null)
                    .commit()
            },
            onFavoriteClick = { vacancy ->
                jobsViewModel.toggleFavorite(vacancy)
            },
            onApplyClick = { vacancy ->
                val responseDialog = ResponseDialog()
                responseDialog.show(requireActivity().supportFragmentManager, "ResponseDialog")
            })
        vacancyRecyclerView.adapter = favoritesAdapter

        // Подписываюсь на обновление списка избранных вакансий
        lifecycleScope.launch {
            jobsViewModel.favoriteVacancies.collect { vacancies ->
                favoritesAdapter.updateVacancies(vacancies)

                // Обновляю текст с количеством вакансий
                val quantityVacancyTextView = view.findViewById<TextView>(R.id.quantity_vacancy)
                val vacancy = wordDeclension.getVacancyCountString(vacancies.size)
                quantityVacancyTextView.text = "$vacancy"
            }
        }
    }
}