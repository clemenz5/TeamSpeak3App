package com.example.teamspeak3app.ui.login

import com.example.teamspeak3app.ui.login.dataModels.CompleteChannel
import com.github.theholywaffle.teamspeak3.api.wrapper.Ban
import com.github.theholywaffle.teamspeak3.api.wrapper.Client


interface ConnectionCallbackInterface {
    fun onConnection(connectionInfo:ConnectionInfo)

    fun onDisconnection(connectionInfo:ConnectionInfo)

    fun onGetClients(clients:List<Client>)

    fun onGetBans(clients:List<Ban>?)

    fun onGetCompleteChannels(completeChannels:List<CompleteChannel>)
}