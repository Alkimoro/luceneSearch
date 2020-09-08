package com.yyy.service;

import com.yyy.dao.lucene.LuceneSkuDao;
import com.yyy.dao.tradition.SkuDao;
import com.yyy.domain.Sku;
import com.yyy.query.LuceneSkuQuery;
import com.yyy.query.SkuQuery;
import com.yyy.viewobject.SkuVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("searchService")
public class SkuService {

    @Resource
    private SkuDao skuDao;

    @Resource
    private LuceneSkuDao luceneSkuDao;

    @Transactional
    public List<Sku> querySkuList(SkuQuery skuQuery){
        return skuDao.findByQuery(skuQuery);
    }

    @Transactional
    public SkuVO querySkuVOByLucene(LuceneSkuQuery luceneSkuQuery){
        return (SkuVO) luceneSkuDao.findVOByQuery(luceneSkuQuery);
    }
}
