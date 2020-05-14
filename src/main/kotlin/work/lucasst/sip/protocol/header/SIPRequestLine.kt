package work.lucasst.sip.protocol.header

class SIPRequestLine(var method: String, var uri: String, var version: String) {

    override fun toString(): String{

        return "$method $uri $version"

    }

}