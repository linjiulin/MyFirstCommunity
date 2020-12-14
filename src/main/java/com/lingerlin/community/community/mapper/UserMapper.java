package com.lingerlin.community.community.mapper;

import com.lingerlin.community.community.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatar})")
    void insert(User user);

    @Select("select * from user where token = #{token}")
    User findByToken(@Param("token") String token);

    @Select("select * from user where account_id = #{accountId} and name = #{name}")
    User findByAccountId(@Param("accountId") String accountId, @Param("name") String name);

    @Select("select * from user where id = #{creator}")
    User findById(@Param("creator") Integer creator);

    @Update("update user set token = #{token} ,gmt_modified = #{gmt_modified},avatar = #{avatar},bio = #{bio} where account_id = #{account_id} ")
    void updateByAccountId(@Param("token") String token,@Param("gmt_modified") long currentTimeMillis,@Param("avatar") String avatar_url,@Param("bio") String bio,@Param("account_id") String account_id);
}
