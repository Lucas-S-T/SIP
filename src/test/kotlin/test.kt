import work.lucasst.sip.SIPManager
import work.lucasst.sip.protocol.rtp.RTPPacket
import java.io.BufferedReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import javax.xml.bind.DatatypeConverter
import kotlin.concurrent.thread
import kotlin.random.Random

fun main(){






    val manager = SIPManager("45.32.106.183", 2080, "1794859", "8d9239")
    val session = manager.newSession(true)
    var invite = session.createInvite("sip:005511999260522@45.32.106.183")

    val packet =  RTPPacket(payloadType = 8)

    invite.onSDPOpen = {
        var s = DatagramSocket()

        println("Sending")
        var proc = Runtime.getRuntime().exec("ffmpeg -i /home/lucas/Desktop/discord.wav -f alaw -ar 8000 pipe:1")

        var out = proc.inputStream

        while (true){
            var b = ByteArray(160)
            out.read(b)

            var pac = packet.getToSend(b, 160)
            s.send(DatagramPacket(pac, pac.size, InetAddress.getByName(it.address), it.port))
            Thread.sleep(20)


        }

    }

    invite.onConnect = {

        println("Resposta do destinatário")

    }

    invite.execute()






}