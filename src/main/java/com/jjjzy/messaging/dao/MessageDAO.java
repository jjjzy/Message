package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.Enums.MessageStatus;
import com.jjjzy.messaging.Models.Message;
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

    @Select("SELECT * from messages where id = #{id}")
    Message getMessageById(@Param("id") int id);

    @Select("SELECT * from messages where (from_user_id=#{fromUserId} and to_user_id=#{toUserId} and send_time <= #{endTime} and send_time >= #{startTime}) or (from_user_id=#{toUserId} and to_user_id=#{fromUserId} and send_time <= #{endTime} and send_time >= #{startTime})")
    List<Message> getUserMessages(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("SELECT * from messages where (from_user_id=#{fromUserId} and to_conversation_id=#{toConversationId} and send_time <= #{endTime} and send_time >= #{startTime})")
    List<Message> getConversationMessages(@Param("fromUserId") int fromUserId, @Param("toConversationId") int toConversationId, @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Update("UPDATE messages SET message_status = #{status} WHERE (from_user_id=#{fromUserId} and to_user_id=#{toUserId}) or (from_user_id=#{toUserId} and to_user_id=#{fromUserId})")
    void updateUserMessageStatus(@Param("status") MessageStatus status, @Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);

    @Update("UPDATE messages SET message_status = #{status} WHERE id = #{id}")
    void updateMessageStatusById(@Param("status") MessageStatus status, @Param("id") int id);

//    @Update("UPDATE messages SET message_status = #{status} WHERE (from_user_id=#{fromUserId} and to_user_id=#{toUserId}) or (from_user_id=#{toUserId} and to_user_id=#{fromUserId})")
//    void updateMessageStatusToConversation(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);
}
