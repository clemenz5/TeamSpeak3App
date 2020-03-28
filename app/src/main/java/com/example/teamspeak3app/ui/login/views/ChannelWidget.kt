package com.example.teamspeak3app.ui.login.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teamspeak3app.R
import com.example.teamspeak3app.ui.login.MainActivity
import com.example.teamspeak3app.ui.login.Member
import java.util.*

class ChannelWidget @JvmOverloads constructor(context: Context,
                    attrs: AttributeSet? = null,
                    defStyle: Int = 0): LinearLayout(context, attrs, defStyle) {

    var channelNameView:TextView? = null
    var recycler: RecyclerView? = null
    var memberList: LinkedList<Member<String, String>> = LinkedList()
    var viewAdapter: RecyclerViewAdapter? = null
    private var myLayoutManager: LinearLayoutManager? = null

    init {
        val view:View = LayoutInflater.from(context).inflate(R.layout.channel_widget_layout, this, true)
        channelNameView = view.findViewById(R.id.channel_name_view)
        viewAdapter = RecyclerViewAdapter(memberList)
        recycler = view.findViewById<RecyclerView>(R.id.channel_members_view).apply {
            adapter = viewAdapter
            myLayoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            layoutManager = myLayoutManager
        }
    }

    public fun setName(string:String){
        channelNameView?.text = string
    }

    public fun setMembers(list:LinkedList<Member<String,String>>){
        memberList.clear()
        memberList.addAll(list)
        viewAdapter?.notifyDataSetChanged()
    }

    class RecyclerViewAdapter(clients: List<Member<String, String>>) :
        RecyclerView.Adapter<ViewHolder>() {
        var mMemberList = clients
        var mSelectedMemberList: LinkedList<Member<String, String>> = LinkedList()


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.member_layout, parent, false))

        }

        override fun getItemCount(): Int {
            return mMemberList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.setMember(mMemberList[position])

            if (mSelectedMemberList.contains(mMemberList[position])) {
                holder.setSelectedLayout(true)
            } else {
                holder.setSelectedLayout(false)
            }

            holder.setOnLongClickListener(View.OnLongClickListener {
                if (mSelectedMemberList.contains(mMemberList[position])) {
                    mSelectedMemberList.remove(mMemberList[position])
                    notifyDataSetChanged()
                } else {
                    mSelectedMemberList.add(mMemberList[position])
                    notifyDataSetChanged()
                }
                true
            })
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mItemView = itemView
        var entry1View: TextView? = null
        var entry2View: TextView? = null
        var entry3View: TextView? = null
        var containerLayout: LinearLayout? = null

        init {
            entry1View = mItemView.findViewById(R.id.member_entry1_view)
            entry2View = mItemView.findViewById(R.id.member_entry2_view)
            containerLayout = mItemView.findViewById(R.id.container_layout)
        }

        fun setMember(member: Member<String, String>) {
            entry1View?.text = member.entry1
            entry2View?.text = member.entry2
        }

        fun setSelectedLayout(selected: Boolean) {
            if (selected) {
                containerLayout?.setBackgroundColor(mItemView.resources.getColor(R.color.colorAccent))
            } else {
                containerLayout?.setBackgroundColor(mItemView.resources.getColor(R.color.trans))
            }
        }

        fun setOnLongClickListener(clickListener: View.OnLongClickListener) {
            mItemView.setOnLongClickListener(clickListener)
        }
    }

}