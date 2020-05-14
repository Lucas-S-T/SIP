import work.lucasst.sip.SIPManager

fun main(){



    val manager = SIPManager("45.32.106.183", 2080, "1794859", "8d9239")
    val session = manager.newSession(true)
    var invite = session.createInvite("sip:005511999260522@45.32.106.183")
    invite.execute()




}