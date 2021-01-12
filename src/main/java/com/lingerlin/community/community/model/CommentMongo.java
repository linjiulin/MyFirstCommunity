package com.lingerlin.community.community.model;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class CommentMongo {
    private String _id;
    private String parentId;
    private Integer type;
    private String commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;

}
