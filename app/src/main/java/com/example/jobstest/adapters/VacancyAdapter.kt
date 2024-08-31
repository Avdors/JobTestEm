package com.example.jobstest.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Vacancy
import com.example.jobstest.R
import com.example.jobstest.utils.WordDeclension

class VacancyAdapter(
    private var vacancies: List<Vacancy>,
    private val onVacancyClick: (Vacancy) -> Unit,
    private val onFavoriteClick: (Vacancy) -> Unit,
    private val onShowMoreClick: () -> Unit // Кнопка "Еще вакансий"
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TYPE_VACANCY = 0
    private val ITEM_TYPE_SHOW_MORE = 1

    // состояние списка вакансий
    private var isFullListDisplayed = false
    // общий список вакансий
    private var totalVacanciesCount: Int = vacancies.size



    // ViewHolder для вакансий
    class VacancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val peopleTextView: TextView? = itemView.findViewById(R.id.search_tv_count_of_people)
        private val titleTextView: TextView? = itemView.findViewById(R.id.search_vacancy_title)
        private val salaryTextView: TextView? = itemView.findViewById(R.id.search_vacancy_salary)
        private val townTextView: TextView? = itemView.findViewById(R.id.search_vacancy_town)
        private val companyTextView: TextView? = itemView.findViewById(R.id.search_vacancy_company)
        private val experienceTextView: TextView? = itemView.findViewById(R.id.search_vacancy_experience)
        private val publishedDateTextView: TextView? = itemView.findViewById(R.id.search_vacancy_published_date)
        private val favoriteImageView: ImageView = itemView.findViewById(R.id.search_like_bttn)
        private val buttonrespons: Button = itemView.findViewById(R.id.bt_respons)
        private val wordDeclension = WordDeclension()

        fun bind(vacancy: Vacancy, onVacancyClick: (Vacancy) -> Unit, onFavoriteClick: (Vacancy) -> Unit) {
            if (vacancy.lookingNumber!! > 0) {
                val human = wordDeclension.getHumanCountString(vacancy.lookingNumber!!.toInt())
                peopleTextView?.text = "Сейчас просматривают $human"
                peopleTextView?.visibility = View.VISIBLE
            } else {
                peopleTextView?.visibility = View.GONE
            }

            titleTextView?.text = vacancy.title
            salaryTextView?.text = vacancy.salary.full
            townTextView?.text = vacancy.address.town
            companyTextView?.text = vacancy.company
            experienceTextView?.text = vacancy.experience.previewText
            publishedDateTextView?.text = "Опубликовано ${vacancy.publishedDate}"
            buttonrespons.text = itemView.context.getString(R.string.respons)

            favoriteImageView.setImageResource(
                if (vacancy.isFavorite) R.drawable.fill_heart_icon else R.drawable.heart_icon
            )

            itemView.setOnClickListener { onVacancyClick(vacancy) }
            favoriteImageView.setOnClickListener { onFavoriteClick(vacancy) }
        }
    }

    // ViewHolder для кнопки "Еще вакансий"
    class ShowMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val showMoreButton: Button = itemView.findViewById(R.id.more_vacancies_button)
        private val wordDeclension = WordDeclension()
        fun bind(onShowMoreClick: () -> Unit, remainingCount: Int) {

            val vacancy = wordDeclension.getVacancyCountString(remainingCount.toInt())
            showMoreButton.text = "Еще $vacancy"
            showMoreButton.setOnClickListener { onShowMoreClick() }
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (position < 3|| isFullListDisplayed) ITEM_TYPE_VACANCY else ITEM_TYPE_SHOW_MORE

    }

    fun updateVacancies(allVacancies: List<Vacancy>, showFullList: Boolean, totalVacanciesCount: Int) {
        isFullListDisplayed = showFullList
        this.totalVacanciesCount = totalVacanciesCount
        this.vacancies = allVacancies
        notifyDataSetChanged() // Обновляем адаптер
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_VACANCY) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
            VacancyViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show_more_button, parent, false)
            ShowMoreViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        // Возвращаем размер списка + 1, чтобы показать кнопку "Еще вакансий"
        return if (isFullListDisplayed) vacancies.size else 4
       // return if (vacancies.size >= 3) 4 else vacancies.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_TYPE_VACANCY) {
            val vacancy = vacancies[position]
            Log.d("VacancyAdapter", "Binding vacancy at position: $position")
            (holder as VacancyViewHolder).bind(vacancy, onVacancyClick, onFavoriteClick)
        } else {
            Log.d("VacancyAdapter", "Binding show more button at position: $position")

            val remainingVacancies = totalVacanciesCount - 3
            (holder as ShowMoreViewHolder).bind(onShowMoreClick, remainingVacancies)
        }
    }
}