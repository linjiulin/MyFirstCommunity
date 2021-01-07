package com.lingerlin.community.community.model;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class Comment {
    private Long id;
    private Long parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
}
