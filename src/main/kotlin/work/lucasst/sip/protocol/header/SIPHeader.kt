package work.lucasst.sip.protocol.header

import java.lang.StringBuilder

class SIPHeader(var key: String, var value: String, var parameters: MutableList<SIPParameter> = mutableListOf(), val commaSeparator: Boolean = false){

    override fun toString(): String{

        val sb = StringBuilder()
        sb.append("$key: $value")

        for(p in parameters){
            if(commaSeparator){
                sb.append(", $p")
            }else {
                sb.append(";$p")
            }
        }

        return sb.toString()

    }

}