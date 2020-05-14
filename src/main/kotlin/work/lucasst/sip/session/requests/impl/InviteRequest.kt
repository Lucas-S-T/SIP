package work.lucasst.sip.session.requests.impl

import work.lucasst.sip.SIPManager
import work.lucasst.sip.protocol.communication.UDP
import work.lucasst.sip.protocol.header.SIPHeader
import work.lucasst.sip.protocol.header.SIPParameter
import work.lucasst.sip.protocol.header.SIPRequestLine
import work.lucasst.sip.session.requests.SIPRequest
import java.net.DatagramPacket
import java.util.*

class InviteRequest(val manager: SIPManager, val sipAddress: String): UDP() {

    var callID = UUID.randomUUID().toString()



    override fun firstPacket(): DatagramPacket {

        val headers = mutableListOf(

                SIPHeader("Via", "SIP/2.0/UDP ${manager.address}:${manager.port};rport=0", mutableListOf(SIPParameter("branch", UUID.randomUUID().toString()))),
                SIPHeader("Max-Forwards", "100"),
                SIPHeader("To", "$sipAddress"),
                SIPHeader("From", "sip:${manager.username}@${manager.address}", mutableListOf(SIPParameter("tag", UUID.randomUUID().toString()))),
                SIPHeader("Contact", "<$sipAddress>"),
                SIPHeader("Call-ID", callID),
                SIPHeader("CSeq", "0 INVITE")

        )



        val request =  SIPRequest(SIPRequestLine("INVITE", sipAddress, "SIP/2.0"), headers)

        println(request.toString())

        val buf = request.toString().toByteArray()

        return DatagramPacket(buf, buf.size, manager.inetAddress, manager.port)


    }

    override fun onReceive(packet: DatagramPacket): Boolean {

        println(String(packet.data))

        return true
    }





}