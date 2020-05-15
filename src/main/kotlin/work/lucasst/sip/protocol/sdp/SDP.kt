package work.lucasst.sip.protocol.sdp

class SDP(var address: String, var port: Int, var codecs: MutableList<SDPCodec>)



fun parseSDP(data: String): SDP{


    var codecs = mutableListOf<SDPCodec>()
    var address = ""
    var port = 0

    var lines = data.split("\r\n")

    for(l in lines){

        var kv = l.split("=")

        if(kv[0] == "o"){
            address = kv[1].split("IP4")[1].replaceFirst(" ", "")
        }

        if(kv[0]=="m"){
            if(kv[1].startsWith("audio")){

                port = kv[1].split(" ")[1].toInt()

            }
        }

        if(kv[0] == "a"){

            if(kv[1].startsWith("rtpmap")){

                var v = kv[1].split(":")[1].split(" ")
                var name = v[1].split("/")

                val c = SDPCodec(v[0].toInt(), name[0], name[1].toInt())
                codecs.add(c)


            }

        }


    }



return SDP(address, port, codecs)
}