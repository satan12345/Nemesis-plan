package com.able.springbootes;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.*;
import org.elasticsearch.search.aggregations.bucket.missing.Missing;
import org.elasticsearch.search.aggregations.bucket.missing.MissingAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author jipeng
 * @date 2019-02-18 13:19
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EsDeom {

    TransportClient client;

    @Before
    public void init() throws Exception {
        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
        client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("118.25.49.75"), 9300)
                );
    }

    @Test
    public void test1() throws Exception {
        GetRequestBuilder getRequestBuilder = client.prepareGet("lib3", "doc", "1");
        ActionFuture<GetResponse> execute = getRequestBuilder.execute();
        GetResponse getResponse = execute.get();
        log.info("查到的结果为:{}", getResponse.getSourceAsString());
    }

    //添加文档
    @Test
    public void test2() throws Exception {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject()
                .field("id", "1")
                .field("title", "Java设计模式之装饰设计模式")
                .field("content", "在不改变原类文件和使用继承的情况下,动态地扩展一个对象的功能")
                .field("postDate", "2018-05-20")
                .field("url", "www.baidu.com")
                .endObject();
        IndexResponse indexResponse = client.prepareIndex("index1", "doc", "1")
                .setSource(xContentBuilder).get();
        log.info("status={}", indexResponse.status());

    }

    //删除文档
    @Test
    public void test3() {
        DeleteResponse response = client.prepareDelete("index1", "doc", "1").get();
        log.info("status={}", response.getResult());
    }

    @Test
    public void test4() {
        GetResponse getResponse = client.prepareGet("index1", "doc", "1").get();
        log.info("是否存在={}", getResponse.isExists());
        log.info("内容为:{}", getResponse.getSourceAsString());
    }

    //更新
    @Test
    public void test5() throws Exception {
        UpdateRequest request = new UpdateRequest();
        request.index("index1").type("doc").id("1")
                .doc(XContentFactory.jsonBuilder().startObject()
                        .field("title", "单例设计模式")
                        .endObject());
        UpdateResponse updateResponse = client.update(request).get();
        log.info("是否更新成功:{}", updateResponse.getResult());
    }

    @Test
    public void test6() {
        Map<String, String> map = Maps.newHashMap();
        map.put("content", "修改后的content");
        UpdateResponse updateResponse = client.prepareUpdate("index1", "doc", "1").setDoc(map).get();
        log.info("是否更新成功:{}", updateResponse.getResult());
    }

    //upsert
    @Test
    public void test7() throws Exception {
        IndexRequest source = new IndexRequest().index("index1").type("doc").id("2")
                .source(
                        XContentFactory.jsonBuilder().startObject()
                                .field("id", "2")
                                .field("title", "Java设计模式之工厂设计模式")
                                .field("content", "静态工厂,实例工厂")
                                .field("postDate", "2017-05-20")
                                .field("url", "www.google.com")
                                .endObject()
                );
        UpdateRequest doc = new UpdateRequest().index("index1").type("doc").id("2").doc(
                XContentFactory.jsonBuilder().startObject().
                        field("title", "命令行设计模式").endObject()
        ).upsert(source);
        UpdateResponse updateResponse = client.update(doc).get();
        log.info("操作的状态为:{}", updateResponse.getResult());


    }

    @Test
    public void test8() {

        MultiGetResponse multiGetItemResponses = client.prepareMultiGet().add("index1", "doc", Arrays.asList("1", "2", "3")).get();
        MultiGetItemResponse[] responses = multiGetItemResponses.getResponses();
        Arrays.stream(responses).forEach(mgir -> {
            log.info("元素是否存在:{}", mgir.getResponse().isExists());
            log.info("查询到的元素为:{}", mgir.getResponse().getSourceAsString());
        });


    }

    @Test
    public void test9() throws Exception {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        bulkRequestBuilder.add(client.prepareIndex("index1", "doc", "3").setSource(
                XContentFactory.jsonBuilder().startObject()
                        .field("id", "3")
                        .field("title", "代理设计模式")
                        .field("content", "代理设计模式")
                        .field("postDate", "2019-02-18")
                        .field("url", "www.sina.com")
                        .endObject()
        ));
        Map<String, String> map = Maps.newHashMap();
        map.put("title", "再次更新后的标题");
        bulkRequestBuilder.add(client.prepareUpdate("index1", "doc", "2").setDoc(map));
        bulkRequestBuilder.add(client.prepareDelete("index1", "doc", "5"));
        BulkResponse bulkItemResponses = client.bulk(bulkRequestBuilder.request()).get();
        Arrays.stream(bulkItemResponses.getItems()).forEach(bir -> {
            log.info("状态为:{}", bir.getResponse().getResult());
        });
    }

    //delete by query
    @Test
    public void test10() {
        BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.
                newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("content", "工厂"))
                .source("index1")
                .get();
        System.out.println(bulkByScrollResponse.getBatches());
    }

    //match_all
    @Test
    public void test11() {
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        SearchResponse searchResponse = client.prepareSearch("index1")
                .setQuery(matchAllQueryBuilder)
                .setSize(3)
                .get();

        searchResponse.getHits().forEach(x -> {
            System.out.println(x.getScore());
            System.out.println(x.getSourceAsString());
            System.out.println(x.getSourceAsMap());
        });
    }

    //match
    @Test
    public void test12() {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "模式");
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(matchQueryBuilder).get();
        searchResponse.getHits().forEach(x -> {
            System.out.println(x.getSourceAsString());
        });
    }

    //mulitMatch
    @Test
    public void test13() {
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("模式", "title", "content");
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(multiMatchQueryBuilder).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //term query
    @Test
    public void test14() {
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("postDate", "2017-05-20");
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(queryBuilder).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }

    }

    //terms qyery
    @Test
    public void test15() {
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery("postDate", "2017-05-20", "2019-02-18");
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(queryBuilder).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    // range query
    @Test
    public void test16() {
        RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("postDate").from("1999-12-12").to("2019-12-12");
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(queryBuilder).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //prefex
    @Test
    public void test17() {
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("title", "单");
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(prefixQueryBuilder).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //wildcard 通配符
    @Test
    public void test18() {
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(QueryBuilders.wildcardQuery("title", "代理*")).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }

    }

    //fuzzy
    @Test
    public void test19() {
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(QueryBuilders.fuzzyQuery("title", "设计")).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //type query
    @Test
    public void test20() {
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(QueryBuilders.typeQuery("doc")).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //ids query
    @Test
    public void test21() {
        SearchResponse searchResponse = client.prepareSearch("index1")
                .setQuery(QueryBuilders.idsQuery("doc").addIds(new String[]{"1", "2", "3"})).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //agg
    @Test
    public void test22() {
        MaxAggregationBuilder field = AggregationBuilders.max("aggAgeMax").field("age");
        SearchResponse searchResponse = client.prepareSearch("lib3").addAggregation(
                field).get();
        Max max = searchResponse.getAggregations().get("aggAgeMax");
        System.out.println(max.getValue());

        searchResponse = client.prepareSearch("lib3").addAggregation(AggregationBuilders.avg("avg_age").field("age")).get();
        Avg avg = searchResponse.getAggregations().get("avg_age");
        System.out.println(avg.getValue());

        searchResponse = client.prepareSearch("lib3").addAggregation(AggregationBuilders.min("minAge").field("age")).get();
        Min min = searchResponse.getAggregations().get("minAge");
        System.out.println(min.getValue());

        searchResponse = client.prepareSearch("lib3").addAggregation(AggregationBuilders.sum("sumAge").field("age")).get();
        Sum sum = searchResponse.getAggregations().get("sumAge");
        System.out.println(sum.getValue());

        searchResponse = client.prepareSearch("lib3").addAggregation(AggregationBuilders.cardinality("cardinality").field("age")).get();
        Cardinality cardinality = searchResponse.getAggregations().get("cardinality");
        System.out.println(cardinality.getValue());

    }

    @Test
    public void test23() {
        SearchResponse searchResponse = client.prepareSearch("index1").setQuery(QueryBuilders.commonTermsQuery("title", "java")).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        searchResponse = client.prepareSearch("lib3").setQuery(QueryBuilders.queryStringQuery("+喝酒 -唱歌")).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("-----------");
        searchResponse = client.prepareSearch("lib3").setQuery(QueryBuilders.simpleQueryStringQuery("+喝酒 -唱歌")).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //bool query
    @Test
    public void test24() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("interests", "听音乐"))
                .mustNot(QueryBuilders.matchQuery("interests", "喝酒"))
                .should(QueryBuilders.matchQuery("address", "北京"))
                .filter(QueryBuilders.rangeQuery("age").gt(26).lte(29));
        SearchResponse searchResponse = client.prepareSearch("lib3").setQuery(boolQueryBuilder).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    //constantscore
    @Test
    public void test25() {
        ConstantScoreQueryBuilder constantScoreQuery = QueryBuilders.constantScoreQuery(QueryBuilders.termQuery("name", "赵"));
        SearchResponse searchResponse = client.prepareSearch("lib3").setQuery(constantScoreQuery).get();
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }
    }

    @Test
    public void test26() {
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("terms").field("age");
        SearchResponse searchResponse = client.prepareSearch("lib3").addAggregation(termsAggregationBuilder).get();
        Terms terms = searchResponse.getAggregations().get("terms");
        for (Terms.Bucket bucket : terms.getBuckets()) {
            System.out.println(bucket.getKey() + ":" + bucket.getDocCount());
        }
    }

    @Test
    public void test27() {
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("age", 20);
        FilterAggregationBuilder filter = AggregationBuilders.filter("filter", queryBuilder);
        SearchResponse searchResponse = client.prepareSearch("lib3").addAggregation(filter).get();
        Filter filter1 = searchResponse.getAggregations().get("filter");
        System.out.println(filter1.getDocCount());
    }

    @Test
    public void test28() {
        FiltersAggregationBuilder filters = AggregationBuilders.filters("filters", new FiltersAggregator.KeyedFilter("interests", QueryBuilders.matchQuery("interests", "唱歌")));
        SearchResponse searchResponse = client.prepareSearch("lib3").addAggregation(filters).get();
        Filters filters1 = searchResponse.getAggregations().get("filters");
        List<? extends Filters.Bucket> buckets = filters1.getBuckets();
        for (Filters.Bucket bucket : buckets) {
            System.out.println(bucket.getKey() + ":" + bucket.getDocCount());
        }


    }

    @Test
    public void test29() {
        RangeAggregationBuilder rangeAggregationBuilder = AggregationBuilders.range("range").field("age").addRange(10, 50);
        SearchResponse searchResponse = client.prepareSearch("lib3").addAggregation(rangeAggregationBuilder).get();
        Range range = searchResponse.getAggregations().get("range");
        for (Range.Bucket bucket : range.getBuckets()) {
            System.out.println(bucket.getKey() + ":" + bucket.getDocCount());
        }
    }

    @Test
    public void test30(){
        MissingAggregationBuilder missing = AggregationBuilders.missing("missing").field("price");
        SearchResponse searchResponse = client.prepareSearch("lib4").addAggregation(missing).get();
        Aggregation missing1 = searchResponse.getAggregations().get("missing");
        System.out.println(missing1.toString());
        
    }
    
    @Test
    public void test31(){
        ClusterHealthResponse clusterHealthResponse = client.admin().cluster().prepareHealth().get();

        log.info("clusterName={}",clusterHealthResponse.getClusterName());
        log.info("numberOfDataNodes={}",clusterHealthResponse.getNumberOfDataNodes());
        log.info("numberOfNodes={}",clusterHealthResponse.getNumberOfNodes());
        for (ClusterIndexHealth value : clusterHealthResponse.getIndices().values()) {
            log.info("index={},numberOfShards={},numberOfReplicas={},healthStatus={}",value.getIndex(),value.getNumberOfShards(),
                    value.getNumberOfReplicas(),value.getStatus());
        }

    }

    @After
    public void destroy() {
        client.close();
    }

}

