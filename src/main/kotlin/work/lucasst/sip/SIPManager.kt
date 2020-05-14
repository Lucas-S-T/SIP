package work.lucasst.sip

import work.lucasst.sip.session.SIPSession
import work.lucasst.sip.session.requests.impl.RegisterPacket
import java.net.InetAddress


class SIPManager(val address: String, val port: Int, val username: String, val password: String) {

    var inetAddress = InetAddress.getByName(address)
    var isAuth = false

    fun newSession(register: Boolean): SIPSession{

        if(register){

            val p = RegisterPacket(this)
            p.execute()

        }

        return SIPSession(register, this)

    }



}