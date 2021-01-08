package com.lingerlin.community.community.enums;

/**
 * @author lingerlin
 */

public enum CommentTypeEnum {
    DISCUSSION(1),
    COMMENT(2);
    private Integer type;

    public static boolean isExist(Integer type) {
        for(CommentTypeEnum commentTypeEnum:CommentTypeEnum.values()){
            if(commentTypeEnum.getType()==type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    CommentTypeEnum(Integer type) {
        this.type = type;
    }
}
