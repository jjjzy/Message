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
    //TODO
    //ADD PARAM() DOESNT WORK
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

    @Select("SELECT * from messages where (from_user_id = #{userId} or to_user_id = #{userId} or to_conversation_id in (select conversation_id from conversation_users where user_id = #{userId})) and send_time >= #{lastSyncTime}")
    List<Message> getLastestMessageBySyncTime(@Param("userId") int userId, @Param("lastSyncTime") String lastSyncTime);

//    @Update("UPDATE messages SET message_status = #{status} WHERE (from_user_id=#{fromUserId} and to_user_id=#{toUserId}) or (from_user_id=#{toUserId} and to_user_id=#{fromUserId})")
//    void updateMessageStatusToConversation(@Param("fromUserId") int fromUserId, @Param("toUserId") int toUserId);
    @Select("SELECT * from messages where (from_user_id = #{userId} or to_user_id = #{userId})")
    List<Message> getAllMessage(@Param("userId") int userId);

    @Select("SELECT * from messages where (from_user_id = #{userId} or to_user_id = #{userId}) and message_status = #{status}")
    List<Message> getAllUnreadMessage(@Param("userId") int userId, @Param("status") String status);
}
