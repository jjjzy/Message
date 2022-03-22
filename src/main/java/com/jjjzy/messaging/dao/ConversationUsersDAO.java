package com.jjjzy.messaging.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ConversationUsersDAO {

    @Insert("INSERT INTO `conversation_users` (conversation_id, user_id) VALUES (#{conversation_id}, #{user_id})")
    void createConversationUser(@Param("conversation_id") int conversation_id, @Param("user_id") int user_id);

    @Insert("INSERT INTO `conversation_users` (conversation_id, user_id) VALUES (#{conversationId}, #{id})")
    void inviteUserToConversation(@Param("conversationId") int conversationId, @Param("id")int id);

    @Select("SELECT conversation_id from conversation_users where user_id = #{id}")
    List<Integer> getConversationIdById(@Param("id") int id);

    @Delete("DELETE FROM `conversation_users` WHERE user_id = #{userId}")
    void removeUserFromConversation(@Param("userId") int userId);

    @Delete("DELETE FROM `conversation_users` WHERE conversation_id = #{id}")
    void removeConversation(@Param("id") int id);
}
