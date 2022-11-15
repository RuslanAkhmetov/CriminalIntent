package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import androidx.lifecycle.Observer
import java.util.Locale

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecycleView : RecyclerView

    private var adapter : CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel : CrimeListViewModel by lazy{
        ViewModelProvider(this)[CrimeListViewModel::class.java]
    }


    private inner class CrimeHolder(view: View)
        : RecyclerView.ViewHolder(view) , View.OnClickListener{

        private lateinit var crime: Crime

        val titleTextView : TextView = view.findViewById(R.id.crime_title)
        val dateTextView : TextView = view.findViewById(R.id.crime_date)
        val solvedImageView: ImageView = view.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime){
            this.crime = crime
            titleTextView.text = this.crime.title
            val df = SimpleDateFormat("EEEE, MMM, dd, yyyy", Locale.US)
            dateTextView.text = df.format(this.crime.date)
            solvedImageView.visibility = if (crime.isSolved){View.VISIBLE}
                else {View.GONE}

        }

       override fun onClick (view: View){
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private inner class CrimeAdapter (var crimes: List<Crime>)
        :RecyclerView.Adapter<CrimeHolder> (){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = if (viewType == 0) {
                            layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                        } else{
                            layoutInflater.inflate(R.layout.list_item_crime_police, parent, false)
                        }
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int = crimes.size

        override fun getItemViewType(position: Int): Int {
            return if (crimes[position].requiresPolice) 1 else 0
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecycleView = view.findViewById(R.id.crime_recycle_view) as RecyclerView
        crimeRecycleView.layoutManager = LinearLayoutManager(context)

        crimeRecycleView.adapter= adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            this.viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            }
        )
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeAdapter(crimes)
        crimeRecycleView.adapter = adapter
    }

    companion object{
        fun newInstance() : CrimeListFragment {
            return CrimeListFragment()
        }
    }

}