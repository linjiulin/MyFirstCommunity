package com.lingerlin.community.community.mapper;

import com.lingerlin.community.community.model.Discussion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiscussionMapper {
    @Insert("insert into discussion (TITLE,DESCRIPTION,GMT_CREATE,GMT_MODIFIED,CREATOR,TAG) VALUES(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Discussion discussion);

    @Select("select * from discussion order by GMT_MODIFIED DESC limit #{offset},#{size}")
    List<Discussion> list(@Param(value = "offset") Integer offset,@Param(value = "size")  Integer size);

    @Select("select count(1) from discussion")
    Integer count();

    @Select("select * from discussion where CREATOR=#{creator} order by GMT_MODIFIED DESC limit #{offset},#{size}")
    List<Discussion> listSelf(@Param(value = "creator") Integer id,@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);
}
