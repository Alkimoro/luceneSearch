package com.yyy.query;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkuQuery extends BaseQuery {

    //商品主键id
    private String id;
    //商品名称
    private String name;
    //价格
    private Integer price;
    //价格区间
    private Integer minPrice;
    private Integer maxPrice;
    //库存数量
    private Integer num;
    //图片
    private String image;
    //分类名称
    private String categoryName;
    //品牌名称
    private String brandName;
    //规格
    private String spec;
    //销量
    private Integer saleNum;

}
