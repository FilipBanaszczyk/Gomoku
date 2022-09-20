package com.example.myapplication

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_match_history_popup.*
import java.text.SimpleDateFormat

class AdapterMatchHistory(private var match_list :ArrayList<GameLog>, val ctx: Context):
    RecyclerView.Adapter<AdapterMatchHistory.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    var chosen_index = -1
    val vh :Array<ViewHolder?> = arrayOfNulls<ViewHolder>(match_list.size)
    interface OnClickListener
    {
        fun onItemClick(index: Int)
    }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        val item = view.findViewById<LinearLayout>(R.id.match_list_item)
        var tv_winner = view.findViewById<TextView>(R.id.tv_winner_ad)
        //val typeface = Typeface.createFromAsset(assets, "fonts/yourfont.ttf")

        val tv_date = view.findViewById<TextView>(R.id.tv_date_ad)
        val tv_mode = view.findViewById<TextView>(R.id.tv_mode_ad)


        fun bindOnClickListener(onClickListener: OnClickListener?, index: Int)
        {
            item.setOnClickListener{view -> onClickListener?.onItemClick(index)}

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.adapter_match_history, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tv_winner.text = match_list[position].winner
        holder.tv_date.text = SimpleDateFormat("yyyy/MM/dd").format(SimpleDateFormat("yyyy/MM/dd/hh/mm/ss").parse(match_list[position].date))
        holder.tv_mode.text = match_list[position].diffLevel
        //Log.d("bind", position.toString())

        if(position==chosen_index){
            if(match_list[position].winner.equals("AI")){

                val resID = ctx.resources.getIdentifier("lost_match_chosen", "drawable", ctx.packageName)
                holder.item.background= ActivityCompat.getDrawable(ctx,resID)

            }
            else{
                val resID = ctx.resources.getIdentifier("won_match_chosen", "drawable", ctx.packageName)
                holder.item.background= ActivityCompat.getDrawable(ctx,resID)


            }
        }
        else{
            if(match_list[position].winner.equals("AI")){

                val resID = ctx.resources.getIdentifier("lost_match", "drawable", ctx.packageName)
                holder.item.background= ActivityCompat.getDrawable(ctx,resID)

            }
            else{
                val resID = ctx.resources.getIdentifier("match_details", "drawable", ctx.packageName)
                holder.item.background= ActivityCompat.getDrawable(ctx,resID)


            }
        }
        vh[position] = holder
        holder.bindOnClickListener(onClickListener,position)


    }
    override fun getItemCount(): Int {
        return match_list.size
    }
    fun choose(index : Int){
        chosen_index = index


            for (i in 0 until match_list.size){
                val holder = vh[i]
                if(holder!=null){
                    if(i==index){
                        if(match_list[i].winner.equals("AI")){

                            val resID = ctx.resources.getIdentifier("lost_match_chosen", "drawable", ctx.packageName)
                            holder.item.background= ActivityCompat.getDrawable(ctx,resID)
                            //vh[index].tv_mode.text = "chosen"
                            notifyItemChanged(i)

                        }
                        else{
                            val resID = ctx.resources.getIdentifier("won_match_chosen", "drawable", ctx.packageName)
                            holder.item.background= ActivityCompat.getDrawable(ctx,resID)
                            //vh[index].tv_mode.text = "chosen"
                            notifyItemChanged(i)

                        }
                    }
                    else{
                        if(match_list[i].winner.equals("AI")){

                            val resID = ctx.resources.getIdentifier("lost_match", "drawable", ctx.packageName)
                            holder.item.background= ActivityCompat.getDrawable(ctx,resID)
                            notifyItemChanged(i)
                        }
                        else{
                            val resID = ctx.resources.getIdentifier("match_details", "drawable", ctx.packageName)
                            holder.item.background= ActivityCompat.getDrawable(ctx,resID)
                            notifyItemChanged(i)

                        }
                    }
                }
        }

    }






    fun setOnClickListener(onClickListener: OnClickListener)
    {
        this.onClickListener = onClickListener
    }

}