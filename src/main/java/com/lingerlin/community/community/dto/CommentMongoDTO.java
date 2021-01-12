package com.lingerlin.community.community.dto;

import com.lingerlin.community.community.model.UserMongo;
import lombok.Data;

@Data
public class CommentMongoDTO {
    private String _id;
    private Integer parentId;
    private Integer type;
    private String commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;
    private UserMongo user;
}
