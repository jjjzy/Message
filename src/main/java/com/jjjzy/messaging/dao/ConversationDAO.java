package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.models.Conversation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface ConversationDAO {
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

//
//    CREATE TABLE `conversations` (
//        `id` int(11) NOT NULL AUTO_INCREMENT,
//        `title` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
//        `notice` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
//        `create_time` datetime DEFAULT NULL,
//        PRIMARY KEY (`id`)
//        ) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4


//    CREATE TABLE `messages` (
//        `id` int NOT NULL AUTO_INCREMENT,
//        `from_user_id` int NOT NULL,
//        `to_user_id` int DEFAULT NULL,
//        `to_conversation_id` int DEFAULT NULL,
//        `message_type` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
//        `content` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
//        `send_time` datetime DEFAULT NULL,
//        `message_status` varchar(128) CHARACTER SET utf8 DEFAULT NULL,
//        PRIMARY KEY (`id`)
//        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
