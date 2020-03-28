package com.example.teamspeak3app.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.teamspeak3app.R
import com.example.teamspeak3app.ui.login.dataModels.CompleteChannel
import com.example.teamspeak3app.ui.login.views.ChannelWidget
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban
import com.github.theholywaffle.teamspeak3.api.wrapper.Client
import java.util.*
import java.util.stream.Collectors


class MainActivity : AppCompatActivity(), ConnectionCallbackInterface {
    private val TAG = this.javaClass.name
    var connection: TS3Connection? = null
    var recycler: RecyclerView? = null
    var mCompleteChannelList: LinkedList<CompleteChannel> = LinkedList()
    var viewAdapter: RecyclerViewAdapter? = null
    private var myLayoutManager: LinearLayoutManager? = null
    private var refreshLayout: SwipeRefreshLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        connection = TS3Connection(
            this,
            intent.getStringExtra(LoginActivity.LOGIN_HOST),
            intent.getStringExtra(LoginActivity.LOGIN_USERNAME),
            intent.getStringExtra(LoginActivity.LOGIN_PASSWORD)
        )
        connection?.login()


        viewAdapter = RecyclerViewAdapter(mCompleteChannelList, this)
        recycler = findViewById<RecyclerView>(R.id.list_view).apply {
            adapter = viewAdapter
            myLayoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            layoutManager = myLayoutManager
        }


        refreshLayout = findViewById(R.id.refresh_layout)
        refreshLayout?.setOnRefreshListener { connection?.getCompleteChannels() }

    }

    override fun onConnection(connectionInfo: ConnectionInfo) {

        if (connectionInfo.mSuccess) {
            connection?.getCompleteChannels()
        } else {
            connection = null
            val mIntent = Intent(this, LoginActivity::class.java)
            startActivity(mIntent)
            finish()
        }
    }

    override fun onGetClients(clients: List<Client>) {
        /*
        Log.d(TAG, clients.toString())
        memberList.clear()
        for (client in clients) {
            memberList.add(Member(client.nickname, client.isInputMuted.toString(), client.ip))
            Log.d("MainActivity", client.nickname)
        }
        viewAdapter?.mSelectedMemberList?.clear()
        updateUI()
         */
    }

    fun updateUI() {
        runOnUiThread {
            viewAdapter?.notifyDataSetChanged()
            refreshLayout?.isRefreshing = false
        }
    }

    override fun onGetBans(clients: List<Ban>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onGetCompleteChannels(completeChannels: List<CompleteChannel>) {
        mCompleteChannelList.clear()
        mCompleteChannelList.addAll(completeChannels)
        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        connection?.logout()
    }

    override fun onDisconnection(connectionInfo: ConnectionInfo) {
        val mIntent = Intent(this, LoginActivity::class.java)
        startActivity(mIntent)
        finish()
    }


    class RecyclerViewAdapter(completeChannels: List<CompleteChannel>, context: Context) : RecyclerView.Adapter<ViewHolder>() {
        var mCompleteChannels = completeChannels
        val mContext = context


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ChannelWidget(mContext, null, 0))
        }

        override fun getItemCount(): Int {
            return mCompleteChannels.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.mItemView.setName(mCompleteChannels[position].mChannel.name)
            holder.mItemView.setMembers(
                LinkedList<Member<String, String>>(
                    mCompleteChannels[position].mClients.stream().map { client ->
                        Member<String, String>(
                            client.nickname,
                            client.isInputMuted.toString()
                        )
                    }.collect(Collectors.toList())
                )
            )
        }

    }


    class ViewHolder(itemView: ChannelWidget) : RecyclerView.ViewHolder(itemView) {
        val mItemView: ChannelWidget = itemView
    }
}