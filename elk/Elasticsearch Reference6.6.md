# Elasticsearch Reference

## Getting Started

Elasticsearch is a highly scalable open-source full-text search and analytics engine. It allows you to store, search, and analyze big volumes(量) of data quickly and in near real time. It is generally used as the underlying engine/technology that powers applications that have complex search features and requirements.（它通常用作底层引擎/技术，为具有复杂搜索功能和要求的应用程序提供支持。）

Here are a few sample use-cases that Elasticsearch could be used for:

- You run an online web store where you allow your customers to search for products that you sell. In this case, you can use Elasticsearch to store your entire product catalog and inventory（库存） and provide search and autocomplete suggestions for them.
- You want to collect log or transaction（交易） data and you want to analyze and mine this data to look for trends（挖掘这些数据以寻找趋势）, statistics, summarizations, or anomalies（异常）. In this case, you can use Logstash (part of the Elasticsearch/Logstash/Kibana stack) to collect, aggregate, and parse your data, and then have Logstash feed this data into Elasticsearch. Once the data is in Elasticsearch, you can run searches and aggregations to mine any information that is of interest to you.
- You run a price alerting platform which allows price-savvy customers to specify a rule like "I am interested in buying a specific electronic gadget（小工具） and I want to be notified if the price of gadget falls below $X from any vendor（供应商） within the next month". In this case you can scrape（收集） vendor prices, push them into Elasticsearch and use its reverse-search (Percolator) capability to match price movements（价格浮动） against (针对)customer queries and eventually push the alerts out to the customer once matches are found.
- You have analytics/business-intelligence（商业智能） needs and want to quickly investigate （调查）, analyze, visualize（显示）, and ask ad-hoc questions on a lot of data (think millions or billions of records). In this case, you can use Elasticsearch to store your data and then use Kibana (part of the Elasticsearch/Logstash/Kibana stack) to build custom dashboards that can visualize aspects of your data that are important to you（可以显示对您来说重要方面的数据）. Additionally, you can use the Elasticsearch aggregations functionality to perform complex business intelligence queries against your data.

For the rest of this tutorial, you will be guided through the process of getting Elasticsearch up and running, taking a peek(窥视) inside it, and performing basic operations like indexing, searching, and modifying your data. At the end of this tutorial, you should have a good idea of what Elasticsearch is, how it works, and hopefully be inspired to(受到启发) see how you can use it to either build sophisticated(复杂的) search applications or to mine（挖局） intelligence from your data.



### Basic Concepts

There are a few concepts that are core to Elasticsearch. Understanding these concepts from the outset will tremendously（异常） help ease the learning process.

#### Near Realtime (NRT)（近实时）

Elasticsearch is a near-realtime search platform. What this means is there is a slight latency (normally one second) from the time you index a document until the time it becomes searchable.

#### Cluster

A cluster is a collection of one or more nodes (servers) that together holds your entire(整个) data and provides federated (联合)indexing and search capabilities across all nodes. A cluster is identified by a unique name which by default is "elasticsearch". This name is important because a node can only be part of a cluster if the node is set up to join the cluster by its name.

Make sure that you don’t reuse(重用) the same cluster names in different environments, otherwise you might end up with nodes joining the wrong cluster. For instance you could use `logging-dev`, `logging-stage`, and `logging-prod` for the development, staging, and production clusters.

Note that it is valid and perfectly fine(有效而且非常好) to have a cluster with only a single node in it. Furthermore, you may also have multiple independent clusters each with its own unique cluster name(有多个独立的集群，每个集群都有自己唯一的集群名称).

####Node

A node is a single server that is part of your cluster, stores your data, and participates in (参与)the cluster’s indexing and search capabilities. Just like a cluster, a node is identified by(由...确定) a name which by default is a random Universally Unique IDentifier (UUID) that is assigned to the node at startup. You can define any node name you want if you do not want the default. This name is important for administration purposes where you want to identify which servers in your network correspond to which nodes in your Elasticsearch cluster.

