package com.example.jobstest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Offer
import com.example.jobstest.R


class OfferAdapter(
    private var offers: List<Offer>,
    private val onItemClick: (String) -> Unit
): RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {


    class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.offer_title)
        private val buttonTextView: TextView = itemView.findViewById(R.id.search_bttn_item_offers)
        private val iconImageView: ImageView = itemView.findViewById(R.id.search_item_logo_recommendation)

        fun bind(offer: Offer, onItemClick: (String) -> Unit) {
            // Установка иконки в зависимости от id
            val iconResId = when (offer.id) {
                "near_vacancies" -> R.drawable.near_vacancies_icon
                "level_up_resume" -> R.drawable.level_up_resume_icon
                "temporary_job" -> R.drawable.temporary_job_icon
                else -> null
            }

            if (iconResId != null) {
                iconImageView.setImageResource(iconResId)
                iconImageView.visibility = View.VISIBLE
            } else {
                iconImageView.visibility = View.GONE
            }

            // Установка заголовка
            titleTextView.text = offer.title
            titleTextView.maxLines = if (offer.button != null) 2 else 3

            // Установка текста кнопки (выделенного текста)
            if (offer.button != null) {
                buttonTextView.text = offer.button!!.text
                buttonTextView.visibility = View.VISIBLE
            } else {
                buttonTextView.visibility = View.GONE
            }

            // Настраиваем клик по всей карточке
            itemView.setOnClickListener {
                onItemClick(offer.link)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.offer_item, parent, false)
        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        holder.bind(offers[position], onItemClick)
    }

    override fun getItemCount(): Int = offers.size

    // Метод для обновления данных в адаптере
    fun updateOffers(newOffers: List<Offer>) {
        this.offers = newOffers
        notifyDataSetChanged() // Обновляем адаптер
    }
}