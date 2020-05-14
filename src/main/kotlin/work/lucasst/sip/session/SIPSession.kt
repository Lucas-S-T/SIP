package work.lucasst.sip.session

import work.lucasst.sip.SIPManager
import work.lucasst.sip.session.requests.impl.InviteRequest

class SIPSession {

    var register = false
    var manager: SIPManager? = null

    constructor(register: Boolean, manager: SIPManager){

        this.manager = manager
        this.register = register

        //TODO REGISTER

    }


    fun createInvite(sipAddress: String): InviteRequest{

        return InviteRequest(manager!!, sipAddress)

    }


}