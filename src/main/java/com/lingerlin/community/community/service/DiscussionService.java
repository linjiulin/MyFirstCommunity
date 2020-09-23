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
}
