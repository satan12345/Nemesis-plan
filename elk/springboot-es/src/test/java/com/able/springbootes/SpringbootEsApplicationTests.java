package com.able.springbootes;

import com.able.springbootes.bean.Article;
import com.able.springbootes.bean.Book;
import com.able.springbootes.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootEsApplicationTests {
    public static final String INDEX="atuguigu";
    public static final String TYPE="news";

    @Resource
    JestClient jestClient;
    @Resource
    TransportClient elasticsearchClient;
    @Test
    public void contextLoads() {

    }

    /**
     * 给es中保存一个文档
     */
    @Test
    public void test1 () throws Exception{
        Article article=new Article();
        article.setId(1);
        article.setTitle("好消息");
        article.setAuthor("卡卡西");
        article.setContent("hello world");
        Index index = new Index.Builder(article).index(INDEX).type(TYPE).build();
        DocumentResult execute = jestClient.execute(index);
    }
    /**
     * 使用jest进行搜索
     */
    @Test
    public void test2 () throws Exception{
        String json="{\n" +
                "  \"query\": {\n" +
                "    \"match\": {\n" +
                "      \"content\": \"hello\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        Search build = new Search.Builder(json).addIndex(INDEX).addType(TYPE).build();
        SearchResult result = jestClient.execute(build);
        System.out.println(result.getJsonString());

    }

    @Test
    public void test3 () throws Exception{
        GetRequestBuilder getRequestBuilder = elasticsearchClient.prepareGet(INDEX, TYPE, "1");
        ActionFuture<GetResponse> execute = getRequestBuilder.execute();
        System.out.println(execute.get().getSourceAsString());
    }
    @Autowired
    BookRepository bookRepository;
    @Test
    public void test4 (){
        Book book=new Book();
        book.setId(1);
        book.setName("西游记");
        book.setAuthor("吴承恩");
        bookRepository.index(book);
    }
    @Test
    public void test5 (){
        Optional<Book> bookOptional = bookRepository.findById(1);
        System.out.println(bookOptional.get());
    }
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;


}

