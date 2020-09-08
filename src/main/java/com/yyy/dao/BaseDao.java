package com.yyy.dao;

import com.yyy.query.BaseQuery;
import com.yyy.viewobject.BaseVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseDao<T> {
    List<T> findByQuery(BaseQuery baseQuery);
    BaseVO findVOByQuery(BaseQuery baseQuery);
    T findById(Long id);
}
