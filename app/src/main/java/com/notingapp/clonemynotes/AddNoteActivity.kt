package com.notingapp.clonemynotes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.notingapp.clonemynotes.Adapter.ColorAdapter
import com.notingapp.clonemynotes.Models.Note
import com.notingapp.clonemynotes.databinding.ActivityAddNoteBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var oldNote: Note
    private var selectedBgColor: Int? = null
    private var isUpdate = false

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            oldNote = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(oldNote.title)
            binding.etNote.setText(oldNote.note)
            binding.addNoteBg.setBackgroundColor(
                Color.parseColor(
                    "#" + Integer.toHexString(
                        oldNote.bg_color
                    )
                )
            )
            isUpdate = true
            selectedBgColor = oldNote.bg_color
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.imgDone.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val noteDesc = binding.etNote.text.toString().trim()
            if (title.isNotEmpty() && noteDesc.isNotEmpty()) {
                val formatter = SimpleDateFormat("EEE, d, yyyy HH:mm a")
                if (selectedBgColor == null){
                    selectedBgColor = ContextCompat.getColor(this, R.color.NoteColor0)
                }
                note = if (isUpdate){
                    Note(oldNote.id, title, noteDesc, formatter.format(Date()), selectedBgColor!!)
                }else{
                    Note(0, title, noteDesc, formatter.format(Date()), selectedBgColor!!)
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(
                    "note",
                    note
                )
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }

        binding.imgBack.setOnClickListener { onBackPressed() }


        binding.imgBg.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_bg_color)
            val colorAdapter = ColorAdapter(this)

            val colors = this.resources.getIntArray(R.array.noteBackgroundColor)

            val colorList = ArrayList<Int>()
            colors.forEach { colorList.add(it) }
            colorAdapter.setColorList(colorList)


            val recyclerColor = dialog.findViewById<RecyclerView>(R.id.recyclerColor)
            recyclerColor.adapter = colorAdapter

            dialog.findViewById<Button>(R.id.saveColor).setOnClickListener {
                selectedBgColor = colorAdapter.getSelectedBgColor()
                binding.addNoteBg.setBackgroundColor(
                    Color.parseColor(
                        "#" + Integer.toHexString(
                            selectedBgColor!!
                        )
                    )
                )
                dialog.dismiss()
            }

            dialog.show()
        }

    }
}