package pe.edu.upc.pethealth.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList

import pe.edu.upc.lib.NewsModel
import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.MainActivity
import pe.edu.upc.pethealth.adapters.NewsAdapters
import pe.edu.upc.pethealth.network.PetHealthApiService
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private lateinit var myTipRecyclerView: RecyclerView
    internal lateinit var newsAdapters: NewsAdapters
    private lateinit var myTipLayoutManager: RecyclerView.LayoutManager
    internal lateinit var news: ArrayList<NewsModel.News>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setFragmentToolbar("Home", false, fragmentManager!!)
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        myTipRecyclerView = view.findViewById<View>(R.id.myTipRecyclerView) as RecyclerView
        news = ArrayList()
        newsAdapters = NewsAdapters(news)
        myTipLayoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        myTipRecyclerView.adapter = newsAdapters
        myTipRecyclerView.layoutManager = myTipLayoutManager
        val user = SharedPreferencesManager.getInstance(this.context!!).user
        val person = SharedPreferencesManager.getInstance(this.context!!).person
        Log.d("TESTING Person data:", person!!.toString())
        Log.d("TESTING User data:", user!!.toString())
        updateTips()
        return view
    }

    private fun updateTips() {
        AndroidNetworking.get(PetHealthApiService.TIP_URL)
                .addHeaders("access_token", SharedPreferencesManager.getInstance(this.context!!).accessToken)
                .setPriority(Priority.LOW)
                .setTag(getString(R.string.app_name))
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            val data = response.getJSONArray("data")
                            news = Gson().fromJson(data.toString())
                            newsAdapters.news = news
                            newsAdapters.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                    }

                    override fun onError(anError: ANError) {
                        Log.d(getString(R.string.app_name), anError.localizedMessage)
                    }
                })
    }

}// Required empty public constructor
