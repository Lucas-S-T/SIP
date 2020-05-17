package work.lucasst.sip.session.responses.impl

import work.lucasst.sip.SIPManager
import work.lucasst.sip.protocol.header.SIPHeader
import work.lucasst.sip.protocol.header.SIPParameter
import work.lucasst.sip.protocol.header.SIPStatusLine
import work.lucasst.sip.session.responses.SIPResponse
import java.util.*


fun get200ByeResponse(manager: SIPManager, callId: String, sipAddress: String): SIPResponse{



    return SIPResponse(SIPStatusLine("SIP/2.0", 200, "OK"), mutableListOf(

            SIPHeader("Via", "SIP/2.0/UDP ${manager.address}:${manager.port};rport=0", mutableListOf(SIPParameter("branch", UUID.randomUUID().toString()))),
            SIPHeader("From", "sip:${manager.username}@${manager.address}", mutableListOf(SIPParameter("tag", UUID.randomUUID().toString()))),
            SIPHeader("CSeq", "0 BYE"),
            SIPHeader("Call-ID", callId),
            SIPHeader("To", sipAddress),
            SIPHeader("Content-Length", "0")

    ),  "")

}
