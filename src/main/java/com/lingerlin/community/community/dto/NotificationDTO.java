package com.lingerlin.community.community.dto;

import lombok.Data;

/**
 * @author lingerlin
 */
@Data
public class NotificationDTO {
    Integer id;
    Integer notifier;
    Integer outerId;
    Integer type;
    Long gmtCreate;
    Integer status;
    String notifierName;
    String outerTitle;
    String typeName;
}
