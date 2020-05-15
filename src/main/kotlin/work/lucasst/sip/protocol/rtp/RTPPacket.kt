package work.lucasst.sip.protocol.rtp

import java.nio.ByteBuffer
import kotlin.random.Random

class RTPPacket (var version: Int = 2, var padding: Int = 0, var extension: Int = 0, var csic: Int = 0, var  marker: Int = 0, var payloadType: Int){


    val sync = Random.nextBits(32)

    var seq: Short = Random.nextInt(500).toShort()
    var timestamp = Random.nextInt()


    fun getToSend(payload: ByteArray, timestamp: Int): ByteArray{

        var buffer: ByteBuffer = ByteBuffer.allocate(2)
        buffer.putShort(seq)

        var header = byteArrayOf(firstByte(), secondByte())
        header = header.plus(buffer.array())

        buffer = ByteBuffer.allocate(4)
        buffer.putInt(this.timestamp)

        header = header.plus(buffer.array())

        buffer = ByteBuffer.allocate(4)
        buffer.putInt(sync)

        header = header.plus(buffer.array())
        header = header.plus(payload)

        this.timestamp+= timestamp
        seq++

        return header
    }


    fun firstByte(): Byte{

        var byte = 0
        byte = byte or version
        byte = byte.shl(1)
        byte = byte or padding
        byte = byte.shl(1)
        byte = byte or extension
        byte = byte shl 4
        byte = byte or csic

        return byte.toByte()

    }

    fun secondByte(): Byte{

        var byte = 0
        byte = byte or marker
        byte = byte shl 7
        byte = byte or payloadType

        return byte.toByte()

    }





}