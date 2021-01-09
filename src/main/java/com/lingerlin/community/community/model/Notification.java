package com.lingerlin.community.community.model;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class Notification {
    Integer id;
    Integer notifier;
    Integer receiver;
    Integer outerId;
    Integer type;
    Long gmtCreate;
    Integer status;
    String notifierName;
    String outerTitle;
}
