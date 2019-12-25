package dev.eliseo.jeff.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.transition.TransitionInflater

import dev.eliseo.jeff.R
import dev.eliseo.jeff.data.manager.Status
import dev.eliseo.jeff.data.model.Geoname
import kotlinx.android.synthetic.main.search_fragment.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        adapter = SearchListAdapter {
             showGeoname(it)
        }

        recyclerViewSearch.adapter = adapter

        editTextSearch.addTextChangedListener {
            viewModel.setQuery(it.toString())
        }

        viewModel.geonameSearchList.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                //TODO
                Status.SUCCESS -> adapter.submitList(it.data)
            }
        })
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    fun showGeoname(geoname: Geoname) {
        view?.findNavController()?.navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(geoname.geonameId))
    }
}
