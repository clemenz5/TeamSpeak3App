package com.example.teamspeak3app.ui.login

class Member<R, S> (r:R, s:S){
    var entry1 = r
    var entry2 = s

    override fun toString(): String {
        return entry1.toString() + ", " + entry2.toString()
    }
}