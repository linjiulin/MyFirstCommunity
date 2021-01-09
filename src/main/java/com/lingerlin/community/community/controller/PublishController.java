package com.lingerlin.community.community.controller;

import com.lingerlin.community.community.cache.TagCache;
import com.lingerlin.community.community.mapper.DiscussionMapper;
import com.lingerlin.community.community.mapper.UserMapper;
import com.lingerlin.community.community.model.Discussion;
import com.lingerlin.community.community.model.User;
import com.lingerlin.community.community.service.DiscussionService;
import org.apache.commons.lang3.StringUtils;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private DiscussionMapper discussionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussionService discussionService;

    @GetMapping("/publish/{id}")
    public  String edit(@PathVariable(name = "id") Integer id,
                        Model model){
        Discussion discussion = discussionMapper.findById(id);
        System.out.println("标题是："+discussion.getTitle());
        model.addAttribute("title",discussion.getTitle());
        model.addAttribute("description",discussion.getDescription());
        model.addAttribute("tag",discussion.getTag());
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "id",required = false) Integer id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            HttpServletRequest request,
            Model model) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tag", tag);
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "讨论内容不能为空");
            return "publish";
        }
        if (tag == null || tag == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入了非法标签:" + invalid);
            return "publish";
        }
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            model.addAttribute("error", "用户未登录");
            return "publish";
        }
        discussionService.checkDiscussion(id,title,description,tag,user);
        return "redirect:/";
    }
}
