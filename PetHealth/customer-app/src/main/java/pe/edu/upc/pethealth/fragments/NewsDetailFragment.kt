package pe.edu.upc.pethealth.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_my_tip_detail.*
import pe.edu.upc.lib.NewsModel

import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity

class NewsDetailFragment(var news: NewsModel.News) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_my_tip_detail, container, false)
        (activity as MainActivity).setFragmentToolbar("Tip", true, fragmentManager!!)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(news.image).into(detailImageView)
        descriptionDetailTextView.text = news.content
    }

}
