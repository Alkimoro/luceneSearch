package com.yyy.dao.lucene;

import com.yyy.dao.BaseDao;
import com.yyy.domain.Sku;
import com.yyy.exception.SystemException;
import com.yyy.query.BaseQuery;
import com.yyy.query.LuceneSkuQuery;
import com.yyy.viewobject.BaseVO;
import com.yyy.viewobject.SkuVO;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository("luceneSkuDao")
public class LuceneSkuDao implements BaseDao<Sku> {

    @Resource
    private LuceneIndexManager luceneIndexManager;

    public static final File indexPath=new File("src"+File.separator+"main"+File.separator+"resources"+File.separator+"luceneDir"+File.separator);

    @Override
    public List<Sku> findByQuery(BaseQuery baseQuery) {
        return null;
    }

    @Override
    public BaseVO findVOByQuery(BaseQuery baseQuery) {
        if(baseQuery instanceof LuceneSkuQuery){
            return query((LuceneSkuQuery) baseQuery);
        }else{
            throw new SystemException("[LuceneSkuDao.findVOByQuery] 参数类型错误 应使用 LuceneSkuQuery 作为参数");
        }
    }

    @Override
    public Sku findById(Long id) {
        return null;
    }

    private void createLuceneIndex(){
        indexPath.mkdirs();
        try {
            luceneIndexManager.createLuceneIndex();
        } catch (Exception e) {
            throw new SystemException("[LuceneSkuDao.createLuceneIndex] Lucene索引创建失败");
        }
    }

    private SkuVO query(LuceneSkuQuery luceneSkuQuery) {
        if((!LuceneSkuDao.indexPath.exists())||LuceneSkuDao.indexPath.listFiles()==null||LuceneSkuDao.indexPath.listFiles().length==0){
            createLuceneIndex();
        }
        long startTime = System.currentTimeMillis();
        //1. 需要使用的对象封装
        SkuVO resultModel = new SkuVO();
        //从第几条开始查询
        int start = (luceneSkuQuery.getPageIndex() - 1) * luceneSkuQuery.getPageSize();
        //查询到多少条为止
        int end = luceneSkuQuery.getPageIndex() * luceneSkuQuery.getPageSize();
        //创建分词器
        Analyzer analyzer = new IKAnalyzer();
        //创建组合查询对象
        BooleanQuery.Builder builder = new BooleanQuery.Builder();

        //2. 根据查询关键字封装查询对象
        QueryParser queryParser = new QueryParser("name", analyzer);
        Query query1 = null;
        //判断传入的查询关键字是否为空, 如果为空查询所有, 如果不为空, 则根据关键字查询
        if (StringUtils.isEmpty(luceneSkuQuery.getQueryString())) {
            try {
                query1 = queryParser.parse("*:*");
            } catch (ParseException e) {
                throw new SystemException("查询字符串转换错误");
            }
        } else {
            try {
                query1 = queryParser.parse(luceneSkuQuery.getQueryString());
            } catch (ParseException e) {
                throw new SystemException("查询字符串转换错误");
            }
        }
        //将关键字查询对象, 封装到组合查询对象中
        builder.add(query1, BooleanClause.Occur.MUST);

        //3. 根据价格范围封装查询对象
        if (luceneSkuQuery.getMinPrice()!=null||luceneSkuQuery.getMaxPrice()!=null) {
            int min=0;int max=Integer.MAX_VALUE;
            min=luceneSkuQuery.getMinPrice()==null?min:luceneSkuQuery.getMinPrice();
            max=luceneSkuQuery.getMaxPrice()==null?max:luceneSkuQuery.getMaxPrice();
            Query query2 = IntPoint.newRangeQuery("price",min,max);
            //将价格查询对象, 封装到组合查询对象中
            builder.add(query2, BooleanClause.Occur.MUST);
        }

        //4. 创建Directory目录对象, 指定索引库的位置
        /**
         * 使用MMapDirectory消耗的查询时间
         * ====消耗时间为=========324ms
         * ====消耗时间为=========18ms
         */
        Directory directory = null;
        try {
            directory = FSDirectory.open(Paths.get(LuceneSkuDao.indexPath.getAbsolutePath()));
            //5. 创建输入流对象
            IndexReader reader = DirectoryReader.open(directory);
            //6. 创建搜索对象
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            //7. 搜索并获取搜索结果
            TopDocs topDocs = indexSearcher.search(builder.build(), end);
            //8. 获取查询到的总条数
            resultModel.setTotalCount((int)topDocs.totalHits);
            if(resultModel.getTotalCount()==0){
                throw new SystemException("无与条件匹配的数据。");
            }
            //9. 获取查询到的结果集
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            long endTime = System.currentTimeMillis();
            System.out.println("========= [LuceneSkuDao.query] 消耗时间为=========" + (endTime - startTime) + "ms");

            //10. 遍历结果集封装返回的数据
            List<Sku> skuList = new ArrayList<>();
            if (scoreDocs != null) {
                for (int i = start; i < end; i ++) {
                    if(i>=scoreDocs.length){break;}
                    //通过查询到的文档编号, 找到对应的文档对象
                    Document document = reader.document(scoreDocs[i].doc);
                    //封装Sku对象
                    Sku sku = new Sku();
                    sku.setId(document.get("id"));
                    sku.setPrice(Integer.parseInt(document.get("price")));
                    sku.setImage(document.get("image"));
                    sku.setName(document.get("name"));
                    sku.setBrandName(document.get("brandName"));
                    sku.setCategoryName(document.get("categoryName"));

                    skuList.add(sku);
                }
            }
            //封装查询到的结果集
            resultModel.setSkuList(skuList);
            resultModel.setPageSize(luceneSkuQuery.getPageSize());
            resultModel.setPageIndex(luceneSkuQuery.getPageIndex());
            //总页数
            long pageCount = topDocs.totalHits % luceneSkuQuery.getPageSize() > 0 ?
                    (topDocs.totalHits/luceneSkuQuery.getPageSize()) + 1 : topDocs.totalHits/luceneSkuQuery.getPageSize();
            resultModel.setPageCount((int)(long)pageCount);
            return resultModel;
        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof SystemException){
                throw new SystemException(((SystemException) e).getErrorMsg());
            }else {
                throw new SystemException("Lucene查询错误");
            }
        }
    }
}
