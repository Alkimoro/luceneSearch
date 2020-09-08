package com.yyy.viewobject;

import com.yyy.domain.Sku;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SkuVO extends BaseVO {

    // 商品列表
    private List<Sku> skuList;

}
