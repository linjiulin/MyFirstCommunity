package com.lingerlin.community.community.model;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class Comment {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;
}
