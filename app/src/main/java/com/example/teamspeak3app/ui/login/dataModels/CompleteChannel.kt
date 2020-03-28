package com.example.teamspeak3app.ui.login.dataModels

import com.github.theholywaffle.teamspeak3.api.wrapper.Channel
import com.github.theholywaffle.teamspeak3.api.wrapper.Client
import java.util.*

/**
 * A Channel is a model with the channel itself and all of its clients
 */
class CompleteChannel (channel:Channel, clients:LinkedList<Client>){
    var mChannel = channel
    var mClients = clients
}