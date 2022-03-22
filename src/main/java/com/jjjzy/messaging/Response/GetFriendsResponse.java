package com.jjjzy.messaging.Response;

import com.jjjzy.messaging.Enums.Status;
import com.jjjzy.messaging.Models.FriendInvitation;
import com.jjjzy.messaging.Models.User;

import java.util.List;

public class GetFriendsResponse extends BaseResponse{

    List<User> friends;
    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }



    public GetFriendsResponse(List<User> friends) {
        super(Status.OK);
        this.friends = friends;
    }
}
