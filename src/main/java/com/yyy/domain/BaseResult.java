package com.yyy.domain;

import com.yyy.viewobject.BaseVO;
import lombok.Data;

@Data
public class BaseResult<T extends BaseVO> {
    Integer code;
    String msg;
    T data;
    String redirectURL;
}
