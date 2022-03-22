package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.Models.UserValidationCode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TestUserValidationCodeDAO {
    @Insert("INSERT INTO `user_validation_code` (user_id, validation_code) VALUES (#{userId}, #{validationCode})")
    int insertUserValidationCode(UserValidationCode userValidationCode);

    @Select("SELECT * from `user_validation_code` WHERE user_id = #{userId}")
    UserValidationCode findUserValidationCodeByUserId(@Param("userId") int userId);

    @Delete("DELETE FROM `user_validation_code` WHERE user_id = #{userId}")
    void deleteUserValidationCode(@Param("userId") int userId);
}
