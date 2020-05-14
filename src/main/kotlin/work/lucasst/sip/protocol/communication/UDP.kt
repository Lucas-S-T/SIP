package work.lucasst.sip.protocol.communication

import java.net.DatagramPacket
import java.net.DatagramSocket

abstract class UDP() {

    val s = DatagramSocket()

    fun execute(){

        val packet = firstPacket()

        s.send(packet)

        var rec = true

        while(rec) {
            val receiveData = ByteArray(4096)
            val receivePacket = DatagramPacket(receiveData, receiveData.size)
            s.receive(receivePacket)
            rec = onReceive(receivePacket)
        }

    }

    fun send(packet: DatagramPacket){

        s.send(packet)

    }

    abstract fun firstPacket(): DatagramPacket

    abstract fun onReceive(packet: DatagramPacket): Boolean

}