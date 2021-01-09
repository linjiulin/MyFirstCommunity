package com.lingerlin.community.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
/**
 * @author lingerlin
 */ /*
这个类是用来储存每页查询出来的讨论信息
 */
@Data
public class PageDTO<T> {
    private List<T> data;
    private boolean hasPrevious; //是否展示前一页按钮
    private boolean hasNext;//是否展示后一页按钮
    private boolean hasFirstPage;//是否展示第一页按钮
    private boolean hasEndPage;//是否展示最后一页按钮
    private Integer totalpage;  //这个变量是总页数
    private Integer localpage; //当前页码变量
    private List<Integer> pages = new ArrayList<>(); //页面显示的页码的集合

    public void setPagination(Integer totalcount,Integer page,Integer size) {

        if(totalcount%size==0){
            totalpage = totalcount/size;
        }
        else{
           totalpage = totalcount/size+1;
        }
        if(page<1){
            page=1;
        }
        if(page>totalpage){
            page=totalpage;
        }
        this.localpage = page;
        pages.add(page);
        for(int i = 1;i<=3;i++){
            if(page-i>0){
                pages.add(0,page-i);
            }
            if(page+i<=totalpage){
                pages.add(page+i);
            }
        }
        if(page==1){//是否展示上一页
            hasPrevious=false;
        }
        else{
            hasPrevious=true;
        }
        if(page==totalpage){
            hasNext = false;
        }
        else{
            hasNext = true;
        }
        //是否展示第一页
        if(pages.contains(1)){
            hasFirstPage=false;
        }
        else{
            hasFirstPage=true;
        }

        //是否展示最后一页
        if(pages.contains(totalpage)){
            hasEndPage=false;
        }
        else{
            hasEndPage=true;
        }
    }
}
