package pe.edu.upc.pethealth.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

import com.squareup.picasso.Picasso

import pe.edu.upc.lib.models.NewsModel
import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity
import pe.edu.upc.pethealth.fragments.NewsDetailFragment

/**
 * Created by genob on 28/09/2017.
 */

class NewsAdapters(var news: ArrayList<NewsModel.News>) : RecyclerView.Adapter<NewsAdapters.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_tips, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val junction = news[position]
        holder.updateFrom(junction)
    }

    override fun getItemCount(): Int = news.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val newsMaterialCardView = view.findViewById<View>(R.id.newsMaterialCardView) as MaterialCardView
        private val newsImageView = view.findViewById<View>(R.id.tipImageView) as ImageView
        private val descriptionTextView = view.findViewById<View>(R.id.tipDescriptionTextView) as TextView

        fun updateFrom(news: NewsModel.News){
            descriptionTextView.text = news.content
            Picasso.get().load(news.image).into(newsImageView)

            newsMaterialCardView.setOnClickListener { view ->
                val context = view.context as MainActivity
                val newFragment = NewsDetailFragment(news)
                context.supportFragmentManager.beginTransaction()
                        .replace(R.id.content, newFragment)
                        .addToBackStack(null)
                        .commit()
            }
        }
    }
}
