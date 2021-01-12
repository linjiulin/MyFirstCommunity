package com.lingerlin.community.community.dto;

import com.lingerlin.community.community.model.UserMongo;
import lombok.Data;

@Data
public class DiscussionMongoDTO {
    private String _id;
    private String title;
    private String description;
    private Long gmtCreate;
    private Long gmtModified;
    private String creator;
    private Integer commentCount;
    private Integer viewCount;
    private Integer likeCount;
    private String tag;
    private UserMongo user;
}
