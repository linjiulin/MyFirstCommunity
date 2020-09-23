package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
public class DiscussionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussionMapper discussionMapper;

    public List<DiscussionDTO> list() {
        List<Discussion> discussionList = discussionMapper.list();
        List<DiscussionDTO> discussionDTOList = new ArrayList<>();
        for (Discussion discussion : discussionList) {
            User user = userMapper.findById(discussion.getCreator());
            DiscussionDTO discussionDTO = new DiscussionDTO();
            BeanUtils.copyProperties(discussion,discussionDTO);
            discussionDTO.setUser(user);
            discussionDTOList.add(discussionDTO);
        }
        return discussionDTOList;
    }
}
