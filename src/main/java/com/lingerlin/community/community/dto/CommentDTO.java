package com.lingerlin.community.community.dto;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class CommentDTO {
    private Integer parentId;
    private String content;
    private Integer type;
}
