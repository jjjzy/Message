package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.Enums.FriendInvitationStatus;
import com.jjjzy.messaging.Models.FriendInvitation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FriendDAO {
    @Select("SELECT * from friend_invitation where (from_user_id=#{userId} or to_user_id=#{userId}) and status=#{status}")
    List<FriendInvitation> getInvitationByIdAndStatus(@Param("userId") int userId,
                                                      @Param("status") FriendInvitationStatus status);

    @Insert("INSERT INTO `friend_invitation` (from_user_id, to_user_id, send_time, message, status) VALUES (#{fromUserId}, #{toUserId}, #{sendTime}, #{message}, #{status})")
    int insertInvitation(FriendInvitation friendInvitation);

    @Update("UPDATE `friend_invitation` SET status = #{status} WHERE id = #{invitationId}")
    void updateInvitationStatus(@Param("invitationId") int invitationId,
                                @Param("status") FriendInvitationStatus status);

    @Select("SELECT * from friend_invitation where id = #{invitationId}")
    FriendInvitation getInvitationById(@Param("invitationId") int invitationId);
}
