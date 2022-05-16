package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.models.Conversation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TestConversationDAO {

    @Delete("truncate table conversations;")
    void deleteAllConversations();

    @Insert(value = "INSERT INTO `conversations` (title, notice, create_time) VALUES (#{title}, #{notice}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int createConversation(Conversation conversation);

    @Update("UPDATE `conversations` SET title = #{title}, notice = #{notice} WHERE id = #{conversationId}")
    void updateConversation(@Param("conversationId") int conversationId, @Param("title") String title, @Param("notice") String notice);

    @Select("SELECT * from conversations where id = #{id}")
    Conversation getConversationById(@Param("id") int id);

    @Delete("DELETE FROM `conversations` WHERE id = #{id}")
    void removeConversation(@Param("id") int id);
}
