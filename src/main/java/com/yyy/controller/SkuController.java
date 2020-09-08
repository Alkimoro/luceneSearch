package com.yyy.controller;

import com.yyy.domain.BaseResult;
import com.yyy.exception.SystemException;
import com.yyy.query.LuceneSkuQuery;
import com.yyy.service.SkuService;
import com.yyy.viewobject.SkuVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/sku")
public class SkuController {

    @Resource
    private SkuService skuService;

    @ResponseBody
    @PostMapping("/querySkuVOByLucene")
    public BaseResult<SkuVO> querySkuVOByLucene(LuceneSkuQuery luceneSkuQuery){
        try {
            BaseResult<SkuVO> baseResult=new BaseResult<>();
            luceneSkuQuery.checkAndFix();
            SkuVO skuVO = skuService.querySkuVOByLucene(luceneSkuQuery);
            baseResult.setData(skuVO);
            baseResult.setCode(200);
            return baseResult;
        }catch (SystemException systemException){
            BaseResult<SkuVO> baseResult=new BaseResult<>();
            baseResult.setCode(600);
            baseResult.setMsg(systemException.getErrorMsg());
            return baseResult;
        }
    }

}
