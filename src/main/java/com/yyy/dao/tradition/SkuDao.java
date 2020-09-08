package com.yyy.dao.tradition;

import com.yyy.dao.BaseDao;
import com.yyy.domain.Sku;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface SkuDao extends BaseDao<Sku> {

}
