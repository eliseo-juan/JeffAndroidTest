package dev.eliseo.jeff.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import dev.eliseo.jeff.R
import dev.eliseo.jeff.data.model.Geoname
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: SuggestionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = SuggestionListAdapter {
            showGeoname(it)
        }

        recyclerViewMain.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.geonameSuggestionList.observe(viewLifecycleOwner, Observer { resource ->
            recyclerViewMain.visibility = if (resource.isNotEmpty()) View.VISIBLE else View.GONE
            adapter.submitList(resource)
        })

        editTextMainSearch.addTextChangedListener {
            viewModel.searchQuery.value = it.toString()
        }

        editTextMainSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                navigateToSearch()
            }
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun navigateToSearch() {
        val extras = FragmentNavigatorExtras(
            cardViewMainContainer to "container",
            editTextMainSearch to "searchEditText",
            recyclerViewMain to "recyclerView"
        )
        view?.findNavController()
            ?.navigate(
                R.id.action_mainFragment_to_searchFragment,
                null,
                null,
                extras
            )
    }

    fun showGeoname(geoname: Geoname) {
        view?.findNavController()
            ?.navigate(MainFragmentDirections.actionMainFragmentToDetailsFragment(geoname.geonameId))
    }

}
