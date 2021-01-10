package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.dto.NotificationDTO;
import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.enums.NotificationStatusEnum;
import com.lingerlin.community.community.enums.NotificationTypeEnum;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import com.lingerlin.community.community.mapper.NotificationMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.Notification;
import com.lingerlin.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lingerlin
 */
@Service
public class NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;

    public PageDTO list(Integer receiver, Integer page, Integer size) {
        PageDTO<NotificationDTO> pageDTO = new PageDTO<>();
        Integer totalcount = notificationMapper.countByReceiver(receiver);
        System.out.println(totalcount);
        pageDTO.setPagination(totalcount,page,size);
        if(page<1){
            page=1;
        }
        if(page>pageDTO.getTotalpage()){
            page=pageDTO.getTotalpage();
        }

        Integer offset = size *(page-1);
        List<Notification> notificationList = notificationMapper.listSelf(receiver,offset,size);
        if (notificationList.size() == 0) {
            return pageDTO;
        }
        List<NotificationDTO> notificationDTOList = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            notificationDTOList.add(notificationDTO);
        }
        pageDTO.setData(notificationDTOList);
        return pageDTO;
    }

    public NotificationDTO read(Integer id, User user) {
        Notification notification = notificationMapper.selectById(id);
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if (!Objects.equals(notification.getReceiver(), user.getId())) {
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateStatusById(notification);
        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
        return notificationDTO;
    }

    public Integer unreadCount(Integer receiver) {
        Integer unreadCount = notificationMapper.unreadCountByReceiver(receiver,NotificationStatusEnum.UNREAD.getStatus());
        return unreadCount;
    }


    public void deleteByOuterId(Integer id) {
        notificationMapper.deleteByOuterId(id);
    }

    public List<Notification> findByOuterId(Integer id) {
        List<Notification> notificationList = notificationMapper.selectByOuterId(id);
        return notificationList;
    }
}
