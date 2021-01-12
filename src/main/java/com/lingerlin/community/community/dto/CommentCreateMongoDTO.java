package com.lingerlin.community.community.dto;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class CommentCreateMongoDTO {
    private String parentId;
    private String content;
    private Integer type;
}
