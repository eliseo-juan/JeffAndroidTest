package dev.eliseo.jeff.ui.search

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.transition.TransitionInflater
import dev.eliseo.jeff.R
import dev.eliseo.jeff.data.manager.Status
import dev.eliseo.jeff.data.model.Geoname
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_fragment.toolbar


class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextSearch.addTextChangedListener {
            viewModel.setQuery(it.toString())
        }

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_24)
        toolbar.setNavigationOnClickListener {
            (activity as AppCompatActivity).onBackPressed()
            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        editTextSearch.requestFocus()

        val imm: InputMethodManager? = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = SearchListAdapter {
            showGeoname(it)
        }
        recyclerViewSearch.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)

        viewModel.geonameSearchList.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                //TODO Loading/Error
                Status.SUCCESS -> adapter.submitList(it.data)
            }
        })
    }

    private fun showGeoname(geoname: Geoname) {
        view?.findNavController()
            ?.navigate(SearchFragmentDirections.actionSearchFragmentToDetailsFragment(geoname.geonameId))
    }
}
