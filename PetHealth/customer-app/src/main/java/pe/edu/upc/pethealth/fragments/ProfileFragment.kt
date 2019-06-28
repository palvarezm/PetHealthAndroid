package pe.edu.upc.pethealth.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import kotlinx.android.synthetic.main.fragment_profile.*
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import pe.edu.upc.lib.models.Pet
import pe.edu.upc.pethealth.R
import pe.edu.upc.pethealth.activities.AddPetActivity
import pe.edu.upc.pethealth.activities.MainActivity
import pe.edu.upc.pethealth.adapters.PetsAdapter
import pe.edu.upc.pethealth.models.Person
import pe.edu.upc.pethealth.network.PetHealthApiService
import pe.edu.upc.pethealth.persistence.SharedPreferencesManager

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {
    private var person: Person? = null
    private lateinit var petsAdapter: PetsAdapter
    private var sharedPreferencesManager: SharedPreferencesManager? = null

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu!!.add(resources.getString(R.string.bt_edit_profile))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == 0) {
            val userInformationFragment = UserInformationFragment()
            fragmentManager!!.beginTransaction().addToBackStack(null).replace(R.id.content, userInformationFragment).commit()
            return true
        } else
            return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProfileFragment.context)
            adapter = petsAdapter
        }
        newPetTextView.setOnClickListener { view ->
            val context = view.context
            val intent = Intent(context, AddPetActivity::class.java)
            context.startActivity(intent)
        }
        updateProfile()
        updatePets()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        (activity as MainActivity).setFragmentToolbar(resources.getString(R.string.title_profile), false, fragmentManager)
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        sharedPreferencesManager = this.context?.let { SharedPreferencesManager.getInstance(it) }
        person = sharedPreferencesManager!!.person
        petsAdapter = PetsAdapter(ArrayList())
        setHasOptionsMenu(true)
        return view
    }

    private fun updatePets() {
        Log.d("TOKEN", sharedPreferencesManager!!.accessToken)
        AndroidNetworking.get(PetHealthApiService.PET_URL)
                .addPathParameter("userId", Integer.toString(sharedPreferencesManager!!.user!!.id))
                .addHeaders("access_token", sharedPreferencesManager!!.accessToken)
                .setPriority(Priority.LOW)
                .setTag(R.string.app_name)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            response.toString()
                            val pets = Gson().fromJson<ArrayList<Pet>>(response.getJSONArray("data").toString())
                            petsAdapter.pets = pets
                            petsAdapter.notifyDataSetChanged()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    override fun onError(anError: ANError) {
                        Log.d(getString(R.string.app_name), anError.errorBody)
                    }
                })
    }

    private fun updateProfile() {
        if (sharedPreferencesManager!!.user!!.photo.isNotEmpty()){
            Picasso.get().load(sharedPreferencesManager!!.user!!.photo).error(R.mipmap.ic_launcher)
                    .transform(RoundedCornersTransformation(10, 0))
                    .resize(150, 170)
                    .centerCrop()
                    .into(profileImageView)
        }
        else{
            profileImageView.setImageResource(R.mipmap.ic_launcher)
        }
        nameTextView.text = person!!.name + " " + person!!.lastName
        documentNumberTextView.text = person!!.dni
        phoneTextView.text = person!!.phone
        addressTextView.text = person!!.address
    }

}