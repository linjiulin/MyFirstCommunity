package com.lingerlin.community.community.mapper;

import com.lingerlin.community.community.model.Notification;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author lingerlin
 */
@Mapper
public interface NotificationMapper {

    @Insert("insert into NOTIFICATION (notifier, receiver, outer_id, type, gmt_create, status, notifier_name, outer_title) values (#{notifier},#{receiver},#{outerId},#{type},#{gmtCreate},#{status},#{notifierName},#{outerTitle})")
    void insert(Notification notification) ;

    @Select("select count(1) from NOTIFICATION where RECEIVER = #{receiver}")
    Integer countByReceiver(Integer receiver);

    @Select("select * from NOTIFICATION where RECEIVER=#{receiver} order by GMT_CREATE DESC limit #{offset},#{size}")
    List<Notification> listSelf(@Param(value = "receiver") Integer receiver, @Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select * from NOTIFICATION where ID=#{id}")
    Notification selectById(Integer id);

    @Update("update NOTIFICATION set STATUS = #{status} where ID=#{id}")
    void updateStatusById(Notification notification);

    @Select("select count(1) from NOTIFICATION where STATUS = ${status} AND RECEIVER = #{receiver}")
    Integer unreadCountByReceiver(@Param(value = "receiver") Integer receiver, @Param(value = "status") int status);

    @Select("select * from NOTIFICATION WHERE OUTER_ID=#{outerId}")
    List<Notification> selectByOuterId(Integer outerId);

    @Delete("delete from NOTIFICATION WHERE OUTER_ID=#{outerId}")
    void deleteByOuterId(Integer outerId);
}
