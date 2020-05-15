package work.lucasst.sip.rtc

import work.lucasst.sip.protocol.rtp.RTPPacket
import work.lucasst.sip.protocol.rtp.getRTCPacketPayload
import work.lucasst.sip.protocol.sdp.SDP
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class RTCSession(var sdp: SDP) {

    var onReceivePayload: (data: ByteArray) -> Unit = {}
    val sendQueue = mutableListOf<ByteArray>()

    private var s = DatagramSocket()

    private var started = true

    fun addALawSample(sample: ByteArray){
        sendQueue.add(sample)
    }

    fun start(){

        started = true

        //send thread
        Thread(){

            val packet =  RTPPacket(payloadType = 8)
            while(started){
               Thread.sleep(1)
                if(sendQueue.size >0) {

                    val pac = packet.writePayload(sendQueue[0], 160)
                    s.send(DatagramPacket(pac, pac.size, InetAddress.getByName(sdp.address), sdp.port))
                    sendQueue.removeAt(0)
                }
            }


        }.start()

        //receive thread
        Thread(){


            while(started){
                Thread.sleep(1)
                    Thread.sleep(1)
                    var b = ByteArray(172)
                    var pac = DatagramPacket(b, b.size)
                    s.receive(pac)

                    onReceivePayload(getRTCPacketPayload(pac.data))
            }


        }.start()



    }


    fun stop(){

        started = false

    }


}