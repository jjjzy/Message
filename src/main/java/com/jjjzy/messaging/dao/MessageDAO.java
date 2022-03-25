package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.Models.FriendInvitation;
import com.jjjzy.messaging.Models.Message;
import com.jjjzy.messaging.Models.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MessageDAO {
    @Insert(value = "INSERT INTO `messages` (from_user_id, to_user_id, to_conversation_id, message_type, content, send_time, message_status)" +
            " VALUES (#{fromUserId}, #{toUserId}, #{toConversationId}, #{messageType}, #{content}, #{sendTime}, #{messageStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertMessage(Message message);



    @Select("SELECT * from messages where (from_user_id=#{fromUserId} and to_user_id=#{toUserId}) or (from_user_id=#{toUserId} and to_user_id=#{fromUserId})")
    List<Message> getToUserMessage(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);

    @Select("SELECT * from messages where (from_user_id=#{fromUserId} and to_conversation_id=#{toConversationId})")
    List<Message> getToConversationMessage(@Param("fromUserId") int fromUserId, @Param("toConversationId") int toConversationId);

}
