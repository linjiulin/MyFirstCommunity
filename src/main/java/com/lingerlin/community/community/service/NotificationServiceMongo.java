package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dao.MongoNotificationDao;
import com.lingerlin.community.community.dto.NotificationMongoDTO;
import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.enums.NotificationStatusEnum;
import com.lingerlin.community.community.enums.NotificationTypeEnum;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import com.lingerlin.community.community.model.NotificationMongo;
import com.lingerlin.community.community.model.UserMongo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lingerlin
 */
@Service
public class NotificationServiceMongo {
    @Autowired
    private MongoNotificationDao mongoNotificationDao;

    public PageDTO list(String receiver, Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        Integer totalcount = mongoNotificationDao.countByReceiver(receiver);
        System.out.println(totalcount);
        pageDTO.setPagination(totalcount,page,size);
        if(page>pageDTO.getTotalpage()){
            page=pageDTO.getTotalpage();
        }
        if(page<1){
            page=1;
        }
        Integer offset = size *(page-1);
        List<NotificationMongo> notificationMongoList = mongoNotificationDao.listSelf(receiver,offset,size);
        if (notificationMongoList.size() == 0) {
            return pageDTO;
        }
        List<NotificationMongoDTO> notificationDTOList = new ArrayList<>();
        for (NotificationMongo notificationMongo : notificationMongoList) {
            NotificationMongoDTO notificationMongoDTO = new NotificationMongoDTO();
            BeanUtils.copyProperties(notificationMongo,notificationMongoDTO);
            notificationMongoDTO.setTypeName(NotificationTypeEnum.nameOfType(notificationMongo.getType()));
            notificationDTOList.add(notificationMongoDTO);
        }
        pageDTO.setData(notificationDTOList);
        return pageDTO;
    }

    public NotificationMongoDTO read(String  id, UserMongo user) {
        NotificationMongo notification = mongoNotificationDao.selectById(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.get_id())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        mongoNotificationDao.updateStatusById(notification);
        NotificationMongoDTO notificationDTO = new NotificationMongoDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

    public Integer unreadCount(String receiver) {
        Integer unreadCount = mongoNotificationDao.unreadCountByReceiver(receiver,NotificationStatusEnum.UNREAD.getStatus());
        return unreadCount;
    }


    public void deleteByOuterId(String id) {
        mongoNotificationDao.deleteByOuterId(id);
    }

    public List<NotificationMongo> findByOuterId(String id) {
        List<NotificationMongo> notificationMongoList = mongoNotificationDao.selectByOuterId(id);
        return notificationMongoList;
    }
}
