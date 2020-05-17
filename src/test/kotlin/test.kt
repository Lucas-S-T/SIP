import work.lucasst.sip.SIPManager
import work.lucasst.sip.protocol.rtp.RTPPacket
import work.lucasst.sip.rtc.RTCSession
import java.io.BufferedReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

fun main(){






    val manager = SIPManager("45.32.106.183", 2080, "1794859", "8d9239")
    val session = manager.newSession(true)
    var invite = session.createInvite("sip:005511974446962@45.32.106.183")

    val packet =  RTPPacket(payloadType = 8)

    invite.onSDPOpen = {
        println("Servidor de voz conectado")



    }
/*
    invite.onConnect = {

        println("Destinatário conectado na outra ponta (200 OK) -> Transmitindo áudio")

        var it = invite.sdp!!

        var s = DatagramSocket()

        var proc = Runtime.getRuntime().exec("ffmpeg -i /home/lucas/Desktop/discord.wav -f alaw -ar 8000 -ac 1 pipe:1")

        var out = proc.inputStream

        var rtc = RTCSession(it)

        rtc.onReceivePayload = {

            println(String(it))

        }

        rtc.start()


        while (proc.isAlive){
            println("filling")
            var b = ByteArray(160)
            out.read(b)
            rtc.sendQueue.add(b)
            Thread.sleep(20)
        }

    }
*/
    invite.execute()

        println("waiting")
        Thread.sleep(20000)

    invite.close()





}