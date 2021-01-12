package com.lingerlin.community.community.model;

import lombok.Data;

@Data
public class DiscussionMongo {
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
}
