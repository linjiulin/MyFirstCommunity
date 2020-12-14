package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscussionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussionMapper discussionMapper;


    public PageDTO list(Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        Integer totalcount = discussionMapper.count();
        pageDTO.setPagination(totalcount,page,size);
        if(page<1){
            page=1;
        }
        if(page>pageDTO.getTotalpage()){
            page=pageDTO.getTotalpage();
        }

        Integer offset = size *(page-1);
        List<Discussion> discussionList = discussionMapper.list(offset,size);
        List<DiscussionDTO> discussionDTOList = new ArrayList<>();
        for (Discussion discussion : discussionList) {
            User user = userMapper.findById(discussion.getCreator());
            DiscussionDTO discussionDTO = new DiscussionDTO();
            BeanUtils.copyProperties(discussion,discussionDTO);
            discussionDTO.setUser(user);
            discussionDTOList.add(discussionDTO);
        }
        pageDTO.setDiscussions(discussionDTOList);

        return pageDTO;
    }

    public PageDTO list(Integer id, Integer page, Integer size) {
        PageDTO pageDTO = new PageDTO();
        Integer totalcount = discussionMapper.count();
        pageDTO.setPagination(totalcount,page,size);
        if(page<1){
            page=1;
        }
        if(page>pageDTO.getTotalpage()){
            page=pageDTO.getTotalpage();
        }

        Integer offset = size *(page-1);
        List<Discussion> discussionList = discussionMapper.listSelf(id,offset,size);
        List<DiscussionDTO> discussionDTOList = new ArrayList<>();
        for (Discussion discussion : discussionList) {
            User user = userMapper.findById(discussion.getCreator());
            DiscussionDTO discussionDTO = new DiscussionDTO();
            BeanUtils.copyProperties(discussion,discussionDTO);
            discussionDTO.setUser(user);
            discussionDTOList.add(discussionDTO);
        }
        pageDTO.setDiscussions(discussionDTOList);
        return pageDTO;
    }

    public DiscussionDTO getById(Integer id) {
        Discussion discussion = discussionMapper.findById(id);
        User user = userMapper.findById(discussion.getCreator());
        DiscussionDTO discussionDTO = new DiscussionDTO();
        BeanUtils.copyProperties(discussion,discussionDTO);
        discussionDTO.setUser(user);
        return discussionDTO;
    }

    /*此方法用来判断用户发起的是新建讨论请求还是更改讨论请求
     */
    public void checkDiscussion(Integer id,String title,String description,String tag,User user){
        Discussion discussionOld = discussionMapper.findById(id);
        if(discussionOld!=null){
            System.out.println("存在讨论");
            discussionOld.setGmtModified(System.currentTimeMillis());
            discussionMapper.UpdateById(discussionOld.getId(),
                    title,
                    description,
                    tag,
                    discussionOld.getGmtModified());
        }
        else {
            Discussion discussion = new Discussion();
            discussion.setTitle(title);
            discussion.setDescription(description);
            discussion.setTag(tag);
            discussion.setCreator(user.getId());
            discussion.setGmtCreate(System.currentTimeMillis());
            discussion.setGmtModified(discussion.getGmtCreate());
            discussionMapper.create(discussion);
        }
        
    }
}
