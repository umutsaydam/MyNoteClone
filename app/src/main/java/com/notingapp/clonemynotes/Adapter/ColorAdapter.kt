package com.notingapp.clonemynotes.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.notingapp.clonemynotes.R

class ColorAdapter(private val context: Context) :
    RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
    private lateinit var colorList: List<Int>
    private var selectedBgColor: Int = R.color.NoteColor0
    private val scale = context.resources.displayMetrics.density
    private var lastChecked :RadioButton? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ColorAdapter.ColorViewHolder {
        return ColorViewHolder(
            LayoutInflater.from(context).inflate(R.layout.bg_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ColorAdapter.ColorViewHolder, position: Int) {
        var isSelected: Boolean = false
        holder.colorRadio.setBackgroundColor(colorList[position])

        if (position == 0 && lastChecked == null){
            lastChecked = holder.colorRadio
            holder.colorRadio.isChecked = true
        }

        holder.colorRadio.setOnClickListener {
            if (holder.colorRadio.isChecked){
                if (lastChecked != null){
                    lastChecked?.isChecked = false
                }
                lastChecked = holder.colorRadio
            }
            selectedBgColor = colorList[position]
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    fun getSelectedBgColor(): Int {
        return selectedBgColor
    }

    fun setColorList(list: List<Int>) {
        colorList = list
        notifyDataSetChanged()
    }

    class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bgCardview = itemView.findViewById<CardView>(R.id.bgCardview)
        val colorRadio = itemView.findViewById<RadioButton>(R.id.colorRadio)
    }
}