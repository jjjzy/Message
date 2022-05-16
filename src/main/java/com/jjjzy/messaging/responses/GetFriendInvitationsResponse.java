package com.jjjzy.messaging.responses;

import com.jjjzy.messaging.enums.Status;
import com.jjjzy.messaging.models.FriendInvitation;

import java.util.List;

public class GetFriendInvitationsResponse extends BaseResponse{
    List<FriendInvitation> invitations;

    public List<FriendInvitation> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<FriendInvitation> invitations) {
        this.invitations = invitations;
    }

    public GetFriendInvitationsResponse(List<FriendInvitation> invitations) {
        super(Status.OK);
        this.invitations = invitations;
    }
}
