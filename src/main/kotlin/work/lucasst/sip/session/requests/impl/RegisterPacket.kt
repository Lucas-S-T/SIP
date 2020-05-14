package work.lucasst.sip.session.requests.impl

import work.lucasst.sip.SIPManager
import work.lucasst.sip.protocol.communication.UDP
import work.lucasst.sip.protocol.header.SIPHeader
import work.lucasst.sip.protocol.header.SIPParameter
import work.lucasst.sip.protocol.header.SIPRequestLine
import work.lucasst.sip.session.requests.SIPRequest
import work.lucasst.sip.session.responses.parseResponse
import java.math.BigInteger
import java.net.DatagramPacket
import java.security.MessageDigest
import java.util.*

class RegisterPacket(val manager: SIPManager): UDP() {

    var callID = UUID.randomUUID().toString()

    override fun firstPacket(): DatagramPacket {

        val headers = mutableListOf(

                SIPHeader("Via", "SIP/2.0/UDP ${manager.address}:${manager.port};rport=0", mutableListOf(SIPParameter("branch", UUID.randomUUID().toString()))),
                SIPHeader("Max-Forwards", "100"),
                SIPHeader("From", "sip:${manager.username}@${manager.address}", mutableListOf(SIPParameter("tag", UUID.randomUUID().toString()))),
                SIPHeader("To", "<sip:${manager.username}@${manager.address}>"),
                SIPHeader("CSeq", "0 REGISTER"),
                SIPHeader("Call-ID", callID)


        )

        val request =  SIPRequest(SIPRequestLine("REGISTER", "sip:${manager.address}:${manager.port}", "SIP/2.0"), headers)

        println(request.toString())

        val buf = request.toString().toByteArray()

        return DatagramPacket(buf, buf.size, manager.inetAddress, manager.port)


    }

    override fun onReceive(packet: DatagramPacket): Boolean {

        var resp = parseResponse(String(packet.data))

        println(resp.toString())


        if(resp.status.status == 401){

            var wwwLine = ""

            for(h in resp.headers){

                if(h.key == "WWW-Authenticate"){
                    wwwLine = h.value
                    break
                }

            }

            var auth = wwwLine.split(" ")

            if(auth[0] == "Digest"){

                var seq = auth[1].split(",")

                var realm = ""
                var nonce = ""

                for(v in seq){

                    var kv = v.split("=")

                    if(kv[0] == "realm"){
                        realm = kv[1].replace("\"", "")
                    }
                    if(kv[0] == "nonce"){
                        nonce = kv[1].replace("\"", "")
                    }

                }





               // HA1 = MD5(username:realm:password)
              //  HA2 = MD5(method:digestURI)
              //  response = MD5(HA1:nonce:HA2)

                var ha1 = "${manager.username}:$realm:${manager.password}".md5()
                var ha2 = "REGISTER:sip:${manager.address}:${manager.port}".md5()
                var response = "$ha1:$nonce:$ha2".md5()


            var authh = "Digest username=\"${manager.username}\", realm=\"$realm\", nonce=\"$nonce\", uri=\"sip:${manager.address}:${manager.port}\", response=\"$response\""


                val headers = mutableListOf(
                        SIPHeader("Via", "SIP/2.0/UDP ${manager.address}:${manager.port};rport=0", mutableListOf(SIPParameter("branch", UUID.randomUUID().toString()))),
                        SIPHeader("Max-Forwards", "100"),
                        SIPHeader("From", "sip:${manager.username}@${manager.address}", mutableListOf(SIPParameter("tag", UUID.randomUUID().toString()))),
                        SIPHeader("To", "<sip:${manager.username}@${manager.address}>"),
                        SIPHeader("CSeq", "0 REGISTER"),
                        SIPHeader("Call-ID", callID),
                        SIPHeader("Authorization", authh)
                )
                val request =  SIPRequest(SIPRequestLine("REGISTER", "sip:${manager.address}:${manager.port}", "SIP/2.0"), headers)

                println(request.toString())
                val buf = request.toString().toByteArray()
                s.send(DatagramPacket(buf, buf.size, manager.inetAddress, manager.port))



            }


            return true
        }



        manager.isAuth = true
        return false


    }

    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}
