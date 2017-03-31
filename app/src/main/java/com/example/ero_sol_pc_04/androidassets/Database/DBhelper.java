package com.example.ero_sol_pc_04.androidassets.Database;

/**
 * Created by ERO-SOL-PC-04 on 3/31/2017.
 */

import java.util.ArrayList;
import java.util.List;package com.erosol.voip.model;

import com.erosol.voip.activities.VoipApplication;
import com.erosol.voip.util.VoipConstants;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallDirection;
import com.sinch.android.rtc.messaging.Message;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static VoipCall wrapCall(Call call){
        VoipCall voipCall = new VoipCall();
        voipCall.setCallId(call.getCallId());
        voipCall.setRemoteUserId(call.getRemoteUserId());
        voipCall.setStartTime(call.getDetails().getStartedTime());
        voipCall.setEstTime(call.getDetails().getEstablishedTime());
        voipCall.setEndTime(call.getDetails().getEndedTime());
        voipCall.setCallEndCause(call.getDetails().getEndCause().getValue());
        voipCall.setIsVideoOffered(call.getDetails().isVideoOffered() ? 1 : 0);
        if (call.getDirection() == CallDirection.INCOMING){
            voipCall.setCallDirection(VoipCall.DIRECTION_INCOMING);
        } else {
            voipCall.setCallDirection(VoipCall.DIRECTION_OUTGOING);
        }
        voipCall.setCount(1); // FIXME right now hard coded to 1
        voipCall.setTimeStamp(System.currentTimeMillis());
        return voipCall;
    }

    public static VoipMessage wrapMessage(Message message, VoipMessageBody voipMessageBody, String referenceName, int direction, int status, boolean hasMedia){
        VoipMessage voipMessage = new VoipMessage();
        voipMessage.setHeaders(message.getHeaders());
        voipMessage.setMessageId(message.getMessageId());
        voipMessage.setVoipMessageBody(voipMessageBody);
        voipMessage.setHasMedia(hasMedia);
        voipMessage.setMediaReference(referenceName);
        voipMessage.setSenderId(message.getSenderId());
        voipMessage.setTimeStamp(message.getTimestamp().getTime());
        voipMessage.setDirection(direction);
        voipMessage.setStatus(status);
        voipMessage.setLocalStatus(VoipMessage.NOT_SEEN);
        voipMessage.setRecipientIdsList(message.getRecipientIds());
        return voipMessage;
    }

    public static VoipMessage wrapMessage(Message message, int status){
        VoipMessage voipMessage = new VoipMessage();
        voipMessage.setMessageId(message.getMessageId());
        voipMessage.setSenderId(message.getSenderId());
        voipMessage.setStatus(status);
        voipMessage.setTimeStamp(message.getTimestamp().getTime());
        voipMessage.setRecipientIdsList(message.getRecipientIds());
        return voipMessage;
    }

    public static VoipMessage wrapCallToMessage(Call call){
        VoipMessage voipMessage = new VoipMessage();
        VoipCall voipCall = new VoipCall();
        voipCall.setRemoteUserId(call.getRemoteUserId());
        voipMessage.setTimeStamp(voipMessage.getTimeStamp());
        voipMessage.setHasMedia(false);
        voipMessage.setSenderId(call.getRemoteUserId());
        voipMessage.setMessageType(VoipMessage.CALL);
        VoipMessageBody body = new VoipMessageBody();
        body.setTextBody(null);
        voipMessage.setVoipMessageBody(body);
        voipMessage.setLocalStatus(VoipMessage.NOT_SEEN);
        List<String> rList = new ArrayList<>();
        rList.add(VoipApplication.getInstance().getUser().getUserName());
        voipMessage.setRecipientIdsList(rList);
        return voipMessage;
    }
}
