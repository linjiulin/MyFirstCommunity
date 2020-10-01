package com.lingerlin.community.community.model;

import com.sun.jdi.LongValue;
import lombok.Data;

/*
这个类是IDEA为H2数据库表自动生成的表属性封装类
 */
@Data
public class User {

  private Integer id;
  private String accountId;
  private String name;
  private String token;
  private String avatar;
  private String bio;
  private Long gmtCreate;
  private Long gmtModified;

}
