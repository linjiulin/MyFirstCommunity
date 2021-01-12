package com.lingerlin.community.community.model;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class NotificationMongo {
    private String _id;
    private String notifier;
    private String receiver;
    private String outerId;
    private Integer type;
    private Long gmtCreate;
    private Integer status;
    private String notifierName;
    private String outerTitle;
}
