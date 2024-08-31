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
    private val onFavoriteClick: (Vacancy) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>()  {
    class FavoritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val titleTextView: TextView = itemView.findViewById(R.id.search_vacancy_title)
        private val favoriteImageView: ImageView = itemView.findViewById(R.id.search_like_bttn)
        private val companyTextView: TextView = itemView.findViewById(R.id.search_vacancy_company)
        private val wordDeclension = WordDeclension()

        private val peopleTextView: TextView? = itemView.findViewById(R.id.search_tv_count_of_people)
        //private val titleTextView: TextView? = itemView.findViewById(R.id.search_vacancy_title)
        private val salaryTextView: TextView? = itemView.findViewById(R.id.search_vacancy_salary)
        private val townTextView: TextView? = itemView.findViewById(R.id.search_vacancy_town)
        //private val companyTextView: TextView? = itemView.findViewById(R.id.search_vacancy_company)
        private val experienceTextView: TextView? = itemView.findViewById(R.id.search_vacancy_experience)
        private val publishedDateTextView: TextView? = itemView.findViewById(R.id.search_vacancy_published_date)
        //private val favoriteImageView: ImageView = itemView.findViewById(R.id.search_like_bttn)
        private val buttonrespons: Button = itemView.findViewById(R.id.bt_respons)

        fun bind(vacancy: Vacancy, onFavoriteClick: (Vacancy) -> Unit) {
            if (vacancy.lookingNumber!! > 0) {
                val human = wordDeclension.getHumanCountString(vacancy.lookingNumber!!.toInt())
                peopleTextView?.text = "Сейчас просматривают $human"
                peopleTextView?.visibility = View.VISIBLE
            } else {
                peopleTextView?.visibility = View.GONE
            }

            //titleTextView?.text = vacancy.title
            salaryTextView?.text = vacancy.salary.full
            townTextView?.text = vacancy.address.town
            //companyTextView?.text = vacancy.company
            experienceTextView?.text = vacancy.experience.previewText
            publishedDateTextView?.text = "Опубликовано ${vacancy.publishedDate}"
            buttonrespons.text = itemView.context.getString(R.string.respons)
            titleTextView.text = vacancy.title
            companyTextView.text = vacancy.company
            favoriteImageView.setImageResource(
                R.drawable.fill_heart_icon
            )

            favoriteImageView.setOnClickListener { onFavoriteClick(vacancy) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vacancy_item, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int = vacancies.size

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(vacancies[position], onFavoriteClick)
    }

    fun updateVacancies(newVacancies: List<Vacancy>) {
        this.vacancies = newVacancies
        notifyDataSetChanged()
    }
}