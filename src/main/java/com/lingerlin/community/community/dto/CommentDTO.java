package com.lingerlin.community.community.dto;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class CommentDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
