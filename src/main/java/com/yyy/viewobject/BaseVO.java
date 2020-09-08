package com.yyy.viewobject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseVO {

    // 数据条数
    Integer totalCount;
    //总页数
    Integer pageCount;
    // 页面大小
    Integer pageSize;
    // 页索引
    Integer pageIndex;

}
