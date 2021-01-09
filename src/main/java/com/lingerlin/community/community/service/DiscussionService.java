package com.lingerlin.community.community.service;

import com.lingerlin.community.community.dto.DiscussionDTO;
import com.lingerlin.community.community.dto.PageDTO;
import com.lingerlin.community.community.exception.CustomizeErrorCode;
import com.lingerlin.community.community.exception.CustomizeException;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingerlin
 */
@Service
public class DiscussionService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussionMapper discussionMapper;


    /**
     * @param page
     * @param size
     * @return
     */
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

    /**
     * @param id
     * @param page
     * @param size
     * @return
     */
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

    /**
     * @param id 传输的是讨论的id
     * @return 返回的是一个关联了用户的讨论集合
     */
    public DiscussionDTO getById(Integer id) {
        Discussion discussion = discussionMapper.findById(id);
        if(discussion==null){
            throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
        }
        User user = userMapper.findById(discussion.getCreator());
        DiscussionDTO discussionDTO = new DiscussionDTO();
        BeanUtils.copyProperties(discussion,discussionDTO);
        discussionDTO.setUser(user);
        return discussionDTO;
    }

    /**
     * @param id 传输的讨论id
     * @param title 讨论标题
     * @param description 讨论详细
     * @param tag 讨论标签
     * @param user 传输来的用户
     */
    public void checkDiscussion(Integer id,String title,String description,String tag,User user){
        Discussion discussionOld = discussionMapper.findById(id);
        if(discussionOld!=null){
            System.out.println("存在讨论");
            discussionOld.setGmtModified(System.currentTimeMillis());
            int updated = discussionMapper.UpdateById(discussionOld.getId(),
                    title,
                    description,
                    tag,
                    discussionOld.getGmtModified());
            if(updated!=1){
                throw new CustomizeException(CustomizeErrorCode.DISCUSSION_NOT_FOUND);
            }
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

    public void incView(Integer id) {
        Discussion discussion = discussionMapper.findById(id);
        System.out.println("阅读数是："+discussion.getViewCount());
        discussionMapper.UpdateViewById(id);
    }

    public List<Discussion> getRelatedDiscussionBytag(DiscussionDTO discussionDTO) {
        if(StringUtils.isBlank(discussionDTO.getTag())){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(discussionDTO.getTag(),',');
        String tagRegex = Arrays.stream(tags).collect(Collectors.joining("|"));

        System.out.println(tagRegex);
        List<Discussion> discussionList = discussionMapper.getRelatedDiscussionBytag(discussionDTO.getId(),tagRegex);
        return discussionList;
    }
}
