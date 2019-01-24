package com.able.esstudy.controller;

import com.able.esstudy.model.Book;
import com.able.esstudy.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author jipeng
 * @date 2019-01-23 14:00
 * @description
 */
@RestController
@RequestMapping("book")
public class BookController {
    public static final String INDEX_OF_BOOK="book";
    public static final String TYPE_OF_BOOK="novel";
    @Resource
    TransportClient client;
    @RequestMapping("/query/{id}")
    public ResponseEntity queryById(@PathVariable("id") String id){

        GetResponse response = client.prepareGet(INDEX_OF_BOOK,TYPE_OF_BOOK, id).get();
        if (!response.isExists()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response.getSource(), HttpStatus.OK);
    }
    @PutMapping("save")
    public ResponseEntity save(Book book){
        String jsonString = JsonUtil.obj2String(book);
        IndexResponse response = client.prepareIndex(INDEX_OF_BOOK,TYPE_OF_BOOK, "15").setSource(jsonString, XContentType.JSON).get();

        return ResponseEntity.ok(response.getId());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteById(@PathVariable("id") String id){
        DeleteResponse response = client.prepareDelete(INDEX_OF_BOOK, TYPE_OF_BOOK, id).get();

        return new ResponseEntity(response.status(),HttpStatus.OK);
    }

    @PostMapping("update/{id}")
    public ResponseEntity update(@PathVariable("id") String id,@RequestParam(required = false,defaultValue = "") String author
            ,@RequestParam(required = false,defaultValue = "")String title) throws Exception{
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(INDEX_OF_BOOK);
        updateRequest.type(TYPE_OF_BOOK);
        updateRequest.id(id);

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject();
        if (!StringUtils.isEmpty(title)) {
            xContentBuilder.field("title",title);
        }
        if (!StringUtils.isEmpty(author)) {
            xContentBuilder.field("author",author);
        }
        xContentBuilder.endObject();
        updateRequest.doc(xContentBuilder);
        UpdateResponse response = client.update(updateRequest).get();
        return ResponseEntity.ok(response.getResult().toString());
    }

    @GetMapping("query")
    public ResponseEntity queryByCond(@RequestParam(name = "author",required = false,defaultValue = "") String author,
                                      @RequestParam(name = "title",required = false,defaultValue = "") String title,
                                      @RequestParam(name = "gtWorlds", required = false) Integer gtWorlds,
                                      @RequestParam(name = "ltWorlds",required = false) Integer ltWorlds
                                      ){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(author)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("author",author));
        }
        if (!StringUtils.isEmpty(title)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("title",title));
        }
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch().setIndices(INDEX_OF_BOOK).setTypes(TYPE_OF_BOOK)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(10);
        SearchResponse searchResponse = searchRequestBuilder.get();
        List<Map<String,Object>> list=new ArrayList<>();
        searchResponse.getHits().forEach(x->list.add(x.getSource()));
        return ResponseEntity.ok(list);


    }

}