A node can be configured to join a specific cluster by the cluster name. By default, each node is set up to join a cluster named `elasticsearch` which means that if you start up a number of nodes on your network and—assuming （假设）they can discover each other（假设他们可以发现彼此）—they will all automatically form and join a single cluster named `elasticsearch`.

In a single cluster, you can have as many nodes as you want. Furthermore, if there are no other Elasticsearch nodes currently running on your network, starting a single node will by default form a new single-node cluster named `elasticsearch`.

####Index

An index is a collection of documents that have somewhat similar characteristics. For example, you can have an index for customer data, another index for a product catalog, and yet another index for order data. An index is identified by a name (**that must be all lowercase**) and this name is used to refer to the index when performing indexing, search, update, and delete operations against(针对) the documents in it.

In a single cluster, you can define as many indexes as you want.

####Type(6.0以后废弃)

![Warning](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/icons/warning.png)

#####Deprecated in 6.0.0.

See [*Removal of mapping types*](https://www.elastic.co/guide/en/elasticsearch/reference/current/removal-of-types.html)

A type used to be a logical category/partition of your index to allow you to store different types of documents in the same index, e.g. one type for users, another type for blog posts. It is no longer possible to create multiple types in an index, and the whole concept of types will be removed in a later version. See [*Removal of mapping types*](https://www.elastic.co/guide/en/elasticsearch/reference/current/removal-of-types.html) for more.

####Document

A document is a basic unit of information that can be indexed. For example, you can have a document for a single customer, another document for a single product, and yet another for a single order. This document is expressed in [JSON](http://json.org/) (JavaScript Object Notation) which is a ubiquitous internet data interchange format（无处不在的互联网数据交换格式）.

Within an index/type, you can store as many documents as you want. Note that although（虽然） a document physically resides in（属于） an index, a document actually must be indexed/assigned to a type inside an index(实际上，文档必须被索引/分配给索引中的类型).

####Shards & Replicas

An index can potentially(可能) store a large amount of data that can exceed the hardware limits of a single node（超过单个节点的硬件限制）. For example, a single index of a billion documents taking up 1TB of disk space may not fit on the disk of a single node or may be too slow to serve search requests from a single node alone.

To solve this problem, Elasticsearch provides the ability to subdivide(细分) your index into multiple pieces called shards. When you create an index, you can simply define the number of shards that you want. Each shard is in itself a fully-functional and independent "index" that can be hosted on any node in the cluster(每个分片本身都是一个功能齐全且独立的“索引”，可以托管在集群中的任何节点上。).

Sharding is important for two primary reasons:

- It allows you to horizontally split/scale your content volume（水平拆分/缩放内容量）
- It allows you to distribute and parallelize operations across shards (potentially on multiple nodes) thus increasing performance/throughput(从而提高性能/吞吐量)

The mechanics(机制) of how a shard is distributed and also how its documents are aggregated back into search requests are completely managed by Elasticsearch and is transparent（透明） to you as the user.

In a network/cloud environment where failures can be expected anytime, it is very useful and highly recommended（强烈推荐） to have a failover mechanism（容错机制） in case a shard/node somehow goes offline or disappears for whatever reason. To this end, Elasticsearch allows you to make one or more copies of your index’s shards into what are called replica shards, or replicas for short.

Replication is important for two primary reasons:

- It provides high availability in case a shard/node fails. For this reason, it is important to note that a replica shard is never allocated on the same node as the original/primary shard that it was copied from.
- It allows you to scale out（向外扩展） your search volume/throughput since searches can be executed on all replicas in parallel.

To summarize（总结）, each index can be split into multiple shards. An index can also be replicated zero (meaning no replicas) or more times. Once replicated, each index will have primary shards (the original shards that were replicated from) and replica shards (the copies of the primary shards).

The number of shards and replicas can be defined per index at the time the index is created. After the index is created, you may also change the number of replicas dynamically anytime. You can change the number of shards for an existing index using the [`_shrink`](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/indices-shrink-index.html) and [`_split`](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/indices-split-index.html) APIs, however this is not a trivial task（琐碎的任务） and pre-planning for the correct number of shards is the optimal approach（预先计划正确数量的分片是最佳方法）.

By default, each index in Elasticsearch is allocated 5 primary shards and 1 replica which means that if you have at least two nodes in your cluster, your index will have 5 primary shards and another 5 replica shards (1 complete replica) for a total of 10 shards per index.

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/icons/note.png)

Each Elasticsearch shard is a Lucene index. There is a maximum number of documents you can have in a single Lucene index. As of [`LUCENE-5843`](https://issues.apache.org/jira/browse/LUCENE-5843), the limit is `2,147,483,519` (= Integer.MAX_VALUE - 128) documents. You can monitor shard sizes using the [`_cat/shards`](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/cat-shards.html) API.

With that out of the way, let’s get started with the fun part…

###Installation

![Tip](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/icons/tip.png)

You can skip installation completely by using our hosted Elasticsearch Service on [Elastic Cloud](https://www.elastic.co/cloud), which is available on AWS and GCP. You can [try out the hosted service](https://www.elastic.co/cloud/elasticsearch-service/signup) for free.

Elasticsearch requires at least Java 8. Specifically as of this writing, it is recommended that you use the Oracle JDK version 1.8.0_131. Java installation varies from platform to platform so we won’t go into those details here. Oracle’s recommended installation documentation can be found on [Oracle’s website](http://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html). Suffice to say（我只想说）, before you install Elasticsearch, please check your Java version first by running (and then install/upgrade accordingly if needed):

```
java -version
echo $JAVA_HOME
```

Once we have Java set up, we can then download and run Elasticsearch. The binaries（二进制文件） are available from [`www.elastic.co/downloads`](http://www.elastic.co/downloads) along with all the releases that have been made in the past. For each release, you have a choice among a `zip` or `tar` archive, a `DEB` or `RPM` package, or a Windows `MSI` installation package.

####Installation example with tar

For simplicity（简单）, let’s use the [tar](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/zip-targz.html) file.

Let’s download the Elasticsearch 6.6.0 tar as follows:

```
curl -L -O https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.6.0.tar.gz
```

Then extract it as follows:

```
tar -xvf elasticsearch-6.6.0.tar.gz
```

It will then create a bunch of files and folders in your current directory. We then go into the bin directory as follows:

```
cd elasticsearch-6.6.0/bin
```

And now we are ready to start our node and single cluster:

```
./elasticsearch
```

####Installation with Homebrew

On macOS, Elasticsearch can also be installed via [Homebrew](https://brew.sh/):

```
brew install elasticsearch
```

If installation succeeds, Homebrew will finish by saying that you can start Elasticsearch by entering`elasticsearch`. Do that now. The expected response is described below, under [Successfully running node](https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started-install.html#successfully-running-node)[edit](https://github.com/elastic/elasticsearch/edit/6.6/docs/reference/getting-started.asciidoc).

####Installation example with MSI Windows Installer

For Windows users, we recommend using the [MSI Installer package](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/windows.html). The package contains a graphical user interface (GUI) that guides you through the installation process.

First, download the Elasticsearch 6.6.0 MSI from<https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.6.0.msi>.

Then double-click the downloaded file to launch the GUI. Within the first screen, select the deployment directories:

![images/msi_installer/msi_installer_locations.png](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/msi_installer/msi_installer_locations.png)

Then select whether to install as a service or start Elasticsearch manually as needed. To align with the tar example, choose not to install as a service:

![images/msi_installer/msi_installer_no_service.png](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/msi_installer/msi_installer_no_service.png)

For configuration, simply leave the default values:

![images/msi_installer/msi_installer_configuration.png](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/msi_installer/msi_installer_configuration.png)

Again, to align with the tar example, uncheck all plugins to not install any plugins:

![images/msi_installer/msi_installer_plugins.png](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/msi_installer/msi_installer_plugins.png)

After clicking the install button, Elasticsearch will be installed:

![images/msi_installer/msi_installer_success.png](https://www.elastic.co/guide/en/elasticsearch/reference/current/images/msi_installer/msi_installer_success.png)

By default, Elasticsearch will be installed at `%PROGRAMFILES%\Elastic\Elasticsearch`. Navigate here and go into the bin directory as follows:

**with Command Prompt:**

```
cd %PROGRAMFILES%\Elastic\Elasticsearch\bin
```

**with PowerShell:**

```
cd $env:PROGRAMFILES\Elastic\Elasticsearch\bin
```

And now we are ready to start our node and single cluster:

```
.\elasticsearch.exe
```

####Successfully running node

If everything goes well with installation, you should see a bunch of messages that look like below:

```
[2016-09-16T14:17:51,251][INFO ][o.e.n.Node               ] [] initializing ...
[2016-09-16T14:17:51,329][INFO ][o.e.e.NodeEnvironment    ] [6-bjhwl] using [1] data paths, mounts [[/ (/dev/sda1)]], net usable_space [317.7gb], net total_space [453.6gb], spins? [no], types [ext4]
[2016-09-16T14:17:51,330][INFO ][o.e.e.NodeEnvironment    ] [6-bjhwl] heap size [1.9gb], compressed ordinary object pointers [true]
[2016-09-16T14:17:51,333][INFO ][o.e.n.Node               ] [6-bjhwl] node name [6-bjhwl] derived from node ID; set [node.name] to override
[2016-09-16T14:17:51,334][INFO ][o.e.n.Node               ] [6-bjhwl] version[6.6.0], pid[21261], build[f5daa16/2016-09-16T09:12:24.346Z], OS[Linux/4.4.0-36-generic/amd64], JVM[Oracle Corporation/Java HotSpot(TM) 64-Bit Server VM/1.8.0_60/25.60-b23]
[2016-09-16T14:17:51,967][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [aggs-matrix-stats]
[2016-09-16T14:17:51,967][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [ingest-common]
[2016-09-16T14:17:51,967][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [lang-expression]
[2016-09-16T14:17:51,967][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [lang-mustache]
[2016-09-16T14:17:51,967][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [lang-painless]
[2016-09-16T14:17:51,967][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [percolator]
[2016-09-16T14:17:51,968][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [reindex]
[2016-09-16T14:17:51,968][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [transport-netty3]
[2016-09-16T14:17:51,968][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded module [transport-netty4]
[2016-09-16T14:17:51,968][INFO ][o.e.p.PluginsService     ] [6-bjhwl] loaded plugin [mapper-murmur3]
[2016-09-16T14:17:53,521][INFO ][o.e.n.Node               ] [6-bjhwl] initialized
[2016-09-16T14:17:53,521][INFO ][o.e.n.Node               ] [6-bjhwl] starting ...
[2016-09-16T14:17:53,671][INFO ][o.e.t.TransportService   ] [6-bjhwl] publish_address {192.168.8.112:9300}, bound_addresses {{192.168.8.112:9300}
[2016-09-16T14:17:53,676][WARN ][o.e.b.BootstrapCheck     ] [6-bjhwl] max virtual memory areas vm.max_map_count [65530] likely too low, increase to at least [262144]
[2016-09-16T14:17:56,731][INFO ][o.e.h.HttpServer         ] [6-bjhwl] publish_address {192.168.8.112:9200}, bound_addresses {[::1]:9200}, {192.168.8.112:9200}
[2016-09-16T14:17:56,732][INFO ][o.e.g.GatewayService     ] [6-bjhwl] recovered [0] indices into cluster_state
[2016-09-16T14:17:56,748][INFO ][o.e.n.Node               ] [6-bjhwl] started
```

Without going too much into detail（没有太多细节）, we can see that our node named "6-bjhwl" (which will be a different set of characters in your case) has started and elected（选举） itself as a master in a single cluster. Don’t worry yet at the moment what master means. The main thing that is important here is that we have started one node within one cluster.

As mentioned previously, we can override either the cluster or node name. This can be done from the command line when starting Elasticsearch as follows:

```
./elasticsearch -Ecluster.name=my_cluster_name -Enode.name=my_node_name
```

Also note the line marked http with information about the HTTP address (`192.168.8.112`) and port (`9200`) that our node is reachable from. By default, Elasticsearch uses port `9200` to provide access to its REST API. This port is configurable if necessary.

## Exploring Your Cluster（集群探秘）

####The REST API

Now that we have our node (and cluster) up and running, the next step is to understand how to communicate with it. Fortunately, Elasticsearch provides a very comprehensive（全面） and powerful REST API that you can use to interact with your cluster. Among the few things that can be done with the API are as follows:

- Check your cluster, node, and index health, status, and statistics
- Administer your cluster, node, and index data and metadata
- Perform CRUD (Create, Read, Update, and Delete) and search operations against your indexes
- Execute advanced search operations such as paging, sorting, filtering, scripting, aggregations, and many others

#### Cluster Health(集群健康)

Let’s start with a basic health check, which we can use to see how our cluster is doing. We’ll be using curl to do this but you can use any tool that allows you to make HTTP/REST calls. Let’s assume that we are still on the same node where we started Elasticsearch on and open another command shell window.(假设我们仍然在我们启动Elasticsearch的同一节点上打开另一个命令shell窗口)

To check the cluster health, we will be using the [`_cat` API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/cat.html). You can run the command below in [Kibana’s Console](https://www.elastic.co/guide/en/kibana/6.6/console-kibana.html) by clicking "VIEW IN CONSOLE" or with `curl` by clicking the "COPY AS CURL" link below and pasting it into a terminal.

```
GET /_cat/health?v
```

And the response:

```
epoch      timestamp cluster       status node.total node.data shards pri relo init unassign pending_tasks max_task_wait_time active_shards_percent
1475247709 17:01:49  elasticsearch green           1         1      0   0    0    0        0             0                  -                100.0%
```

We can see that our cluster named "elasticsearch" is up with a green status.

Whenever（每当） we ask for the cluster health, we either get green, yellow, or red.

- Green - everything is good (cluster is fully functional)
- Yellow - all data is available but some replicas are not yet allocated (cluster is fully functional)
- Red - some data is not available for whatever reason (cluster is partially functional)

**Note:** When a cluster is red, it will continue to serve search requests from the available shards but you will likely need to fix it ASAP since there are unassigned shards.

Also from the above response, we can see a total of 1 node and that we have 0 shards since we have no data in it yet. Note that since we are using the default cluster name (elasticsearch) and since Elasticsearch uses unicast network discovery by default to find other nodes on the same machine（因为Elasticsearch默认使用单播网络发现来查找同一台机器上的其他节点）, it is possible that you could accidentally start up more than one node on your computer and have them all join a single cluster. In this scenario, you may see more than 1 node in the above response.

We can also get a list of nodes in our cluster as follows:

```
GET /_cat/nodes?v
```

And the response:

```
ip        heap.percent ram.percent cpu load_1m load_5m load_15m node.role master name
127.0.0.1           10           5   5    4.46                        mdi      *      PB2SGZY
```

Here, we can see our one node named "PB2SGZY", which is the single node that is currently in our cluster.

#### List All Indices

Now let’s take a peek（窥视） at our indices:

```
GET /_cat/indices?v
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-list-indices/1.json)[ ]()

And the response:

```
health status index uuid pri rep docs.count docs.deleted store.size pri.store.size
```

Which simply means we have no indices yet in the cluster.（目前为止我们的集群中没有索引）

#### Create an Index

Now let’s create an index named "customer" and then list all the indexes again:

```
PUT /customer?pretty
GET /_cat/indices?v
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-create-index/1.json)[ ]()

The first command creates the index named "customer" using the PUT verb(动词). We simply append `pretty` to the end of the call to tell it to pretty-print the JSON response (if any).

And the response:

```
health status index    uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   customer 95SQ4TSUT7mWBT7VNHH67A   5   1          0            0       260b           260b
```

The results of the second command tells us that we now have 1 index named customer and it has 5 primary shards and 1 replica (the defaults) and it contains 0 documents in it.

You might also notice that the customer index has a yellow health tagged（标记） to it. Recall from our previous discussion that yellow means that some replicas are not (yet) allocated. The reason this happens for this index is because Elasticsearch by default created one replica for this index. Since we only have one node running at the moment, that one replica cannot yet be allocated (for high availability) until a later point in time when another node joins the cluster. Once that replica gets allocated onto a second node, the health status for this index will turn to green.

#### Index and Query a Document

Let’s now put something into our customer index. We’ll index a simple customer document into the customer index, with an ID of 1 as follows:

```
PUT /customer/_doc/1?pretty
{
  "name": "John Doe"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-query-document/1.json)[ ]()

And the response:

```
{
  "_index" : "customer",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "result" : "created",
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "_seq_no" : 0,
  "_primary_term" : 1
}
```

From the above, we can see that a new customer document was successfully created inside the customer index. The document also has an internal id of 1 which we specified at index time.

It is important to note that Elasticsearch does not require you to explicitly create an index first before you can index documents into it. In the previous example, Elasticsearch will automatically create the customer index if it didn’t already exist beforehand.

Let’s now retrieve(取回) that document that we just indexed:

```
GET /customer/_doc/1?pretty
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-query-document/2.json)[ ]()

And the response:

```json
{
  "_index" : "customer",
  "_type" : "_doc",
  "_id" : "1",
  "_version" : 1,
  "_seq_no" : 25,
  "_primary_term" : 1,
  "found" : true,
  "_source" : { "name": "John Doe" }
}
```

Nothing out of the ordinary(普通) here other than a field, `found`,(除了 found字段 这里没有什么不寻常的) stating that we found a document with the requested ID 1 and another field, `_source`, which returns the full JSON document that we indexed from the previous step.

#### Delete an Index

Now let’s delete the index that we just created and then list all the indexes again:

```
DELETE /customer?pretty
GET /_cat/indices?v
```

And the response:

```
health status index uuid pri rep docs.count docs.deleted store.size pri.store.size
```

Which means that the index was deleted successfully and we are now back to where we started with nothing in our cluster.

Before we move on, let’s take a closer look again at some of the API commands that we have learned so far:

```json
PUT /customer
PUT /customer/_doc/1
{
  "name": "John Doe"
}
GET /customer/_doc/1
DELETE /customer
```

If we study the above commands carefully, we can actually see a pattern of how we access data in Elasticsearch. That pattern can be summarized as follows:

```
<HTTP Verb> /<Index>/<Type>/<ID>
```

This REST access pattern is so pervasive(无处不在) throughout all the API commands that if you can simply remember it, you will have a good head start at mastering Elasticsearch.

### Modifying Your Data

Elasticsearch provides data manipulation(操作) and search capabilities in near real time. By default, you can expect a one second delay (refresh interval(刷新间隔)) from the time you index/update/delete your data until the time that it appears in your search results. This is an important distinction(区别) from other platforms like SQL wherein data is immediately available after a transaction is completed.

####Indexing/Replacing Documents

We’ve previously seen how we can index a single document. Let’s recall that command again:

```
PUT /customer/_doc/1?pretty
{
  "name": "John Doe"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-modify-data/1.json)[ ]()

Again, the above will index the specified document into the customer index, with the ID of 1. If we then executed the above command again with a different (or same) document, Elasticsearch will replace (i.e. reindex) a new document on top of the existing one with the ID of 1:

```
PUT /customer/_doc/1?pretty
{
  "name": "Jane Doe"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-modify-data/2.json)[ ]()

The above changes the name of the document with the ID of 1 from "John Doe" to "Jane Doe". If, on the other hand, we use a different ID, a new document will be indexed and the existing document(s) already in the index remains untouched（不变）.

```
PUT /customer/_doc/2?pretty
{
  "name": "Jane Doe"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-modify-data/3.json)[ ]()

The above indexes a new document with an ID of 2.

When indexing, the ID part is optional. If not specified, Elasticsearch will generate a random ID and then use it to index the document. The actual ID Elasticsearch generates (or whatever we specified explicitly in the previous examples) is returned as part of the index API call.

This example shows how to index a document without an explicit ID:

```
POST /customer/_doc?pretty
{
  "name": "Jane Doe"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-modify-data/4.json)[ ]()

**Note that in the above case, we are using the `POST` verb instead of PUT since we didn’t specify an ID.**(如果不指定id 我们需要使用POST操作符代替PUT操作符)

#### Updating Documents

In addition to being able to index and replace documents（除了能够索引和替换文档）, **we** can also update documents. Note though that Elasticsearch does not actually do in-place updates under the hood. Whenever we do an update, Elasticsearch deletes the old document and then indexes a new document with the update applied to it in one shot.

This example shows how to update our previous document (ID of 1) by changing the name field to "Jane Doe":

```json
POST /customer/_doc/1/_update?pretty
{
  "doc": { "name": "Jane Doe" }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-update-documents/1.json)[ ]()

This example shows how to update our previous document (ID of 1) by changing the name field to "Jane Doe" and at the same time add an age field to it:

```json
POST /customer/_doc/1/_update?pretty
{
  "doc": { "name": "Jane Doe", "age": 20 }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-update-documents/2.json)[ ]()

Updates can also be performed by using simple scripts. This example uses a script to increment the age by 5:

```json
POST /customer/_doc/1/_update?pretty
{
  "script" : "ctx._source.age += 5"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-update-documents/3.json)[ ]()

In the above example, `ctx._source` refers to the current source document that is about to be updated.

Elasticsearch provides the ability to update multiple documents given a query condition (like an `SQL UPDATE-WHERE` statement). See [`docs-update-by-query` API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html)

#### Deleting Documents

Deleting a document is fairly straightforward(直截了当). This example shows how to delete our previous customer with the ID of 2:

```
DELETE /customer/_doc/2?pretty
```

See the [`_delete_by_query` API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html) to delete all documents matching a specific query. It is worth noting that(值的注意的是) it is much more efficient to delete a whole index instead of deleting all documents with the Delete By Query API(使用Delete By Query API删除整个索引而不是删除所有文档会更有效).

#### Batch Processing

In addition to being able to index, update, and delete individual（个别） documents, Elasticsearch also provides the ability to perform any of the above operations in batches using the [`_bulk` API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-bulk.html). This functionality is important in that it provides a very efficient mechanism to do multiple operations as fast as possible with as few network roundtrips（往返） as possible.

As a quick example, the following call indexes two documents (ID 1 - John Doe and ID 2 - Jane Doe) in one bulk operation:

```json
POST /customer/_doc/_bulk?pretty
{"index":{"_id":"1"}}
{"name": "John Doe" }
{"index":{"_id":"2"}}
{"name": "Jane Doe" }
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-batch-processing/1.json)[ ]()

This example updates the first document (ID of 1) and then deletes the second document (ID of 2) in one bulk operation:

```
POST /customer/_doc/_bulk?pretty
{"update":{"_id":"1"}}
{"doc": { "name": "John Doe becomes Jane Doe" } }
{"delete":{"_id":"2"}}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-batch-processing/2.json)[ ]()

Note above that for the delete action, there is no corresponding(相应) source document after it since deletes only require the ID of the document to be deleted.

The Bulk API does not fail due to failures in one of the actions. If a single action fails for whatever reason, it will continue to process the remainder of the actions after it. When the bulk API returns, it will provide a status for each action (in the same order it was sent in) so that you can check if a specific action failed or not.

### Exploring（探索） Your Data

####Sample Dataset

Now that we’ve gotten a glimpse(一瞥) of the basics, let’s try to work on a more realistic(切实的) dataset. I’ve prepared a sample of fictitious JSON documents of customer bank account information. Each document has the following schema:

```json
{
    "account_number": 0,
    "balance": 16623,
    "firstname": "Bradshaw",
    "lastname": "Mckenzie",
    "age": 29,
    "gender": "F",
    "address": "244 Columbus Place",
    "employer": "Euron",
    "email": "bradshawmckenzie@euron.com",
    "city": "Hobucken",
    "state": "CO"
}
```

For the curious, this data was generated using [`www.json-generator.com/`](http://www.json-generator.com/), so please ignore the actual values and semantics(语义) of the data as these are all randomly generated.

####Loading the Sample Dataset

You can download the sample dataset (accounts.json) from [here](https://github.com/elastic/elasticsearch/blob/master/docs/src/test/resources/accounts.json?raw=true). Extract it to our current directory and let’s load it into our cluster as follows:

```
curl -H "Content-Type: application/json" -XPOST "localhost:9200/bank/_doc/_bulk?pretty&refresh" --data-binary "@accounts.json"
curl "localhost:9200/_cat/indices?v"
```

And the response:

```
health status index uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   bank  l7sSYV2cQXmu6_4rJWVIww   5   1       1000            0    128.6kb        128.6kb
```

Which means that we just successfully bulk indexed 1000 documents into the bank index (under the `_doc` type).

#### The Search API

Now let’s start with some simple searches. There are two basic ways to run searches: one is by sending search parameters through the [REST request URI](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-uri-request.html) and the other by sending them through the [REST request body](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-request-body.html). The request body method allows you to be more expressive and also to define your searches in a more readable JSON format. We’ll try one example of the request URI method but for the remainder of this tutorial, we will exclusively（仅仅） be using the request body method.

The REST API for search is accessible from the `_search` endpoint. This example returns all documents in the bank index:

```
GET /bank/_search?q=*&sort=account_number:asc&pretty
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-search-API/1.json)[ ]()

Let’s first dissect the search call. We are searching (`_search` endpoint) in the bank index, and the `q=*`parameter instructs Elasticsearch to match all documents in the index. The `sort=account_number:asc`parameter indicates to sort the results using the `account_number` field of each document in an ascending order. The `pretty` parameter, again, just tells Elasticsearch to return pretty-printed JSON results.

And the response (partially shown):

```
{
  "took" : 63,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "skipped" : 0,
    "failed" : 0
  },
  "hits" : {
    "total" : 1000,
    "max_score" : null,
    "hits" : [ {
      "_index" : "bank",
      "_type" : "_doc",
      "_id" : "0",
      "sort": [0],
      "_score" : null,
      "_source" : {"account_number":0,"balance":16623,"firstname":"Bradshaw","lastname":"Mckenzie","age":29,"gender":"F","address":"244 Columbus Place","employer":"Euron","email":"bradshawmckenzie@euron.com","city":"Hobucken","state":"CO"}
    }, {
      "_index" : "bank",
      "_type" : "_doc",
      "_id" : "1",
      "sort": [1],
      "_score" : null,
      "_source" : {"account_number":1,"balance":39225,"firstname":"Amber","lastname":"Duke","age":32,"gender":"M","address":"880 Holmes Lane","employer":"Pyrami","email":"amberduke@pyrami.com","city":"Brogan","state":"IL"}
    }, ...
    ]
  }
}
```

As for the response, we see the following parts:

- `took` – time in milliseconds for Elasticsearch to execute the search
- `timed_out` – tells us if the search timed out or not
- `_shards` – tells us how many shards were searched, as well as a count of the successful/failed searched shards
- `hits` – search results
- `hits.total` – total number of documents matching our search criteria
- `hits.hits` – actual array of search results (defaults to first 10 documents)
- `hits.sort` - sort key for results (missing if sorting by score)
- `hits._score` and `max_score` - ignore these fields for now

Here is the same exact search above using the alternative（替代） request body method:

```
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": [
    { "account_number": "asc" }
  ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/getting-started-search-API/2.json)[ ]()

The difference here is that instead of passing `q=*` in the URI, we provide a JSON-style query request body to the `_search` API. We’ll discuss this JSON query in the next section.

It is important to understand that once you get your search results back, Elasticsearch is completely done with the request and does not maintain any kind of server-side resources or open cursors into your results. This is in stark contrast to many other platforms such as SQL wherein you may initially get a partial subset of your query results up-front and then you have to continuously go back to the server if you want to fetch (or page through) the rest of the results using some kind of stateful server-side cursor.（重要的是要理解，一旦您获得了搜索结果，Elasticsearch就完全完成了请求，并且不会在结果中维护任何类型的服务器端资源或打开游标。 这与SQL等许多其他平台形成鲜明对比，其中您可能最初预先获得查询结果的部分子集，然后如果要获取（或翻页）其余的则必须连续返回服务器 使用某种有状态服务器端游标的结果。）