package com.example.jobstest.screens

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.domain.model.Vacancy
import com.example.jobstest.R
import com.example.jobstest.viewmodel.JobsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class CardVacancy : Fragment() {
    private lateinit var vacancy: Vacancy
    private val jobsViewModel: JobsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_vacancy, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vacancy = arguments?.getParcelable("vacancy", Vacancy::class.java) ?: return

        // Инициализация и установка данных в элементы макета
        val titleTextView: TextView = view.findViewById(R.id.card_vacancy_title)
        val salaryTextView: TextView = view.findViewById(R.id.card_vacancy_salary)
        val experienceTextView: TextView = view.findViewById(R.id.card_vacancy_experience)
        val schedulesTextView: TextView = view.findViewById(R.id.card_vacancy_published_date)
        val appliedNumberTextView: TextView = view.findViewById(R.id.eye_first_line)
        val lookingNumberTextView: TextView = view.findViewById(R.id.second_line)
        val companyTextView: TextView = view.findViewById(R.id.card_company)
        val addressTextView: TextView = view.findViewById(R.id.card_adress)
        val descriptionTextView: TextView = view.findViewById(R.id.card_description)
        val responsibilitiesTextView: TextView = view.findViewById(R.id.card_task_info)
        val backButton: ImageView = view.findViewById(R.id.card_back_arrow)
        val likeButton: ImageView = view.findViewById(R.id.search_like_bttn)
        val responseButton: Button = view.findViewById(R.id.bt_respons)

        // раздел вопросы
        val cardListQuestionLayout = view.findViewById<LinearLayout>(R.id.card_list_question)
        cardListQuestionLayout.removeAllViews()
        // Для каждого элемента массива создаем кнопку
        vacancy.questions?.forEach { question ->
            val button = Button(requireContext()).apply {
                text = question
                setBackgroundResource(R.drawable.button_back_grey2)
                setTextAppearance(R.style.RegularText_14size)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    // Добавляем отступы для кнопки
                    setMargins(
                        0,
                        resources.getDimensionPixelSize(R.dimen.dp16),
                        0,
                        0)
                }
                // отступы внутри
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.dp16), // left
                    resources.getDimensionPixelSize(R.dimen.dp8),  // top
                    resources.getDimensionPixelSize(R.dimen.dp16), // right
                    resources.getDimensionPixelSize(R.dimen.dp8)   // bottom
                )
                transformationMethod = null

                setOnClickListener {
                    // Обработка нажатия на вопрос
                }
            }
            // Добавляем кнопку в LinearLayout
            cardListQuestionLayout.addView(button)
        }


        // Пример установки данных
        titleTextView.text = vacancy.title
        salaryTextView.text = vacancy.salary.full
        experienceTextView.text = "Требуемый опыт: ${vacancy.experience.text}"
        schedulesTextView.text = vacancy.schedules
            .mapIndexed { index, schedule ->
                if (index == 0) schedule.replaceFirstChar { char -> char.uppercaseChar() }
                else schedule
            }
            .joinToString(", ")

        if (vacancy.appliedNumber != null) {
            appliedNumberTextView.text = "${vacancy.appliedNumber} человек уже откликнулись"
        } else {
            appliedNumberTextView.visibility = View.GONE
        }

        if (vacancy.lookingNumber != null) {
            lookingNumberTextView.text = "${vacancy.lookingNumber} человека сейчас смотрят"
        } else {
            lookingNumberTextView.visibility = View.GONE
        }

        companyTextView.text = vacancy.company
        addressTextView.text = "${vacancy.address.town}, ${vacancy.address.street}, ${vacancy.address.house}"
        descriptionTextView.text = vacancy.description

        responsibilitiesTextView.text = vacancy.responsibilities

        // Назад
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // кнопка "Избранное"
        likeButton.setOnClickListener {
            jobsViewModel.toggleFavorite(vacancy)
        }

        // Откликнутся
        responseButton.setOnClickListener {

        }
    }


}