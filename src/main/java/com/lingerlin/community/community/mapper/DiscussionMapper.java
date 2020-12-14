package com.lingerlin.community.community.mapper;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.model.Discussion;
import org.apache.ibatis.annotations.*;

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

    @Select("select * from discussion where id = #{id}")
    Discussion findById(@Param(value = "id") Integer id);

    @Update("update discussion set TITLE = #{title},DESCRIPTION = #{description},TAG=#{tag},GMT_MODIFIED=#{gmtModified} where ID = #{id}")
    void UpdateById(@Param(value = "id") Integer id,
                    @Param(value = "title") String title,
                    @Param(value = "description") String description,
                    @Param(value = "tag") String tag,
                    @Param(value = "gmtModified") Long gmtModified);

}
