package com.lingerlin.community.community.model;


import lombok.Data;
@Data
public class Discussion {

  private Integer id;
  private String title;
  private String description;
  private Long gmtCreate;
  private Long gmtModified;
  private Integer creator;
  private Integer commentCount;
  private Integer viewCount;
  private Integer likeCount;
  private String tag;

}
