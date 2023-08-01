package com.notingapp.clonemynotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.notingapp.clonemynotes.Adapter.NoteAdapter
import com.notingapp.clonemynotes.Database.NoteDatabase
import com.notingapp.clonemynotes.Models.Note
import com.notingapp.clonemynotes.Models.NoteViewModel
import com.notingapp.clonemynotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NoteAdapter.NotesClickListener,
    PopupMenu.OnMenuItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    private lateinit var viewModel: NoteViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var selectedNote: Note

    private val update =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as? Note
                if (note != null) {
                    viewModel.updateNote(note)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)


        viewModel.allNotes.observe(this) { list ->
            list?.let {
                adapter.updateList(list)
                if (list.isEmpty()){
                    visibilityOfRecyclerAndNoMsg(true)
                }else{
                    visibilityOfRecyclerAndNoMsg(false)
                }
            }
        }

        initUI()

        database = NoteDatabase.getDatabase(this)

    }

    private fun visibilityOfRecyclerAndNoMsg(state: Boolean) {
        if (state){
            binding.recyclerView.visibility = View.GONE
            binding.txtEmptyNoteList.visibility = View.VISIBLE
        }else{
            binding.recyclerView.visibility = View.VISIBLE
            binding.txtEmptyNoteList.visibility = View.GONE
        }
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        adapter = NoteAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val note = result.data?.getSerializableExtra("note") as? Note
                    if (note != null) {
                        viewModel.insertNote(note)
                    }
                }
            }

        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterNotes(newText.trim().lowercase())
                }
                return true
            }

        })

        binding.fbAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            getContent.launch(intent)
        }

    }

    override fun onItemClickListener(note: Note) {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("current_note", note)
        update.launch(intent)
    }

    override fun onLongItemClickListener(note: Note, cardView: CardView) {
        selectedNote = note
        popupDisplay(cardView)
    }

    private fun popupDisplay(cardView: CardView) {
        val popup = PopupMenu(this@MainActivity, cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_note) {
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}