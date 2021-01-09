package com.lingerlin.community.community.mapper;

import com.lingerlin.community.community.model.Comment;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lingerlin
 */
@Mapper
public interface CommentMapper {
    @Insert("insert into COMMENT (ID, PARENT_ID, TYPE, COMMENTATOR, GMT_CREATE, GMT_MODIFIED, LIKE_COUNT,CONTENT) VALUES (#{id},#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void insert(Comment comment);

    @Select("select * from COMMENT where ID = #{parentId}")
    Comment selectById(Integer parentId);

    @Select("select * from COMMENT where PARENT_ID=#{id} and TYPE=#{type}")
    List<Comment> listByParentId(@Param(value = "id") Integer id, @Param(value = "type") Integer type);

    @Update("update COMMENT set COMMENT_COUNT = COMMENT_COUNT+1 where ID=#{id}")
    void UpdateCommentCountById(@Param(value = "id") Integer parentId);
}
