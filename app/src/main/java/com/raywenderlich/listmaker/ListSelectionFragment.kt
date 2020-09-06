package com.raywenderlich.listmaker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ListSelectionFragment : Fragment(),
    ListSelectionRecyclerViewAdapter.ListSelectionRecyclerViewClickListener {
    // Defines a private variable to hold a reference to an object that implements the Fragment interface
    private var listener: OnListItemFragmentInteractionListener? = null

    lateinit var listDataManager: ListDataManager
    lateinit var listsRecyclerView: RecyclerView

    // onAttach is run when the Fragment is first associated with an Activity
    // Assigns the context of the Fragment to listener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListItemFragmentInteractionListener) {
            listener = context
            listDataManager = ListDataManager(context)
        } else {
            throw RuntimeException("$context must implement OnListItemFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val lists = listDataManager.readLists()
        view?.let {
            listsRecyclerView =
                it.findViewById<RecyclerView>(R.id.lists_recyclerview)
            listsRecyclerView.layoutManager = LinearLayoutManager(activity)
            listsRecyclerView.adapter = ListSelectionRecyclerViewAdapter(lists,
                this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    // Fragment acquires the layout to present within the activity
    override fun onCreateView(inflater: LayoutInflater, container:
    ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_selection, container,
            false)
    }
    // Called when a Fragment is no longer attached to an Activity
    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun listItemClicked(list: TaskList) {
        listener?.onListItemClicked(list)
    }

    fun addList(list : TaskList) {
        listDataManager.saveList(list)
        val recyclerAdapter = listsRecyclerView.adapter as
                ListSelectionRecyclerViewAdapter
        recyclerAdapter.addList(list)
    }

    fun saveList(list: TaskList) {
        listDataManager.saveList(list)
        updateLists()
    }

    private fun updateLists() {
        val lists = listDataManager.readLists()
        listsRecyclerView.adapter =
            ListSelectionRecyclerViewAdapter(lists, this)
    }

    interface OnListItemFragmentInteractionListener {
        fun onListItemClicked(list: TaskList)
    }

    companion object {
        fun newInstance(): ListSelectionFragment {
            return ListSelectionFragment()
        }
    }
}

