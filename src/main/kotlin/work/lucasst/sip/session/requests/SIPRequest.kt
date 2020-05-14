package work.lucasst.sip.session.requests

import work.lucasst.sip.protocol.header.SIPHeader
import work.lucasst.sip.protocol.header.SIPRequestLine
import java.lang.StringBuilder


class SIPRequest(var method: SIPRequestLine, var headers: MutableList<SIPHeader>) {

    override fun toString(): String{

        val sb = StringBuilder()

            sb.append("$method\r\n")

            for(h in headers){
                sb.append("$h\r\n")
            }

        sb.append("\r\n")

        return sb.toString()

    }

}


fun parseRequest(): SIPRequest{
    TODO()
}