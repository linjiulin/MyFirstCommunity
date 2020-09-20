package com.lingerlin.community.community.mapper;

import com.lingerlin.community.community.model.Discussion;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DiscussionMapper {
    @Insert("insert into discussion (TITLE,DESCRIPTION,GMT_CREATE,GMT_MODIFIED,CREATOR) VALUES(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator})")
    void create(Discussion discussion);

}
