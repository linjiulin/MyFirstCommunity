package com.lingerlin.community.community.dto;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class NotificationMongoDTO {
    private String _id;
    private String notifier;
    private String outerId;
    private Integer type;
    private Long gmtCreate;
    private Integer status;
    private String notifierName;
    private String outerTitle;
    private String typeName;
}
