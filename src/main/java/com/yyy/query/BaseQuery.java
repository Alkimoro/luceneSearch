package com.yyy.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseQuery {
    public static final Integer defaultPageSize=10;
    // 页码 1~...
    Integer pageIndex;
    // 页大小
    Integer pageSize;
    // 检查并修复不正确的字段 无法修复 则会抛出异常
    public void checkAndFix(){
        if(pageIndex==null||pageIndex<=0){
            pageIndex=1;
        }
        if(pageSize==null||pageSize<=0){
            pageSize=BaseQuery.defaultPageSize;
        }
    }
}
