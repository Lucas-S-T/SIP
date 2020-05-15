package work.lucasst.sip.session.requests.impl

import work.lucasst.sip.SIPManager
import work.lucasst.sip.protocol.communication.UDP
import work.lucasst.sip.protocol.header.SIPHeader
import work.lucasst.sip.protocol.header.SIPParameter
import work.lucasst.sip.protocol.header.SIPRequestLine
import work.lucasst.sip.protocol.sdp.SDP
import work.lucasst.sip.protocol.sdp.parseSDP
import work.lucasst.sip.session.requests.SIPRequest
import work.lucasst.sip.session.responses.parseResponse
import java.net.DatagramPacket
import java.util.*

class InviteRequest(val manager: SIPManager, val sipAddress: String ): UDP() {

    var callID = UUID.randomUUID().toString()
    var sdp: SDP? = null

    var onSDPOpen: (sdp: SDP) -> Unit = {}
    var onConnect: () -> Unit = {}



    override fun firstPacket(): DatagramPacket {

        val headers = mutableListOf(

                SIPHeader("Via", "SIP/2.0/UDP ${manager.address}:${manager.port};rport=0", mutableListOf(SIPParameter("branch", UUID.randomUUID().toString()))),
                SIPHeader("Max-Forwards", "100"),
                SIPHeader("To", "$sipAddress"),
                SIPHeader("From", "sip:${manager.username}@${manager.address}", mutableListOf(SIPParameter("tag", UUID.randomUUID().toString()))),
                SIPHeader("Contact", "<$sipAddress>"),
                SIPHeader("Call-ID", callID),
                SIPHeader("CSeq", "0 INVITE"),
                SIPHeader("Content-Type", "application/sdp")

        )


        var payload = "v=0\r\n" +
                "o=- 3798480436 3798480436 IN IP4 10.0.3.15\r\n" +
                "s=pjmedia\r\n" +
                "b=AS:84\r\n" +
                "t=0 0\r\n" +
                "a=X-nat:0\r\n" +
                "m=audio 55078 RTP/AVP 98 97 99 104 3 0 8 9 96\r\n" +
                "c=IN IP4 10.0.3.15\r\n" +
                "b=TIAS:64000\r\n" +
                "a=rtcp:51509 IN IP4 10.0.3.15\r\n" +
                "a=sendrecv\r\n" +
                "a=rtpmap:98 speex/16000\r\n" +
                "a=rtpmap:97 speex/8000\r\n" +
                "a=rtpmap:99 speex/32000\r\n" +
                "a=rtpmap:104 iLBC/8000\r\n" +
                "a=fmtp:104 mode=30\r\n" +
                "a=rtpmap:3 GSM/8000\r\n" +
                "a=rtpmap:0 PCMU/8000\r\n" +
                "a=rtpmap:8 PCMA/8000\r\n" +
                "a=rtpmap:9 G722/8000\r\n" +
                "a=rtpmap:96 telephone-event/8000\r\n" +
                "a=fmtp:96 0-16\r\n" +
                "a=ice-ufrag:0b1bd86f\r\n" +
                "a=ice-pwd:2aa73e8b\r\n" +
                "a=candidate:Ha00030f 1 UDP 2130706431 10.0.3.15 55078 typ host\r\n" +
                "a=candidate:Hc0a83867 1 UDP 2130706431 192.168.56.103 55078 typ host\r\n" +
                "a=candidate:Hc0a8c802 1 UDP 2130706431 192.168.200.2 55078 typ host\r\n" +
                "a=candidate:Ha00030f 2 UDP 2130706430 10.0.3.15 51509 typ host\r\n" +
                "a=candidate:Hc0a83867 2 UDP 2130706430 192.168.56.103 51509 typ host\r\n" +
                "a=candidate:Hc0a8c802 2 UDP 2130706430 192.168.200.2 51509 typ host\r\n"

        val request =  SIPRequest(SIPRequestLine("INVITE", sipAddress, "SIP/2.0"), headers, payload)


        val buf = request.toString().toByteArray()

        return DatagramPacket(buf, buf.size, manager.inetAddress, manager.port)


    }

    override fun onReceive(packet: DatagramPacket): Boolean {

        //TODO, eventos, when connect, when open, bye function

        var resp = parseResponse(String(packet.data))

        if(resp.status.status == 200){

           //TODO responder ACK

            onConnect()

            return false

        }

        if(resp.status.status == 180 || resp.status.status == 183){
            sdp = parseSDP(resp.payload)

            onSDPOpen(sdp!!)

        }




        return true
    }





}