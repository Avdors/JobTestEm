package com.example.jobstest.adapters

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

class FavoritesAdapter(
    private var vacancies: List<Vacancy>,
    private val onVacancyClick: (Vacancy) -> Unit,
    private val onFavoriteClick: (Vacancy) -> Unit,
    private val onApplyClick: (Vacancy) -> Unit // кнопка Откликнутся
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>()  {
    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val titleTextView: TextView = itemView.findViewById(R.id.item_vacancy_title)
        private val favoriteImageView: ImageView = itemView.findViewById(R.id.search_like_bttn)
        private val companyTextView: TextView = itemView.findViewById(R.id.item_vacancy_company)
        private val wordDeclension = WordDeclension()
        private val peopleTextView: TextView? = itemView.findViewById(R.id.item_peopl_count)
        private val salaryTextView: TextView? = itemView.findViewById(R.id.item_vacancy_salary)
        private val townTextView: TextView? = itemView.findViewById(R.id.item_vacancy_adress)
        private val experienceTextView: TextView? = itemView.findViewById(R.id.item_experience)
        private val publishedDateTextView: TextView? = itemView.findViewById(R.id.item_date)
        private val buttonrespons: Button = itemView.findViewById(R.id.respons_button)

        fun bind(vacancy: Vacancy,
                 onVacancyClick: (Vacancy) -> Unit,
                 onFavoriteClick: (Vacancy) -> Unit,
                 onApplyClick: (Vacancy) -> Unit) {
            if (vacancy.lookingNumber!! > 0) {
                val human = wordDeclension.getHumanCountString(vacancy.lookingNumber!!.toInt())
                peopleTextView?.text = "Сейчас просматривают $human"
                peopleTextView?.visibility = View.VISIBLE
            } else {
                peopleTextView?.visibility = View.GONE
            }


            salaryTextView?.text = vacancy.salary.full
            townTextView?.text = vacancy.address.town

            experienceTextView?.text = vacancy.experience.previewText
            publishedDateTextView?.text = "Опубликовано ${vacancy.publishedDate}"
            buttonrespons.text = itemView.context.getString(R.string.respons)
            titleTextView.text = vacancy.title
            companyTextView.text = vacancy.company
            favoriteImageView.setImageResource(
                R.drawable.fill_heart_icon
            )

            // Обработка нажатия на весь элемент
            itemView.setOnClickListener { onVacancyClick(vacancy) }

            favoriteImageView.setOnClickListener { onFavoriteClick(vacancy) }

            buttonrespons.setOnClickListener { onApplyClick(vacancy) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int = vacancies.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(vacancies[position],onVacancyClick, onFavoriteClick, onApplyClick)
    }

    fun updateVacancies(newVacancies: List<Vacancy>) {
        this.vacancies = newVacancies
        notifyDataSetChanged()
    }
}