package com.lingerlin.community.community.mapper;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.model.Discussion;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Value;

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
    int UpdateById(@Param(value = "id") Integer id,
                    @Param(value = "title") String title,
                    @Param(value = "description") String description,
                    @Param(value = "tag") String tag,
                    @Param(value = "gmtModified") Long gmtModified);

    @Update("update DISCUSSION set VIEW_COUNT=VIEW_COUNT+1 where ID=#{id}")
    void UpdateViewById(@Param(value = "id") Integer id);

    @Update("update DISCUSSION set COMMENT_COUNT = COMMENT_COUNT+1 where ID=#{id}")
    void UpdateCommentCountById(@Param(value = "id") Integer parentId);

    @Select("select * from DISCUSSION WHERE id != ${id} AND TAG REGEXP \'${tag}\'")
    List<Discussion> getRelatedDiscussionBytag(@Param(value = "id") Integer id,@Param(value = "tag") String tagRegex);

    @Select("select count(1) from discussion where CREATOR=#{id}")
    Integer countById(Integer id);

    @Delete("delete from DISCUSSION where ID = #{id}")
    void deleteById(Integer id);

    @Select("select * from DISCUSSION where TITLE REGEXP \'${tagRegex}\' order by GMT_MODIFIED DESC limit #{offset},#{size}")
    List<Discussion> listBySearch(@Param(value = "tagRegex") String tagRegex,@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(*) FROM DISCUSSION WHERE TITLE REGEXP \'${tagRegex}\'")
    Integer countBySearch(@Param(value = "tagRegex") String tagRegex);
}
