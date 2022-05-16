package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.models.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Mapper
public interface TestUserDAO {
    @Delete("truncate table users;")
    void deleteAllUsers();

    @Delete("truncate table user_validation_code;")
    void deleteAllUserValidationCode();

    @Insert(value = "INSERT INTO `users` (username, password, email, nickname, gender, address, register_time, login_token, last_login_time)" +
            " VALUES (#{username}, #{password}, #{email}, #{nickname}, #{gender}, #{address}, #{RegistrationTime}, #{loginToken}, #{lastLoginTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertUser(User user);


    @Select("SELECT * FROM `users` WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM `users` WHERE id = #{id}")
    User findUserByUserId(@Param("id") int id);

    @Update("UPDATE `users` SET is_valid = 1 WHERE id = #{userId}")
    void setUserValid(@Param("userId") int userId);

    @Update("UPDATE `users` SET login_token = #{loginToken} WHERE username = #{username}")
    void setUserLoginToken(@Param("loginToken") String loginToken, @Param("username") String username);

    @Update("UPDATE `users` SET last_login_time = #{lastLoginTime} WHERE username = #{username}")
    void setUserLastLoginTime(@Param("lastLoginTime") Date lastLoginTime, @Param("username") String username);

    @Select("SELECT * FROM `users` WHERE login_token = #{loginToken}")
    User findUserByLoginToken(@Param("loginToken") String loginToken);
}
