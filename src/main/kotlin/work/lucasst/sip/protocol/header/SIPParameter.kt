package work.lucasst.sip.protocol.header

import java.lang.StringBuilder

class SIPParameter(var key: String, var value: String, var commaSeparator: Boolean = false){

    override fun toString(): String{

        if(commaSeparator){
            return "$key=\"$value\""
        }
            return "$key=$value"

    }

}



