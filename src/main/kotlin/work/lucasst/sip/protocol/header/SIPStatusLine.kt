package work.lucasst.sip.protocol.header

class SIPStatusLine(var version: String,var  status: Int,var reason: String) {

    override fun toString(): String {

        return "$version $status $reason"

    }

}