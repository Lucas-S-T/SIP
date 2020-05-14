package work.lucasst.sip.session.responses

import work.lucasst.sip.protocol.header.SIPHeader
import work.lucasst.sip.protocol.header.SIPStatusLine
import java.lang.StringBuilder


class SIPResponse(var status: SIPStatusLine, var headers: MutableList<SIPHeader>) {

    override fun toString(): String {

        val sb = StringBuilder()

        sb.append("$status\r\n")

        for (h in headers) {
            sb.append("$h\r\n")
        }

        sb.append("\r\n")

        return sb.toString()

    }


}

fun parseResponse(data: String): SIPResponse {

    var lines = data.split("\r\n")


    var statusLine = lines[0].split(" ")
    var version = statusLine[0]
    var status = statusLine[1].toInt()

    statusLine = statusLine.drop(2)

    var reason = statusLine.joinToString(separator = " ")

    var sl = SIPStatusLine(version, status, reason)

    var headers = mutableListOf<SIPHeader>()

    lines = lines.drop(1)

    for(l in lines){

        var kv = l.split(":")

        if(kv.size <2){
            break
        }

        var key = kv[0]
        var v = kv[1].trimStart(' ')

        headers.add(SIPHeader(key, v))

    }

    var s = SIPResponse(sl, headers)



    return s

}