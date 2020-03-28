package com.example.teamspeak3app.ui.login

import android.os.AsyncTask
import android.util.Log
import com.example.teamspeak3app.ui.login.dataModels.CompleteChannel
import com.github.theholywaffle.teamspeak3.TS3Config
import com.github.theholywaffle.teamspeak3.TS3Query
import com.github.theholywaffle.teamspeak3.TS3Api
import com.github.theholywaffle.teamspeak3.api.wrapper.Client
import java.lang.Exception
import java.util.*


class TS3Connection(
    callbackInterface: ConnectionCallbackInterface,
    host: String,
    username: String,
    password: String
) {
    private val mCallbackInterface = callbackInterface
    private val mHost = host
    private val mUsername = username
    private val mPassword = password
    private var mApi: TS3Api? = null

    init {
        login()
    }

    fun login() {
        Thread(Runnable {
            val config = TS3Config()
            config.setHost(mHost)
            val query = TS3Query(config)
            query.connect()

            try {
                mApi = query.api
                mApi?.login(mUsername, mPassword)
                mApi?.selectVirtualServerById(1)
                mCallbackInterface.onConnection(ConnectionInfo(mHost, mUsername, true))
                getClients()
            } catch (e: Exception) {
                Log.d("TS3Connection", "Error", e)
                mCallbackInterface.onConnection(ConnectionInfo(mHost, mUsername, false))
            }
        }).start()
    }

    fun getClients() {
        Thread(Runnable {
            val list = mApi?.clients
            if (list != null) {
                mCallbackInterface.onGetClients(list)
            } else {
                mCallbackInterface.onGetClients(LinkedList())
            }
        }).start()
    }

    fun getCompleteChannels() {
        Thread(Runnable {
            val clientList = mApi?.clients
            val channelList = mApi?.channels
            val completeChannelList = LinkedList<CompleteChannel>()
            if (channelList != null && clientList != null){
                for (channel in channelList){
                    completeChannelList.add(CompleteChannel(channel, LinkedList()))
                }
                for(client in clientList){
                    for(completeChannel in completeChannelList){
                        if(completeChannel.mChannel.id == client.channelId){
                            completeChannel.mClients.add(client)
                            break
                        }
                    }
                }
                mCallbackInterface.onGetCompleteChannels(completeChannelList)
            }
        }).start()
    }

    fun getBans() {
        Thread(Runnable {
            mCallbackInterface.onGetBans(mApi?.bans)
        }).start()
    }

    fun logout() {
        Thread(Runnable {
            mApi?.logout()
            mCallbackInterface.onDisconnection(ConnectionInfo("", "", false))

        }).start()
    }
}