package com.jjjzy.messaging.dao;

import com.jjjzy.messaging.models.UserValidationCode;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Mapper
@Repository
public interface UserValidationCodeDAO {
    @Insert("INSERT INTO `user_validation_code` (user_id, validation_code, create_time) VALUES (#{userId}, #{validationCode}, #{createTime})")
    int insertUserValidationCode(UserValidationCode userValidationCode);

    @Select("SELECT * from `user_validation_code` WHERE user_id = #{userId}")
    UserValidationCode findUserValidationCodeByUserId(@Param("userId") int userId);

    @Delete("DELETE FROM `user_validation_code` WHERE user_id = #{userId}")
    void deleteUserValidationCode(@Param("userId") int userId);

    @Update("UPDATE `user_validation_code` SET validation_code = #{validationCode}, create_time = #{createTime} WHERE user_id = #{userId}")
    void updateValidationCode(@Param("validationCode") String validationCode,
                              @Param("createTime") Date createTime,
                                @Param("userId") int userId);
}
