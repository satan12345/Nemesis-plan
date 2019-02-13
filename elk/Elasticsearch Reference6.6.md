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

#### Introducing the Query Language

Elasticsearch provides a JSON-style domain-specific language that you can use to execute queries. This is referred to as the [Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl.html). The query language is quite comprehensive(全面) and can be intimidating（吓人） at first glance but the best way to actually learn it is to start with a few basic examples.

Going back to our last example, we executed this query:

```json
GET /bank/_search
{
  "query": { "match_all": {} }
}
```

Dissecting(剖析)the above, the `query` part tells us what our query definition is and the `match_all` part is simply the type of query that we want to run. The `match_all` query is simply a search for all documents in the specified index.

In addition to the `query` parameter, we also can pass other parameters to influence the search results. In the example in the section above we passed in `sort`, here we pass in `size`:

```json
GET /bank/_search
{
  "query": { "match_all": {} },
  "size": 1
}
```

Note that if `size` is not specified, it defaults to 10.

This example does a `match_all` and returns documents 10 through 19:

```json
GET /bank/_search
{
  "query": { "match_all": {} },
  "from": 10,
  "size": 10
}
```

The **`from`** parameter (0-based) specifies which document index to start from and the `size`parameter specifies how many documents to return starting at the from parameter. This feature is useful when implementing paging of search results. Note that if `from` is not specified, it defaults to 0.

This example does a `match_all` and sorts the results by account balance in descending order and returns the top 10 (default size) documents.

```json
GET /bank/_search
{
  "query": { "match_all": {} },
  "sort": { "balance": { "order": "desc" } }
}
```

#### Executing Searches

Now that we have seen a few of the basic search parameters, let’s dig in some more into the Query DSL. Let’s first take a look at the returned document fields. By default, the full JSON document is returned as part of all searches(作为所有搜索的一部分，返回完整的JSON文档). This is referred to as the source (`_source` field in the search hits). If we don’t want the entire source document returned, we have the ability to request only a few fields from within source to be returned.

This example shows how to return two fields, `account_number` and `balance` (inside of `_source`), from the search:

```json
GET /bank/_search
{
  "query": { "match_all": {} },
  "_source": ["account_number", "balance"]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/1.json)[ ]()

Note that the above example simply reduces the `_source` field. It will still only return one field named `_source` but within it, only the fields `account_number` and `balance` are included.（注意，上面的示例只是减少了_source字段。它仍然只返回一个名为_source的字段，但是其中只包含account_number和balance字段）

If you come from a SQL background, the above is somewhat similar in concept to the `SQL SELECT FROM` field list.

Now let’s move on to the query part. Previously, we’ve seen how the `match_all` query is used to match all documents. Let’s now introduce a new query called the [`match` query](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl-match-query.html), which can be thought of as a basic fielded search query (i.e. a search done against a specific field or set of fields).

This example returns the account numbered 20:

```json
GET /bank/_search
{
  "query": { "match": { "account_number": 20 } }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/2.json)[ ]()

This example returns all accounts containing the term "mill" in the address:

```json
GET /bank/_search
{
  "query": { "match": { "address": "mill" } }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/3.json)[ ]()

This example returns all accounts containing the term "mill" or "lane" in the address:

```json
GET /bank/_search
{
  "query": { "match": { "address": "mill lane" } }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/4.json)[ ]()

This example is a variant of `match` (`match_phrase`) that returns all accounts containing the phrase "mill lane" in the address:

```
GET /bank/_search
{
  "query": { "match_phrase": { "address": "mill lane" } }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/5.json)[ ]()

Let’s now introduce the [`bool` query](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl-bool-query.html). The `bool` query allows us to compose smaller queries into bigger queries using boolean logic.(bool 查询允许我们使用布尔逻辑将较小的查询组合成较大的查询)

This example composes two `match` queries and returns all accounts containing "mill" and "lane" in the address:

```json
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/6.json)[ ]()

In the above example, the `bool must` clause specifies all the queries that must be true for a document to be considered a match.(bool must 子句 指明文档想要匹配 必须所有的查询都是true )

In contrast(与此相反), this example composes two `match` queries and returns all accounts containing "mill" or "lane" in the address:

```json
GET /bank/_search
{
  "query": {
    "bool": {
      "should": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/7.json)[ ]()

In the above example, the `bool should` clause specifies a list of queries either of which must be true for a document to be considered a match.

This example composes two `match` queries and returns all accounts that contain neither "mill" nor "lane" in the address:

```json
GET /bank/_search
{
  "query": {
    "bool": {
      "must_not": [
        { "match": { "address": "mill" } },
        { "match": { "address": "lane" } }
      ]
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-search/8.json)[ ]()

In the above example, the `bool must_not` clause specifies a list of queries none of which must be true for a document to be considered a match.

We can combine `must`, `should`, and `must_not` clauses simultaneously（同时） inside a `bool` query. Furthermore（此外）, we can compose `bool` queries inside any of these `bool` clauses to mimic any complex multi-level boolean logic.

This example returns all accounts of anybody who is 40 years old but doesn’t live in ID(aho):

```json
GET /bank/_search
{
  "query": {
    "bool": {
      "must": [
        { "match": { "age": "40" } }
      ],
      "must_not": [
        { "match": { "state": "ID" } }
      ]
    }
  }
}
```

#### Executing Filters

In the previous section, we skipped over a little detail called the document score (`_score` field in the search results). The score is a numeric value that is a relative measure of how well the document matches the search query that we specified. The higher the score, the more relevant the document is, the lower the score, the less relevant the document is.

But queries do not always need to produce scores（但是查询并不总是需要生成分数）, in particular when they are only used for "filtering"（过滤） the document set. Elasticsearch detects(查出) these situations（状况） and automatically optimizes query execution in order not to compute useless scores.(为了不计算不必要的分数)

The [`bool` query](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl-bool-query.html) that we introduced in the previous section also supports `filter` clauses which allow us to use a query to restrict(限定) the documents that will be matched by other clauses, without changing how scores are computed. As an example, let’s introduce the [`range` query](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl-range-query.html), which allows us to filter documents by a range of values. This is generally used for numeric or date filtering.

This example uses a bool query to return all accounts with balances between 20000 and 30000, inclusive. In other words, we want to find accounts with a balance that is greater than or equal to 20000 and less than or equal to 30000.

```json
GET /bank/_search
{
  "query": {
    "bool": {
      "must": { "match_all": {} },
      "filter": {
        "range": {
          "balance": {
            "gte": 20000,
            "lte": 30000
          }
        }
      }
    }
  }
}
```

Dissecting(解剖) the above, the bool query contains a `match_all` query (the query part) and a `range` query (the filter part). We can substitute(替代) any other queries into the query and the filter parts. In the above case, the range query makes perfect sense since documents falling into the range all match "equally", i.e., no document is more relevant than another.

In addition to the `match_all`, `match`, `bool`, and `range` queries, there are a lot of other query types that are available and we won’t go into them here. Since we already have a basic understanding of how they work, it shouldn’t be too difficult to apply this knowledge in learning and experimenting with the other query types.

#### Executing Aggregations

Aggregations provide the ability to group and extract statistics from your data.（聚合提供了对数据进行分组和提取统计信息的能力） The easiest way to think about aggregations is by roughly equating it to the SQL GROUP BY and the SQL aggregate functions. In Elasticsearch, you have the ability to execute searches returning hits and at the same time return aggregated results separate（分开） from the hits all in one response. This is very powerful and efficient in the sense that you can run queries and multiple aggregations（多个聚合） and get the results back of both (or either) operations in one shot avoiding network roundtrips using a concise（简明的） and simplified API.

To start with, this example groups all the accounts by state, and then returns the top 10 (default) states sorted by count descending（递减） (also default):

```json
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword"
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-aggregations/1.json)[ ]()

In SQL, the above aggregation is similar in concept to:

```
SELECT state, COUNT(*) FROM bank GROUP BY state ORDER BY COUNT(*) DESC LIMIT 10;
```

And the response (partially shown):

```json
{
  "took": 29,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "skipped" : 0,
    "failed": 0
  },
  "hits" : {
    "total" : 1000,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "group_by_state" : {
      "doc_count_error_upper_bound": 20,
      "sum_other_doc_count": 770,
      "buckets" : [ {
        "key" : "ID",
        "doc_count" : 27
      }, {
        "key" : "TX",
        "doc_count" : 27
      }, {
        "key" : "AL",
        "doc_count" : 25
      }, {
        "key" : "MD",
        "doc_count" : 25
      }, {
        "key" : "TN",
        "doc_count" : 23
      }, {
        "key" : "MA",
        "doc_count" : 21
      }, {
        "key" : "NC",
        "doc_count" : 21
      }, {
        "key" : "ND",
        "doc_count" : 21
      }, {
        "key" : "ME",
        "doc_count" : 20
      }, {
        "key" : "MO",
        "doc_count" : 20
      } ]
    }
  }
}
```

We can see that there are 27 accounts in `ID` (Idaho), followed by 27 accounts in `TX` (Texas), followed by 25 accounts in `AL` (Alabama), and so forth.

Note that we set `size=0` to not show search hits because we only want to see the aggregation results in the response.

Building on the previous aggregation, this example calculates the average account balance by state (again only for the top 10 states sorted by count in descending order):

```json
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword"
      },
      "aggs": {
        "average_balance": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-aggregations/2.json)[ ]()

Notice how we nested(嵌套) the `average_balance` aggregation inside the `group_by_state` aggregation. This is a common pattern for all the aggregations. You can nest aggregations inside aggregations arbitrarily to extract pivoted summarizations that you require from your data.

Building on the previous aggregation, let’s now sort on the average balance in descending order:

```json
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_state": {
      "terms": {
        "field": "state.keyword",
        "order": {
          "average_balance": "desc"
        }
      },
      "aggs": {
        "average_balance": {
          "avg": {
            "field": "balance"
          }
        }
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-aggregations/3.json)[ ]()

This example demonstrates how we can group by age brackets (ages 20-29, 30-39, and 40-49), then by gender, and then finally get the average account balance, per age bracket, per gender:

```json
GET /bank/_search
{
  "size": 0,
  "aggs": {
    "group_by_age": {
      "range": {
        "field": "age",
        "ranges": [
          {
            "from": 20,
            "to": 30
          },
          {
            "from": 30,
            "to": 40
          },
          {
            "from": 40,
            "to": 50
          }
        ]
      },
      "aggs": {
        "group_by_gender": {
          "terms": {
            "field": "gender.keyword"
          },
          "aggs": {
            "average_balance": {
              "avg": {
                "field": "balance"
              }
            }
          }
        }
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/getting-started-aggregations/4.json)[ ]()

There are many other aggregations capabilities that we won’t go into detail here. The [aggregations reference guide](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-aggregations.html) is a great starting point if you want to do further experimentation.

#### Conclusion(结论)

Elasticsearch is both a simple and complex product. We’ve so far learned the basics of what it is, how to look inside of it, and how to work with it using some of the REST APIs. Hopefully this tutorial has given you a better understanding of what Elasticsearch is and more importantly, inspired you to further experiment with the rest of its great features!

## Set up Elasticsearch（TODO）

This section includes information on how to setup Elasticsearch and get it running, including:

- Downloading
- Installing
- Starting
- Configuring

###Supported platforms

The matrix of officially supported operating systems and JVMs is available here: [Support Matrix](https://www.elastic.co/support/matrix). Elasticsearch is tested on the listed platforms, but it is possible that it will work on other platforms too.

###Java (JVM) Version

Elasticsearch is built using Java, and requires at least [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) in order to run. Only Oracle’s Java and the OpenJDK are supported. The same JVM version should be used on all Elasticsearch nodes and clients.

We recommend installing Java version **1.8.0_131 or a later version in the Java 8 release series**. We recommend using a [supported](https://www.elastic.co/support/matrix) [LTS version of Java](http://www.oracle.com/technetwork/java/eol-135779.html). Elasticsearch will refuse to start(拒绝启动) if a known-bad version of Java is used.

The version of Java that Elasticsearch will use can be configured by setting the `JAVA_HOME`environment variable.

## Upgrade Elasticsearch (TODO)

 ## API Conventions

The **Elasticsearch** REST APIs are exposed(暴露) using [JSON over HTTP](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/modules-http.html).

The conventions（约定） listed in this chapter can be applied throughout the REST API, unless otherwise specified.

- [*Multiple Indices*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/multi-index.html)
- [*Date math support in index names*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/date-math-index-names.html)
- [*Common options*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/common-options.html)
- [*URL-based access control*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/url-access-control.html)

### Multiple Indices

Most APIs that refer to an `index` parameter support execution across multiple indices, using simple `test1,test2,test3` notation(符号) (or `_all` for all indices). It also support wildcards(通配符), for example: `test*` or `*test` or `te*t` or `*test*`, and the ability to "exclude" (`-`), for example: `test*,-test3`.

All multi indices API support the following url query string parameters:

- `ignore_unavailable`

  Controls whether to ignore if any specified indices are unavailable, this includes indices that don’t exist or closed indices. Either `true` or `false` can be specified.

- `allow_no_indices`

  Controls whether to fail if a wildcard(通配符) indices expressions results into no concrete indices. Either `true`or `false` can be specified. For example if the wildcard expression `foo*` is specified and no indices are available that start with `foo` then depending on this setting the request will fail. This setting is also applicable when `_all`, `*` or no index has been specified. This settings also applies for aliases, in case an alias points to a closed index.

- `expand_wildcards`

  Controls to what kind of concrete indices wildcard indices expression expand to. If `open` is specified then the wildcard expression is expanded to only open indices and if `closed` is specified then the wildcard expression is expanded only to closed indices. Also both values (`open,closed`) can be specified to expand to all indices.If `none` is specified then wildcard expansion will be disabled and if `all` is specified, wildcard expressions will expand to all indices (this is equivalent to specifying `open,closed`).

The defaults settings for the above parameters depend on the api being used.

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

Single index APIs such as the [Document APIs](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs.html) and the [single-index `alias` APIs](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/indices-aliases.html) do not support multiple indices.

###Date math support in index names

Date math index name resolution enables you to search a range of time-series indices, rather than searching all of your time-series indices and filtering the results or maintaining aliases. Limiting the number of indices that are searched reduces the load on the cluster and improves execution performance. For example, if you are searching for errors in your daily logs, you can use a date math name template to restrict the search to the past two days.

Almost all APIs that have an `index` parameter, support date math in the `index` parameter value.

A date math index name takes the following form:

```
<static_name{date_math_expr{date_format|time_zone}}>
```

Where:

| `static_name`    | is the static text part of the name      |
| ---------------- | ---------------------------------------- |
| `date_math_expr` | is a dynamic date math expression that computes the date dynamically |
| `date_format`    | is the optional format in which the computed date should be rendered. Defaults to `YYYY.MM.dd`. |
| `time_zone`      | is the optional time zone . Defaults to `utc`. |

You must enclose date math index name expressions within angle brackets, and all special characters should be URI encoded. For example:

```
# GET /<logstash-{now/d}>/_search
GET /%3Clogstash-%7Bnow%2Fd%7D%3E/_search
{
  "query" : {
    "match": {
      "test": "data"
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/date-math-index-names/1.json)[ ]()

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

### Percent encoding of date math characters

The special characters used for date rounding must be URI encoded as follows:

| `<`  | `%3C` |
| ---- | ----- |
| `>`  | `%3E` |
| `/`  | `%2F` |
| `{`  | `%7B` |
| `}`  | `%7D` |
| `|`  | `%7C` |
| `+`  | `%2B` |
| `:`  | `%3A` |
| `,`  | `%2C` |

The following example shows different forms of date math index names and the final index names they resolve to given the current time is 22rd March 2024 noon utc.

| Expression                              | Resolves to           |
| --------------------------------------- | --------------------- |
| `<logstash-{now/d}>`                    | `logstash-2024.03.22` |
| `<logstash-{now/M}>`                    | `logstash-2024.03.01` |
| `<logstash-{now/M{YYYY.MM}}>`           | `logstash-2024.03`    |
| `<logstash-{now/M-1M{YYYY.MM}}>`        | `logstash-2024.02`    |
| `<logstash-{now/d{YYYY.MM.dd|+12:00}}>` | `logstash-2024.03.23` |

To use the characters `{` and `}` in the static part of an index name template, escape them with a backslash `\`, for example:

- `<elastic\\{ON\\}-{now/M}>` resolves to `elastic{ON}-2024.03.01`

The following example shows a search request that searches the Logstash indices for the past three days, assuming the indices use the default Logstash index name format, `logstash-YYYY.MM.dd`.

```
# GET /<logstash-{now/d-2d}>,<logstash-{now/d-1d}>,<logstash-{now/d}>/_search
GET /%3Clogstash-%7Bnow%2Fd-2d%7D%3E%2C%3Clogstash-%7Bnow%2Fd-1d%7D%3E%2C%3Clogstash-%7Bnow%2Fd%7D%3E/_search
{
  "query" : {
    "match": {
      "test": "data"
    }
  }
}
```

### Common options

The following options can be applied to all of the REST APIs.

####Pretty Results

When appending `?pretty=true` to any request made, the JSON returned will be pretty formatted (use it for debugging only!). Another option is to set `?format=yaml` which will cause the result to be returned in the (sometimes) more readable yaml format.

####Human readable output

Statistics are returned in a format suitable for humans (eg `"exists_time": "1h"` or `"size": "1kb"`) and for computers (eg `"exists_time_in_millis": 3600000` or `"size_in_bytes": 1024`). The human readable values can be turned off by adding `?human=false` to the query string. This makes sense when the stats results are being consumed by a monitoring tool, rather than intended for human consumption. The default for the `human` flag is `false`.

####Date Math

Most parameters which accept a formatted date value — such as `gt` and `lt` in [range queries](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl-range-query.html) `range`queries, or `from` and `to` in [`daterange` aggregations](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-aggregations-bucket-daterange-aggregation.html) — understand date maths.

The expression starts with an anchor date, which can either be `now`, or a date string ending with `||`. This anchor date can optionally be followed by one or more maths expressions:

- `+1h` - add one hour
- `-1d` - subtract one day
- `/d` - round down to the nearest day

The supported time units differ from those supported by [time units](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/common-options.html#time-units) for durations. The supported units are:

| `y`  | years   |
| ---- | ------- |
| `M`  | months  |
| `w`  | weeks   |
| `d`  | days    |
| `h`  | hours   |
| `H`  | hours   |
| `m`  | minutes |
| `s`  | seconds |

Assuming `now` is `2001-01-01 12:00:00`, some examples are:

- `now+1h`

  `now` in milliseconds plus one hour. Resolves to: `2001-01-01 13:00:00`

- `now-1h`

  `now` in milliseconds minus one hour. Resolves to: `2001-01-01 11:00:00`

- `now-1h/d`

  `now` in milliseconds minus one hour, rounded down to UTC 00:00. Resolves to: `2001-01-01 00:00:00``

- `2001.02.01\|\|+1M/d`

  `2001-02-01` in milliseconds plus one month. Resolves to: `2001-03-01 00:00:00`

####Response Filtering

All REST APIs accept a `filter_path` parameter that can be used to reduce the response returned by Elasticsearch. This parameter takes a comma separated list of filters expressed with the dot notation:

```
GET /_search?q=elasticsearch&filter_path=took,hits.hits._id,hits.hits._score
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/1.json)[ ]()

Responds:

```
{
  "took" : 3,
  "hits" : {
    "hits" : [
      {
        "_id" : "0",
        "_score" : 1.6375021
      }
    ]
  }
}
```

It also supports the `*` wildcard character to match any field or part of a field’s name:

```
GET /_cluster/state?filter_path=metadata.indices.*.stat*
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/2.json)[ ]()

Responds:

```
{
  "metadata" : {
    "indices" : {
      "twitter": {"state": "open"}
    }
  }
}
```

And the `**` wildcard can be used to include fields without knowing the exact path of the field. For example, we can return the Lucene version of every segment with this request:

```
GET /_cluster/state?filter_path=routing_table.indices.**.state
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/3.json)[ ]()

Responds:

```
{
  "routing_table": {
    "indices": {
      "twitter": {
        "shards": {
          "0": [{"state": "STARTED"}, {"state": "UNASSIGNED"}],
          "1": [{"state": "STARTED"}, {"state": "UNASSIGNED"}],
          "2": [{"state": "STARTED"}, {"state": "UNASSIGNED"}],
          "3": [{"state": "STARTED"}, {"state": "UNASSIGNED"}],
          "4": [{"state": "STARTED"}, {"state": "UNASSIGNED"}]
        }
      }
    }
  }
}
```

It is also possible to exclude one or more fields by prefixing the filter with the char `-`:

```
GET /_count?filter_path=-_shards
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/4.json)[ ]()

Responds:

```
{
  "count" : 5
}
```

And for more control, both inclusive and exclusive filters can be combined in the same expression. In this case, the exclusive filters will be applied first and the result will be filtered again using the inclusive filters:

```
GET /_cluster/state?filter_path=metadata.indices.*.state,-metadata.indices.logstash-*
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/5.json)[ ]()

Responds:

```json
{
  "metadata" : {
    "indices" : {
      "index-1" : {"state" : "open"},
      "index-2" : {"state" : "open"},
      "index-3" : {"state" : "open"}
    }
  }
}
```

Note that Elasticsearch sometimes returns directly the raw value of a field, like the `_source` field. If you want to filter `_source` fields, you should consider combining the already existing `_source`parameter (see [Get API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-get.html#get-source-filtering) for more details) with the `filter_path` parameter like this:

```
POST /library/book?refresh
{"title": "Book #1", "rating": 200.1}
POST /library/book?refresh
{"title": "Book #2", "rating": 1.7}
POST /library/book?refresh
{"title": "Book #3", "rating": 0.1}
GET /_search?filter_path=hits.hits._source&_source=title&sort=rating:desc
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/6.json)[ ]()

```
{
  "hits" : {
    "hits" : [ {
      "_source":{"title":"Book #1"}
    }, {
      "_source":{"title":"Book #2"}
    }, {
      "_source":{"title":"Book #3"}
    } ]
  }
}
```

####Flat Settings

The `flat_settings` flag affects rendering of the lists of settings. When `flat_settings` flag is `true`settings are returned in a flat format:

```
GET twitter/_settings?flat_settings=true
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/7.json)[ ]()

Returns:

```json
{
  "twitter" : {
    "settings": {
      "index.number_of_replicas": "1",
      "index.number_of_shards": "1",
      "index.creation_date": "1474389951325",
      "index.uuid": "n6gzFZTgS664GUfx0Xrpjw",
      "index.version.created": ...,
      "index.provided_name" : "twitter"
    }
  }
}
```

When the `flat_settings` flag is `false` settings are returned in a more human readable structured format:

```
GET twitter/_settings?flat_settings=false
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/8.json)[ ]()

Returns:

```
{
  "twitter" : {
    "settings" : {
      "index" : {
        "number_of_replicas": "1",
        "number_of_shards": "1",
        "creation_date": "1474389951325",
        "uuid": "n6gzFZTgS664GUfx0Xrpjw",
        "version": {
          "created": ...
        },
        "provided_name" : "twitter"
      }
    }
  }
}
```

By default the `flat_settings` is set to `false`.

####Parameters

Rest parameters (when using HTTP, map to HTTP URL parameters) follow the convention of using underscore casing.

####Boolean Values

All REST APIs parameters (both request parameters and JSON body) support providing boolean "false" as the value `false` and boolean "true" as the value `true`. All other values will raise an error.

####Number Values

All REST APIs support providing numbered parameters as `string` on top of supporting the native JSON number types.

####Time units

Whenever durations need to be specified, e.g. for a `timeout` parameter, the duration must specify the unit, like `2d` for 2 days. The supported units are:

| `d`      | days         |
| -------- | ------------ |
| `h`      | hours        |
| `m`      | minutes      |
| `s`      | seconds      |
| `ms`     | milliseconds |
| `micros` | microseconds |
| `nanos`  | nanoseconds  |

####Byte size units

Whenever the byte size of data needs to be specified, eg when setting a buffer size parameter, the value must specify the unit, like `10kb` for 10 kilobytes. Note that these units use powers of 1024, so `1kb` means 1024 bytes. The supported units are:

| `b`  | Bytes     |
| ---- | --------- |
| `kb` | Kilobytes |
| `mb` | Megabytes |
| `gb` | Gigabytes |
| `tb` | Terabytes |
| `pb` | Petabytes |

####Unit-less quantities

Unit-less quantities means that they don’t have a "unit" like "bytes" or "Hertz" or "meter" or "long tonne".

If one of these quantities is large we’ll print it out like 10m for 10,000,000 or 7k for 7,000. We’ll still print 87 when we mean 87 though. These are the supported multipliers:

| ``   | Single |
| ---- | ------ |
| `k`  | Kilo   |
| `m`  | Mega   |
| `g`  | Giga   |
| `t`  | Tera   |
| `p`  | Peta   |

####Distance Units

Wherever distances need to be specified, such as the `distance` parameter in the [Geo Distance Query](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl-geo-distance-query.html)), the default unit if none is specified is the meter. Distances can be specified in other units, such as `"1km"` or `"2mi"` (2 miles).

The full list of units is listed below:

| Mile          | `mi` or `miles`                |
| ------------- | ------------------------------ |
| Yard          | `yd` or `yards`                |
| Feet          | `ft` or `feet`                 |
| Inch          | `in` or `inch`                 |
| Kilometer     | `km` or `kilometers`           |
| Meter         | `m` or `meters`                |
| Centimeter    | `cm` or `centimeters`          |
| Millimeter    | `mm` or `millimeters`          |
| Nautical mile | `NM`, `nmi` or `nauticalmiles` |

####Fuzziness

Some queries and APIs support parameters to allow inexact *fuzzy* matching, using the `fuzziness`parameter.

When querying `text` or `keyword` fields, `fuzziness` is interpreted as a [Levenshtein Edit Distance](http://en.wikipedia.org/wiki/Levenshtein_distance) — the number of one character changes that need to be made to one string to make it the same as another string.

The `fuzziness` parameter can be specified as:

- `0`, `1`, `2`

  the maximum allowed Levenshtein Edit Distance (or number of edits)

- `AUTO`

  generates an edit distance based on the length of the term. Low and high distance arguments may be optionally provided `AUTO:[low],[high]`, if not specified, the default values are 3 and 6, equivalent to `AUTO:3,6` that make for lengths:`0..2`must match exactly`3..5`one edit allowed`>5`two edits allowed`AUTO` should generally be the preferred value for `fuzziness`.

####Enabling stack traces

By default when a request returns an error Elasticsearch doesn’t include the stack trace of the error. You can enable that behavior by setting the `error_trace` url parameter to `true`. For example, by default when you send an invalid `size` parameter to the `_search` API:

```
POST /twitter/_search?size=surprise_me
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/9.json)[ ]()

The response looks like:

```
{
  "error" : {
    "root_cause" : [
      {
        "type" : "illegal_argument_exception",
        "reason" : "Failed to parse int parameter [size] with value [surprise_me]"
      }
    ],
    "type" : "illegal_argument_exception",
    "reason" : "Failed to parse int parameter [size] with value [surprise_me]",
    "caused_by" : {
      "type" : "number_format_exception",
      "reason" : "For input string: \"surprise_me\""
    }
  },
  "status" : 400
}
```

But if you set `error_trace=true`:

```
POST /twitter/_search?size=surprise_me&error_trace=true
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/common-options/10.json)[ ]()

The response looks like:

```
{
  "error": {
    "root_cause": [
      {
        "type": "illegal_argument_exception",
        "reason": "Failed to parse int parameter [size] with value [surprise_me]",
        "stack_trace": "Failed to parse int parameter [size] with value [surprise_me]]; nested: IllegalArgumentException..."
      }
    ],
    "type": "illegal_argument_exception",
    "reason": "Failed to parse int parameter [size] with value [surprise_me]",
    "stack_trace": "java.lang.IllegalArgumentException: Failed to parse int parameter [size] with value [surprise_me]\n    at org.elasticsearch.rest.RestRequest.paramAsInt(RestRequest.java:175)...",
    "caused_by": {
      "type": "number_format_exception",
      "reason": "For input string: \"surprise_me\"",
      "stack_trace": "java.lang.NumberFormatException: For input string: \"surprise_me\"\n    at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)..."
    }
  },
  "status": 400
}
```

####Request body in query string

For libraries that don’t accept a request body for non-POST requests, you can pass the request body as the `source` query string parameter instead. When using this method, the `source_content_type`parameter should also be passed with a media type value that indicates the format of the source, such as `application/json`.(对于不接受非POST请求的请求主体的库，您可以将请求主体作为源查询字符串参数传递。 使用此方法时，还应使用指示源格式的媒体类型值传递source_content_type参数，例如application / json。)

####Content-Type Requirements

The type of the content sent in a request body must be specified using the `Content-Type` header. The value of this header must map to one of the supported formats that the API supports. Most APIs support JSON, YAML, CBOR, and SMILE. The bulk and multi-search APIs support NDJSON, JSON and SMILE; other types will result in an error response.

Additionally, when using the `source` query string parameter the content type must be specified using the `source_content_type` query string parameter.

### URL-based access control

Many users use a proxy with URL-based access control to secure access to Elasticsearch indices. For [multi-search](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-multi-search.html), [multi-get](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-multi-get.html) and [bulk](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-bulk.html) requests, the user has the choice of specifying an index in the URL and on each individual request within the request body. This can make URL-based access control challenging.

To prevent the user from overriding the index which has been specified in the URL(防止用户覆盖URL中指定的索引), add this setting to the `elasticsearch.yml` file:

```
rest.action.multi.allow_explicit_index: false
```

The default value is `true`, but when set to `false`, Elasticsearch will reject requests that have an explicit index specified in the request body.

## Document APIs

This section starts with a short introduction to Elasticsearch’s [data replication model](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-replication.html), followed by a detailed(详细的) description of the following CRUD APIs:

**Single document APIs**

- [*Index API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html)
- [*Get API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-get.html)
- [*Delete API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete.html)
- [*Update API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update.html)

**Multi-document APIs**

- [*Multi Get API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-multi-get.html)
- [*Bulk API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-bulk.html)
- [*Delete By Query API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html)
- [*Update By Query API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html)
- [*Reindex API*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-reindex.html)

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

All CRUD APIs are single-index APIs. The `index` parameter accepts a single index name, or an `alias` which points to a single index.

### Reading and Writing documents

####Introduction

Each index in Elasticsearch is [divided into shards](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/getting-started-concepts.html#getting-started-shards-and-replicas) (被分片)and each shard can have multiple copies. These copies are known as a *replication group* and must be kept in sync（同步） when documents are added or removed. If we fail to do so, reading from one copy will result in very different results than reading from another. The process of keeping the shard copies in sync and serving reads from them is what we call the *data replication model*.

Elasticsearch’s data replication model is based on the *primary-backup model（主备模型）* and is described very well in the [PacificA paper](https://www.microsoft.com/en-us/research/publication/pacifica-replication-in-log-based-distributed-storage-systems/) of Microsoft Research. That model is based on having a single copy from the replication group that acts as the primary shard. The other copies are called *replica shards*. The primary serves as the main entry（入口） point for all indexing operations. It is in charge of validating(校验) them and making sure they are correct. Once an index operation has been accepted by the primary, the primary is also responsible for replicating the operation to the other copies.

This purpose of this section is to give a high level overview of the Elasticsearch replication model and discuss the implications(影响) it has for various interactions（互动） between write and read operations.

####Basic write model

Every indexing operation in Elasticsearch is first resolved to a replication group using [routing](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-routing), typically based on the document ID. Once the replication group has been determined（决定）, the operation is forwarded internally to the current *primary shard* of the group（操作就是内部转发到对应分片组的主分片）. The primary shard is responsible for validating the operation and forwarding it to the other replicas. Since replicas can be offline, the primary is not required to replicate to all replicas. Instead, Elasticsearch maintains a list of shard copies that should receive the operation. This list is called the *in-sync copies* and is maintained by the master node. As the name implies, these are the set of "good" shard copies that are guaranteed to have processed all of the index and delete operations that have been acknowledged to the user. The primary is responsible for maintaining this invariant and thus has to replicate all operations to each copy in this set.

The primary shard follows this basic flow:

1. Validate incoming operation and reject it if structurally invalid (Example: have an object field where a number is expected)
2. Execute the operation locally i.e. indexing or deleting the relevant document. This will also validate the content of fields and reject if needed (Example: a keyword value is too long for indexing in Lucene).
3. Forward the operation to each replica in the current in-sync copies set. If there are multiple replicas, this is done in parallel.
4. Once all replicas have successfully performed the operation and responded to the primary, the primary acknowledges the successful completion of the request to the client.

#### Failure handling

Many things can go wrong during indexing — disks can get corrupted(毁坏), nodes can be disconnected from each other, or some configuration mistake could cause an operation to fail on a replica despite（尽管） it being successful on the primary. These are infrequent（罕见的） but the primary has to respond to them.

In the case that the primary itself fails, the node hosting the primary will send a message to the master about it. The indexing operation will wait (up to 1 minute, by [default](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/index-modules.html#dynamic-index-settings)) for the master to promote one of the replicas to be a new primary. The operation will then be forwarded to the new primary for processing. Note that the master also monitors the health of the nodes and may decide to proactively demote a primary. This typically happens when the node holding the primary is isolated（隔离的） from the cluster by a networking issue. See [here](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-replication.html#demoted-primary) for more details.

Once the operation has been successfully performed on the primary, the primary has to deal with potential（潜在的） failures when executing it on the replica shards. This may be caused by an actual failure on the replica or due to a network issue preventing the operation from reaching the replica (or preventing the replica from responding). All of these share the same end result: a replica which is part of the in-sync replica set misses an operation that is about to be acknowledged. In order to avoid violating the invariant, the primary sends a message to the master requesting that the problematic shard be removed from the in-sync replica set. Only once removal of the shard has been acknowledged by the master does the primary acknowledge the operation. Note that the master will also instruct another node to start building a new shard copy in order to restore the system to a healthy state.

While forwarding an operation to the replicas, the primary will use the replicas to validate（验证） that it is still the active primary. If the primary has been isolated（孤立） due to a network partition (or a long GC) it may continue to process incoming indexing operations before realising（认识） that it has been demoted（降级）. Operations that come from a stale primary will be rejected by the replicas. When the primary receives a response from the replica rejecting its request because it is no longer the primary then it will reach out to the master and will learn that it has been replaced. The operation is then routed to the new primary.

**What happens if there are no replicas?**

This is a valid scenario（有效的方案） that can happen due to index configuration or simply because all the replicas have failed. In that case the primary is processing operations without any external validation, which may seem problematic. On the other hand, the primary cannot fail other shards on its own but request the master to do so on its behalf. This means that the master knows that the primary is the only single good copy. We are therefore guaranteed that the master will not promote any other (out-of-date) shard copy to be a new primary and that any operation indexed into the primary will not be lost. Of course, since at that point we are running with only single copy of the data, physical hardware issues can cause data loss. See [Wait For Active Shards](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-wait-for-active-shards)[edit](https://github.com/elastic/elasticsearch/edit/6.6/docs/reference/docs/index_.asciidoc) for some mitigation options.

####Basic read mode

Reads in Elasticsearch can be very lightweight lookups by ID or a heavy search request with complex aggregations that take non-trivial（不平凡的） CPU power. One of the beauties of the primary-backup model is that it keeps all shard copies identical (with the exception of in-flight operations). As such, a single in-sync copy is sufficient（足够） to serve read requests.

When a read request is received by a node, that node is responsible for forwarding it to the nodes that hold the relevant shards, collating the responses, and responding to the client. We call that node the *coordinating node* for that request. The basic flow is as follows:

1. Resolve the read requests to the relevant shards. Note that since most searches will be sent to one or more indices, they typically need to read from multiple shards, each representing a different subset of the data.
2. Select an active copy of each relevant shard, from the shard replication group. This can be either the primary or a replica. By default, Elasticsearch will simply round robin between the shard copies.
3. Send shard level read requests to the selected copies.
4. Combine the results and respond. Note that in the case of get by ID look up, only one shard is relevant and this step can be skipped.

#### Failure handling

When a shard fails to respond to a read request, the coordinating node will select another copy from the same replication group and send the shard level search request to that copy instead. Repetitive（重复的） failures can result in no shard copies being available. In some cases, such as `_search`, Elasticsearch will prefer to respond fast, albeit with partial results, instead of waiting for the issue to be resolved（虽然有部分结果，而不是等待问题得到解决） (partial results are indicated in the `_shards` header of the response).

####A few simple implications

Each of these basic flows determines how Elasticsearch behaves as a system for both reads and writes. Furthermore, since read and write requests can be executed concurrently, these two basic flows interact with each other. This has a few inherent implications:

- Efficient reads

  Under normal operation each read operation is performed once for each relevant replication group. Only under failure conditions do multiple copies of the same shard execute the same search.

- Read unacknowledged

  Since the primary first indexes locally and then replicates the request, it is possible for a concurrent read to already see the change before it has been acknowledged.

- Two copies by default

  This model can be fault tolerant while maintaining only two copies of the data. This is in contrast to quorum-based system where the minimum number of copies for fault tolerance is 3.

####Failures

Under failures, the following is possible:

- A single shard can slow down indexing

  Because the primary waits for all replicas in the in-sync copies set during each operation, a single slow shard can slow down the entire replication group. This is the price we pay for the read efficiency mentioned above. Of course a single slow shard will also slow down unlucky searches that have been routed to it.由于主服务器在每个操作期间等待同步副本集中的所有副本，因此单个慢速分片可能会降低整个复制组的速度。 这是我们为上述阅读效率支付的价格。 当然，单个慢速分片也会减慢已经路由到它的不幸运搜索。

- Dirty reads

  An isolated primary can expose writes that will not be acknowledged. This is caused by the fact that an isolated primary will only realize that it is isolated once it sends requests to its replicas or when reaching out to the master. At that point the operation is already indexed into the primary and can be read by a concurrent read. Elasticsearch mitigates this risk by pinging the master every second (by default) and rejecting indexing operations if no master is known.（隔离的主数据库可以暴露无法识别的写入。 这是因为隔离的主服务器只有在向其副本发送请求或向主服务器发送请求时才会意识到它是隔离的。 此时，操作已经索引到主服务器中，并且可以通过并发读取来读取。 Elasticsearch通过每秒ping一次主服务器（默认情况下）并在没有master知道的情况下拒绝索引操作来减轻这种风险。）

####The Tip of the Iceberg

This document provides a high level overview of how Elasticsearch deals with data. Of course, there is much much more going on under the hood. Things like primary terms, cluster state publishing and master election all play a role in keeping this system behaving correctly. This document also doesn’t cover known and important bugs (both closed and open). We recognize that [GitHub is hard to keep up with](https://github.com/elastic/elasticsearch/issues?q=label%3Aresiliency). To help people stay on top of those and we maintain a dedicated [resiliency page](https://www.elastic.co/guide/en/elasticsearch/resiliency/current/index.html) on our website. We strongly advise reading it.（本文档提供了Elasticsearch如何处理数据的高级概述。 当然，还有很多事情要发生。 主要术语，集群状态发布和主选举等都可以在保持系统正常运行方面发挥作用。 本文档也未涵盖已知和重要的错误（关闭和打开）。 我们认识到GitHub很难跟上。 为了帮助人们掌握这些优势，我们在网站上维护了一个专用的弹性页面。 我们强烈建议阅读它。）

### Index API

The index API adds or updates a typed JSON document in a specific index, making it searchable（可被搜索到的）. The following example inserts the JSON document into the "twitter" index, under a type called `_doc` with an id of 1:

```json
PUT twitter/_doc/1
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/1.json)[ ]()

The result of the above index operation is:

```json
{
    "_shards" : {
        "total" : 2,
        "failed" : 0,
        "successful" : 2
    },
    "_index" : "twitter",
    "_type" : "_doc",
    "_id" : "1",
    "_version" : 1,
    "_seq_no" : 0,
    "_primary_term" : 1,
    "result" : "created"
}
```

The `_shards` header provides information about the replication process of the index operation.

- `total` - Indicates to how many shard copies (primary and replica shards) the index operation should be executed on.
- `successful`- Indicates the number of shard copies the index operation succeeded on.
- `failed` - An array that contains replication related errors in the case an index operation failed on a replica shard.

The index operation is successful in the case `successful` is at least 1.

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

Replica shards may not all be started when an indexing operation successfully returns (by default, only the primary is required, but this behavior can be [changed](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-wait-for-active-shards)). In that case, `total` will be equal to the total shards based on the `number_of_replicas` setting and `successful` will be equal to the number of shards started (primary plus replicas). If there were no failures, the `failed` will be 0.(索引操作成功返回时，副本分片数并不是都启动了（默认情况下，只需要主分片，但可以更改此行为）。 在这种情况下，total将等于基于number_of_replicas设置的总分片数，并且success将等于已启动的分片数（primary plus replicas）。 如果没有失败，则失败将为0。)

####Automatic Index Creation（自动索引创建）

The index operation automatically creates an index if it does not already exist, and applies any [index templates](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/indices-templates.html) that are configured. The index operation also creates a dynamic type mapping for the specified type if one does not already exist. By default, new fields and objects will automatically be added to the mapping definition for the specified type if needed. Check out the [mapping](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/mapping.html) section for more information on mapping definitions, and the the [put mapping](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/indices-put-mapping.html) API for information about updating type mappings manually.（如果索引尚不存在，则索引操作会自动创建索引，并应用已配置的任何索引模板。 索引操作还会为指定的类型创建动态类型映射（如果尚不存在）。 默认情况下，如果需要，新字段和对象将自动添加到指定类型的映射定义中。 有关映射定义的更多信息，请查看映射部分;有关手动更新类型映射的信息，请查看put映射API。）

Automatic index creation is controlled by the `action.auto_create_index` setting. This setting defaults to `true`, meaning that indices are always automatically created. Automatic index creation can be permitted only for indices matching certain patterns by changing the value of this setting to a comma-separated list of these patterns. It can also be explicitly permitted and forbidden by prefixing patterns in the list with a `+` or `-`. Finally it can be completely disabled by changing this setting to `false`.

```json
PUT _cluster/settings
{
    "persistent": {
        "action.auto_create_index": "twitter,index10,-index1*,+ind*" 
    }
}

PUT _cluster/settings
{
    "persistent": {
        "action.auto_create_index": "false" 
    }
}

PUT _cluster/settings
{
    "persistent": {
        "action.auto_create_index": "true" 
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/2.json)[ ]()

| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/1.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#CO37-1) | Permit(允许) only the auto-creation of indices called `twitter`, `index10`, no other index matching `index1*`, and any other index matching `ind*`. The patterns are matched in the order in which they are given. |
| ---------------------------------------- | ---------------------------------------- |
| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/2.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#CO37-2) | Completely disable the auto-creation of indices. |
| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/3.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#CO37-3) | Permit the auto-creation of indices with any name. This is the default. |

####Operation Type

The index operation also accepts an `op_type` that can be used to force a `create` operation, allowing for "put-if-absent" behavior. When `create` is used, the index operation will fail if a document by that id already exists in the index.

Here is an example of using the `op_type` parameter:

```json
PUT twitter/_doc/1?op_type=create
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/3.json)[ ]()

Another option to specify `create` is to use the following uri:

```
PUT twitter/_doc/1/_create
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/4.json)[ ]()

####Automatic ID Generation

The index operation can be executed without specifying the id. In such a case, an id will be generated automatically. In addition, the `op_type` will automatically be set to `create`. Here is an example (note the **POST** used instead of **PUT**):

```
POST twitter/_doc/
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/5.json)[ ]()

The result of the above index operation is:

```
{
    "_shards" : {
        "total" : 2,
        "failed" : 0,
        "successful" : 2
    },
    "_index" : "twitter",
    "_type" : "_doc",
    "_id" : "W0tpsmIBdwcYyG50zbta",
    "_version" : 1,
    "_seq_no" : 0,
    "_primary_term" : 1,
    "result": "created"
}
```

####Optimistic concurrency control（乐观的并发控制）

Index operations can be made optional and only be performed if the last modification to the document was assigned the sequence number and primary term specified by the `if_seq_no` and `if_primary_term` parameters. If a mismatch is detected, the operation will result in a `VersionConflictException` and a status code of 409. See [*Optimistic concurrency control*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/optimistic-concurrency-control.html) for more details.

####Routing

By default, shard placement — or `routing` — is controlled by using a hash of the document’s id value. For more explicit （明确的）control, the value fed into the hash function used by the router can be directly specified on a per-operation basis using the `routing` parameter. For example:

```json
POST twitter/_doc?routing=kimchy
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/6.json)[ ]()

In the example above, the "_doc" document is routed to a shard based on the `routing` parameter provided: "kimchy".

When setting up explicit mapping, the `_routing` field can be optionally used to direct the index operation to extract the routing value from the document itself. This does come at the (very minimal) cost of an additional document parsing pass. If the `_routing` mapping is defined and set to be `required`, the index operation will fail if no routing value is provided or extracted.

####Distributed

The index operation is directed to the primary shard based on its route (see the Routing section above) and performed on the actual node containing this shard. After the primary shard completes the operation, if needed, the update is distributed to applicable replicas.(索引操作根据其路由指向主分片（请参阅上面的“路由”部分），并在包含此分片的实际节点上执行。 主分片完成操作后，如果需要，更新将分发到适用的副本。)

####Wait For Active Shards

To improve the resiliency(弹性) of writes to the system, indexing operations can be configured to wait for a certain number of active shard copies before proceeding with the operation. If the requisite number of active shard copies are not available, then the write operation must wait and retry, until either the requisite shard copies have started or a timeout occurs. By default, write operations only wait for the primary shards to be active before proceeding (i.e. `wait_for_active_shards=1`). This default can be overridden in the index settings dynamically by setting `index.write.wait_for_active_shards`. To alter this behavior per operation, the `wait_for_active_shards` request parameter can be used.

Valid values are `all` or any positive integer up to the total number of configured copies per shard in the index (which is `number_of_replicas+1`). Specifying a negative value or a number greater than the number of shard copies will throw an error.

For example, suppose we have a cluster of three nodes, `A`, `B`, and `C` and we create an index `index`with the number of replicas set to 3 (resulting in 4 shard copies, one more copy than there are nodes). If we attempt an indexing operation, by default the operation will only ensure the primary copy of each shard is available before proceeding. This means that even if `B` and `C` went down, and `A` hosted the primary shard copies, the indexing operation would still proceed with only one copy of the data. If `wait_for_active_shards` is set on the request to `3` (and all 3 nodes are up), then the indexing operation will require 3 active shard copies before proceeding, a requirement which should be met because there are 3 active nodes in the cluster, each one holding a copy of the shard. However, if we set `wait_for_active_shards` to `all` (or to `4`, which is the same), the indexing operation will not proceed as we do not have all 4 copies of each shard active in the index. The operation will timeout unless a new node is brought up in the cluster to host the fourth copy of the shard.

It is important to note that this setting greatly reduces the chances of the write operation not writing to the requisite number of shard copies, but it does not completely eliminate the possibility, because this check occurs before the write operation commences. Once the write operation is underway, it is still possible for replication to fail on any number of shard copies but still succeed on the primary. The `_shards` section of the write operation’s response reveals the number of shard copies on which replication succeeded/failed.

```
{
    "_shards" : {
        "total" : 2,
        "failed" : 0,
        "successful" : 2
    }
}
```

####Refresh

Control when the changes made by this request are visible to search. See [refresh](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-refresh.html).

####Noop Updates

When updating a document using the index api a new version of the document is always created even if the document hasn’t changed. If this isn’t acceptable use the `_update` api with `detect_noop`set to true. This option isn’t available on the index api because the index api doesn’t fetch the old source and isn’t able to compare it against the new source.

There isn’t a hard and fast rule about when noop updates aren’t acceptable. It’s a combination of lots of factors like how frequently your data source sends updates that are actually noops and how many queries per second Elasticsearch runs on the shard with receiving the updates.

####Timeout

The primary shard assigned to perform the index operation might not be available when the index operation is executed. Some reasons for this might be that the primary shard is currently recovering from a gateway or undergoing relocation. By default, the index operation will wait on the primary shard to become available for up to 1 minute before failing and responding with an error. The `timeout` parameter can be used to explicitly specify how long it waits. Here is an example of setting it to 5 minutes:

```json
PUT twitter/_doc/1?timeout=5m
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/7.json)[ ]()

####Versioning

Each indexed document is given a version number. By default, internal versioning is used that starts at 1 and increments with each update, deletes included. Optionally, the version number can be set to an external value (for example, if maintained in a database). To enable this functionality, `version_type` should be set to `external`. The value provided must be a numeric, long value greater or equal to 0, and less than around 9.2e+18.

When using the external version type, the system checks to see if the version number passed to the index request is greater than the version of the currently stored document. If true, the document will be indexed and the new version number used. If the value provided is less than or equal to the stored document’s version number, a version conflict will occur and the index operation will fail. For example:

```
PUT twitter/_doc/1?version=2&version_type=external
{
    "message" : "elasticsearch now has versioning support, double cool!"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-index_/8.json)[ ]()

**NOTE:** versioning is completely real time, and is not affected by the near real time aspects of search operations. If no version is provided, then the operation is executed without any version checks.

The above will succeed since the the supplied version of 2 is higher than the current document version of 1. If the document was already updated and its version was set to 2 or higher, the indexing command will fail and result in a conflict (409 http status code).

![Warning](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/warning.png)

External versioning supports the value 0 as a valid version number. This allows the version to be in sync with an external versioning system where version numbers start from zero instead of one. It has the side effect that documents with version number equal to zero can neither be updated using the [Update-By-Query API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html) nor be deleted using the [Delete By Query API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html) as long as their version number is equal to zero.

A nice side effect is that there is no need to maintain strict ordering of async indexing operations executed as a result of changes to a source database, as long as version numbers from the source database are used. Even the simple case of updating the Elasticsearch index using data from a database is simplified if external versioning is used, as only the latest version will be used if the index operations arrive out of order for whatever reason.

#### Version types

Next to the `external` version type explained above, Elasticsearch also supports other types for specific use cases. Here is an overview of the different version types and their semantics.

- `internal`

  only index the document if the given version is identical to the version of the stored document.

- `external` or `external_gt`

  only index the document if the given version is strictly higher than the version of the stored document **or** if there is no existing document. The given version will be used as the new version and will be stored with the new document. The supplied version must be a non-negative long number.

- `external_gte`

  only index the document if the given version is **equal** or higher than the version of the stored document. If there is no existing document the operation will succeed as well. The given version will be used as the new version and will be stored with the new document. The supplied version must be a non-negative long number.

**NOTE**: The `external_gte` version type is meant for special use cases and should be used with care. If used incorrectly, it can result in loss of data. There is another option, `force`, which is deprecated because it can cause primary and replica shards to diverge.

### Get API

The get API allows to get a typed JSON document from the index based on its id. The following example gets a JSON document from an index called twitter, under a type called `_doc`, with id valued 0:

```
GET twitter/_doc/0
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/1.json)[ ]()

The result of the above get operation is:

```
{
    "_index" : "twitter",
    "_type" : "_doc",
    "_id" : "0",
    "_version" : 1,
    "_seq_no" : 10,
    "_primary_term" : 1,
    "found": true,
    "_source" : {
        "user" : "kimchy",
        "date" : "2009-11-15T14:12:12",
        "likes": 0,
        "message" : "trying out Elasticsearch"
    }
}
```

The above result includes the `_index`, `_type`, `_id` and `_version` of the document we wish to 、



, including the actual `_source` of the document if it could be found (as indicated by the `found`field in the response).

The API also allows to check for the existence of a document using `HEAD`, for example:

```
HEAD twitter/_doc/0
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/2.json)[ ]()

### Realtime[edit](https://github.com/elastic/elasticsearch/edit/6.6/docs/reference/docs/get.asciidoc)

By default, the get API is realtime, and is not affected by the refresh rate of the index (when data will become visible for search). If a document has been updated but is not yet refreshed, the get API will issue a refresh call in-place to make the document visible. This will also make other documents changed since the last refresh visible. In order to disable realtime GET, one can set the `realtime`parameter to `false`.

### Source filtering[edit](https://github.com/elastic/elasticsearch/edit/6.6/docs/reference/docs/get.asciidoc)

By default, the get operation returns the contents of the `_source` field unless you have used the `stored_fields` parameter or if the `_source` field is disabled. You can turn off `_source` retrieval by using the `_source` parameter:

```
GET twitter/_doc/0?_source=false
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/3.json)[ ]()

If you only need one or two fields from the complete `_source`, you can use the `_source_includes` & `_source_excludes` parameters to include or filter out that parts you need. This can be especially helpful with large documents where partial retrieval can save on network overhead. Both parameters take a comma separated list of fields or wildcard expressions. Example:

```
GET twitter/_doc/0?_source_includes=*.id&_source_excludes=entities
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/4.json)[ ]()

If you only want to specify includes, you can use a shorter notation:

```
GET twitter/_doc/0?_source=*.id,retweeted
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/5.json)[ ]()

####Stored Fields

The get operation allows specifying(指定) a set of stored fields that will be returned by passing the `stored_fields` parameter. If the requested fields are not stored, they will be ignored. Consider for instance the following mapping:

```json
PUT twitter
{
   "mappings": {
      "_doc": {
         "properties": {
            "counter": {
               "type": "integer",
               "store": false
            },
            "tags": {
               "type": "keyword",
               "store": true
            }
         }
      }
   }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/6.json)[ ]()

Now we can add a document:

```json
PUT twitter/_doc/1
{
    "counter" : 1,
    "tags" : ["red"]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/7.json)[ ]()

1. and try to retrieve it:

```
GET twitter/_doc/1?stored_fields=tags,counter
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/8.json)[ ]()

The result of the above get operation is:

```
{
   "_index": "twitter",
   "_type": "_doc",
   "_id": "1",
   "_version": 1,
   "_seq_no" : 22,
   "_primary_term" : 1,
   "found": true,
   "fields": {
      "tags": [
         "red"
      ]
   }
}
```

Field values fetched from the document itself are always returned as an array. Since the `counter`field is not stored the get request simply ignores it when trying to get the `stored_fields.`

It is also possible to retrieve metadata fields like the `_routing` field:

```
PUT twitter/_doc/2?routing=user1
{
    "counter" : 1,
    "tags" : ["white"]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/9.json)[ ]()

```
GET twitter/_doc/2?routing=user1&stored_fields=tags,counter
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/10.json)[ ]()

The result of the above get operation is:

```
{
   "_index": "twitter",
   "_type": "_doc",
   "_id": "2",
   "_version": 1,
   "_seq_no" : 13,
   "_primary_term" : 1,
   "_routing": "user1",
   "found": true,
   "fields": {
      "tags": [
         "white"
      ]
   }
}
```

Also only leaf fields can be returned via the `stored_field` option. So object fields can’t be returned and such requests will fail.

####Getting the `_source` directly

Use the `/{index}/{type}/{id}/_source` endpoint to get just the `_source` field of the document, without any additional content around it. For example:

```
GET twitter/_doc/1/_source
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/11.json)[ ]()

You can also use the same source filtering parameters to control which parts of the `_source` will be returned:

```
GET twitter/_doc/1/_source?_source_includes=*.id&_source_excludes=entities'
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/12.json)[ ]()

Note, there is also a HEAD variant(变种) for the _source endpoint to efficiently test for document _source existence（存在）. An existing document will not have a _source if it is disabled in the [mapping](https://www.elastic.co/guide/en/elasticsearch/reference/current/mapping-source-field.html).

```
HEAD twitter/_doc/1/_source
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/13.json)[ ]()

####Routing

When indexing using the ability to control the routing, in order to get a document, the routing value should also be provided. For example:

```
GET twitter/_doc/2?routing=user1
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-get/14.json)[ ]()

The above will get a tweet with id `2`, but will be routed based on the user. Note, issuing a get without the correct routing, will cause the document not to be fetched.（注意，在没有正确路由的情况下发出get将导致不提取文档。）

####Preference（偏好）

Controls a `preference` of which shard replicas to execute the get request on. By default, the operation is randomized between the shard replicas.

The `preference` can be set to:

- `_primary`

  The operation will go and be executed only on the primary shards.

- `_local`

  The operation will prefer to be executed on a local allocated shard if possible.

- Custom (string) value

  A custom value will be used to guarantee that the same shards will be used for the same custom value. This can help with "jumping values" when hitting different shards in different refresh states. A sample value can be something like the web session id, or the user name.

####Refresh

The `refresh` parameter can be set to `true` in order to refresh the relevant（相关的） shard before the get operation and make it searchable. Setting it to `true` should be done after careful thought and verification that this does not cause a heavy load on the system (and slows down indexing).

####Distributed

The get operation gets hashed into a specific shard id. It then gets redirected to one of the replicas within that shard id and returns the result. The replicas are the primary shard and its replicas within that shard id group. This means that the more replicas we will have, the better GET scaling (扩展)we will have.

####Versioning support

You can use the `version` parameter to retrieve（获取） the document only if its current version is equal to the specified one. This behavior is the same for all version types with the exception of version type `FORCE` which always retrieves the document. Note that `FORCE` version type is deprecated.（只有当文档的当前版本等于指定版本时，才能使用version参数检索（获取）文档。 除了始终检索文档的版本类型FORCE之外，所有版本类型的此行为都相同。 请注意，不推荐使用FORCE版本类型。）

Internally, Elasticsearch has marked the old document as deleted and added an entirely（完全的） new document. The old version of the document doesn’t disappear immediately, although you won’t be able to access it. Elasticsearch cleans up deleted documents in the background as you continue to index more data.

### Delete API

The delete API allows to delete a typed JSON document from a specific index based on its id. The following example deletes the JSON document from an index called `twitter`, under a type called `_doc`, with id `1`:

```
DELETE /twitter/_doc/1
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-delete/1.json)[ ]()

The result of the above delete operation is:

```
{
    "_shards" : {
        "total" : 2,
        "failed" : 0,
        "successful" : 2
    },
    "_index" : "twitter",
    "_type" : "_doc",
    "_id" : "1",
    "_version" : 2,
    "_primary_term": 1,
    "_seq_no": 5,
    "result": "deleted"
}
```

####Optimistic concurrency control(乐观并发控制)

Delete operations can be made optional and only be performed if the last modification to the document was assigned the sequence number and primary term specified by the `if_seq_no` and `if_primary_term` parameters. If a mismatch is detected, the operation will result in a `VersionConflictException` and a status code of 409. See [*Optimistic concurrency control*](https://www.elastic.co/guide/en/elasticsearch/reference/current/optimistic-concurrency-control.html) for more details.

####Versioning

Each document indexed is versioned. When deleting a document, the `version` can be specified to make sure the relevant document we are trying to delete is actually being deleted and it has not changed in the meantime. Every write operation executed on a document, deletes included, causes its version to be incremented. The version number of a deleted document remains available for a short time after deletion to allow for control of concurrent operations. The length of time for which a deleted document’s version remains available is determined by the `index.gc_deletes` index setting and defaults to 60 seconds.（索引的每个文档都是版本化的。 删除文档时，可以指定版本以确保我们尝试删除的相关文档实际上已被删除，并且在此期间没有更改。 对文档执行的每个写入操作（包括删除）都会导致其版本递增。 删除文档的版本号在删除后仍可短时间使用，以便控制并发操作。 已删除文档的版本保持可用的时间长度由index.gc_deletes索引设置确定，默认为60秒。）

####Routing

When indexing using the ability to control the routing, in order to delete a document, the routing value should also be provided. For example:

```
DELETE /twitter/_doc/1?routing=kimchy
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-delete/2.json)[ ]()

The above will delete a tweet with id `1`, but will be routed based on the user. Note, issuing a delete without the correct routing, will cause the document to not be deleted.

When the `_routing` mapping is set as `required` and no routing value is specified, the delete api will throw a `RoutingMissingException` and reject the request.

####Automatic index creation

If an [external versioning variant](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-index_.html) is used, the delete operation automatically creates an index if it has not been created before (check out the [create index API](https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-create-index.html) for manually creating an index), and also automatically creates a dynamic type mapping for the specific type if it has not been created before (check out the [put mapping](https://www.elastic.co/guide/en/elasticsearch/reference/current/indices-put-mapping.html) API for manually creating type mapping).（如果使用外部版本控制变体，则删除操作会自动创建索引（如果之前尚未创建索引）（请参阅创建索引API以手动创建索引），并且还会自动为特定类型创建动态类型映射（如果它 之前尚未创建（请查看put mapping API以手动创建类型映射））

####Distributed

The delete operation gets hashed into a specific shard id. It then gets redirected into the primary shard within that id group, and replicated (if needed) to shard replicas within that id group.

####Wait For Active Shards

When making delete requests, you can set the `wait_for_active_shards` parameter to require a minimum number of shard copies to be active before starting to process the delete request. See[here](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-index_.html#index-wait-for-active-shards) for further details and a usage example.

####Refresh

Control when the changes made by this request are visible to search. See [*?refresh*](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-refresh.html).

####Timeout

The primary shard assigned to perform the delete operation might not be available when the delete operation is executed. Some reasons for this might be that the primary shard is currently recovering from a store or undergoing relocation. By default, the delete operation will wait on the primary shard to become available for up to 1 minute before failing and responding with an error. The `timeout` parameter can be used to explicitly specify how long it waits. Here is an example of setting it to 5 minutes:

```
DELETE /twitter/_doc/1?timeout=5m
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/current/snippets/docs-delete/3.json)[ ]()

### Delete By Query API

The simplest usage of `_delete_by_query` just performs a deletion on every document that match a query. Here is the API:

```json
POST twitter/_delete_by_query
{
  "query": { 
    "match": {
      "message": "some message"
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/1.json)[ ]()

| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/1.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html#CO38-1) | The query must be passed as a value to the `query` key, in the same way as the [Search API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-search.html). You can also use the `q` parameter in the same way as the search api. |
| ---------------------------------------- | ---------------------------------------- |
|                                          |                                          |

That will return something like this:

```json
{
  "took" : 147,
  "timed_out": false,
  "deleted": 119,
  "batches": 1,
  "version_conflicts": 0,
  "noops": 0,
  "retries": {
    "bulk": 0,
    "search": 0
  },
  "throttled_millis": 0,
  "requests_per_second": -1.0,
  "throttled_until_millis": 0,
  "total": 119,
  "failures" : [ ]
}
```

`_delete_by_query` gets a snapshot of the index when it starts and deletes what it finds using `internal` versioning. That means that you’ll get a version conflict if the document changes between the time when the snapshot was taken and when the delete request is processed. When the versions match the document is deleted.

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

Since `internal` versioning does not support the value 0 as a valid version number, documents with version equal to zero cannot be deleted using `_delete_by_query` and will fail the request.

During the `_delete_by_query` execution, multiple（多） search requests are sequentially(顺序的) executed in order to find all the matching documents to delete. Every time a batch of documents is found, a corresponding bulk request is executed to delete all these documents. In case a search or bulk request got rejected, `_delete_by_query` relies on a default policy to retry rejected requests (up to 10 times, with exponential back off). Reaching the maximum retries limit causes the `_delete_by_query`to abort and all failures are returned in the `failures` of the response. The deletions that have been performed still stick. In other words, the process is not rolled back, only aborted. While the first failure causes the abort, all failures that are returned by the failing bulk request are returned in the `failures` element; therefore it’s possible for there to be quite a few failed entities.（在_delete_by_query执行期间，将按顺序执行多个搜索请求，以便查找要删除的所有匹配文档。 每次找到一批文档时，都会执行相应的批量请求以删除所有这些文档。 如果搜索或批量请求被拒绝，_delete_by_query依赖于默认策略来重试被拒绝的请求（最多10次，指数退回）。 达到最大重试次数限制会导致_delete_by_query中止，并且在响应失败时返回所有失败。 已执行的删除仍然有效。 换句话说，该过程不会回滚，只会中止。 当第一个故障导致中止时，失败的批量请求返回的所有故障都将在failure元素中返回; 因此，可能存在相当多的失败实体。）

If you’d like to count version conflicts rather than cause them to abort then set `conflicts=proceed`on the url or `"conflicts": "proceed"` in the request body.

Back to the API format, this will delete tweets from the `twitter` index:

```json
POST twitter/_doc/_delete_by_query?conflicts=proceed
{
  "query": {
    "match_all": {}
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/2.json)[ ]()

It’s also possible to delete documents of multiple indexes and multiple types at once, just like the search API:

```
POST twitter,blog/_docs,post/_delete_by_query
{
  "query": {
    "match_all": {}
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/3.json)[ ]()

If you provide `routing` then the routing is copied to the scroll query, limiting the process to the shards that match that routing value:(如果提供路由，则路由将复制到滚动查询，从而将进程限制为与该路由值匹配的分片)

```json
POST twitter/_delete_by_query?routing=1
{
  "query": {
    "range" : {
        "age" : {
           "gte" : 10
        }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/4.json)[ ]()

By default `_delete_by_query` uses scroll batches of 1000. You can change the batch size with the `scroll_size` URL parameter:

```json
POST twitter/_delete_by_query?scroll_size=5000
{
  "query": {
    "term": {
      "user": "kimchy"
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/5.json)[ ]()

####URL Parameters

In addition to the standard parameters like `pretty`, the Delete By Query API also supports `refresh`, `wait_for_completion`, `wait_for_active_shards`, `timeout` and `scroll`.

Sending the `refresh` will refresh all shards involved in the delete by query once the request completes. This is different than the Delete API’s `refresh` parameter which causes just the shard that received the delete request to be refreshed. Also unlike the Delete API it does not support `wait_for`.

If the request contains `wait_for_completion=false` then Elasticsearch will perform some preflight checks(预检查), launch the request, and then return a `task` which can be used with [Tasks APIs](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html#docs-delete-by-query-task-api) to cancel or get the status of the task. Elasticsearch will also create a record of this task as a document at `.tasks/task/${taskId}`. This is yours to keep or remove as you see fit. When you are done with it, delete it so Elasticsearch can reclaim the space it uses.（如果请求包含wait_for_completion = false，则Elasticsearch将执行一些预检检查，启动请求，然后返回可与Tasks API一起使用的任务，以取消或获取任务的状态。 Elasticsearch还将在.tasks / task / $ {taskId}中创建此任务的记录作为文档。 这是你的保留或删除你认为合适。 完成后，删除它，以便Elasticsearch可以回收它使用的空间。）

`wait_for_active_shards` controls how many copies of a shard must be active before proceeding with the request. See [here](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-wait-for-active-shards) for details. `timeout` controls how long each write request waits for unavailable shards to become available. Both work exactly how they work in the [Bulk API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-bulk.html). As `_delete_by_query`uses scroll search, you can also specify the `scroll` parameter to control how long it keeps the "search context" alive, eg `?scroll=10m`, by default it’s 5 minutes.（wait_for_active_shards控制在继续请求之前必须激活碎片的副本数。 详情请见此处。 timeout指示每个写入请求等待不可用分片可用的时间。 两者都完全适用于Bulk API中的工作方式。 当_delete_by_queryuses滚动搜索时，您还可以指定scroll参数来控制它保持“搜索上下文”活动的时间，例如？scroll = 10m，默认情况下为5分钟。）

`requests_per_second` can be set to any positive decimal number (`1.4`, `6`, `1000`, etc) and throttles rate at which `_delete_by_query` issues batches of delete operations by padding each batch with a wait time. The throttling can be disabled by setting `requests_per_second` to `-1`.

The throttling is done by waiting between batches so that scroll that `_delete_by_query` uses internally can be given a timeout that takes into account the padding. The padding time is the difference between the batch size divided by the `requests_per_second` and the time spent writing. By default the batch size is `1000`, so if the `requests_per_second` is set to `500`:

```
target_time = 1000 / 500 per second = 2 seconds
wait_time = target_time - write_time = 2 seconds - .5 seconds = 1.5 seconds
```

Since the batch is issued as a single `_bulk` request large batch sizes will cause Elasticsearch to create many requests and then wait for a while before starting the next set. This is "bursty" instead of "smooth". The default is `-1`.

####Response body

The JSON response looks like this:

```
{
  "took" : 147,
  "timed_out": false,
  "total": 119,
  "deleted": 119,
  "batches": 1,
  "version_conflicts": 0,
  "noops": 0,
  "retries": {
    "bulk": 0,
    "search": 0
  },
  "throttled_millis": 0,
  "requests_per_second": -1.0,
  "throttled_until_millis": 0,
  "failures" : [ ]
}
```

- `took`

  The number of milliseconds from start to end of the whole operation.

- `timed_out`

  This flag is set to `true` if any of the requests executed during the delete by query execution has timed out.

- `total`

  The number of documents that were successfully processed.

- `deleted`

  The number of documents that were successfully deleted.

- `batches`

  The number of scroll responses pulled back by the delete by query.

- `version_conflicts`

  The number of version conflicts that the delete by query hit.

- `noops`

  This field is always equal to zero for delete by query. It only exists so that delete by query, update by query and reindex APIs return responses with the same structure.

- `retries`

  The number of retries attempted by delete by query. `bulk` is the number of bulk actions retried and `search` is the number of search actions retried.

- `throttled_millis`

  Number of milliseconds the request slept to conform to `requests_per_second`.

- `requests_per_second`

  The number of requests per second effectively executed during the delete by query.

- `throttled_until_millis`

  This field should always be equal to zero in a `_delete_by_query` response. It only has meaning when using the [Task API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html#docs-delete-by-query-task-api), where it indicates the next time (in milliseconds since epoch) a throttled request will be executed again in order to conform to `requests_per_second`.

- `failures`

  Array of failures if there were any unrecoverable errors during the process. If this is non-empty then the request aborted because of those failures. Delete-by-query is implemented using batches and any failure causes the entire process to abort but all failures in the current batch are collected into the array. You can use the `conflicts` option to prevent reindex from aborting on version conflicts.

####Works with the Task API

You can fetch the status of any running delete-by-query requests with the [Task API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html):

```
GET _tasks?detailed=true&actions=*/delete/byquery
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/6.json)[ ]()

The responses looks like:

```
{
  "nodes" : {
    "r1A2WoRbTwKZ516z6NEs5A" : {
      "name" : "r1A2WoR",
      "transport_address" : "127.0.0.1:9300",
      "host" : "127.0.0.1",
      "ip" : "127.0.0.1:9300",
      "attributes" : {
        "testattr" : "test",
        "portsfile" : "true"
      },
      "tasks" : {
        "r1A2WoRbTwKZ516z6NEs5A:36619" : {
          "node" : "r1A2WoRbTwKZ516z6NEs5A",
          "id" : 36619,
          "type" : "transport",
          "action" : "indices:data/write/delete/byquery",
          "status" : {    
            "total" : 6154,
            "updated" : 0,
            "created" : 0,
            "deleted" : 3500,
            "batches" : 36,
            "version_conflicts" : 0,
            "noops" : 0,
            "retries": 0,
            "throttled_millis": 0
          },
          "description" : ""
        }
      }
    }
  }
}
```

| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/1.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html#CO39-1) | this object contains the actual status. It is just like the response json with the important addition of the `total` field. `total` is the total number of operations that the reindex expects to perform. You can estimate the progress by adding the `updated`, `created`, and `deleted` fields. The request will finish when their sum is equal to the `total` field. |
| ---------------------------------------- | ---------------------------------------- |
|                                          |                                          |

With the task id you can look up the task directly:

```
GET /_tasks/r1A2WoRbTwKZ516z6NEs5A:36619
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/7.json)[ ]()

The advantage of this API is that it integrates with `wait_for_completion=false` to transparently return the status of completed tasks. If the task is completed and `wait_for_completion=false` was set on it then it’ll come back with `results` or an `error` field. The cost of this feature is the document that `wait_for_completion=false` creates at `.tasks/task/${taskId}`. It is up to you to delete that document.

####Works with the Cancel Task API

Any Delete By Query can be canceled using the [task cancel API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html):

```
POST _tasks/r1A2WoRbTwKZ516z6NEs5A:36619/_cancel
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/8.json)[ ]()

The task ID can be found using the [tasks API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html).

Cancellation should happen quickly but might take a few seconds. The task status API above will continue to list the delete by query task until this task checks that it has been cancelled and terminates itself.

####Rethrottling

The value of `requests_per_second` can be changed on a running delete by query using the `_rethrottle` API:

```
POST _delete_by_query/r1A2WoRbTwKZ516z6NEs5A:36619/_rethrottle?requests_per_second=-1
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/9.json)[ ]()

The task ID can be found using the [tasks API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html).

Just like when setting it on the `_delete_by_query` API `requests_per_second` can be either `-1` to disable throttling or any decimal number like `1.7` or `12` to throttle to that level. Rethrottling that speeds up the query takes effect immediately but rethrotting that slows down the query will take effect on after completing the current batch. This prevents scroll timeouts.

####Slicing（切片）

Delete-by-query supports [Sliced Scroll](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-request-scroll.html#sliced-scroll) to parallelize（并行） the deleting process. This parallelization can improve efficiency and provide a convenient way to break the request down into smaller parts.

#### Manually slicing

Slice a delete-by-query manually by providing a slice id and total number of slices to each request:

```
POST twitter/_delete_by_query
{
  "slice": {
    "id": 0,
    "max": 2
  },
  "query": {
    "range": {
      "likes": {
        "lt": 10
      }
    }
  }
}
POST twitter/_delete_by_query
{
  "slice": {
    "id": 1,
    "max": 2
  },
  "query": {
    "range": {
      "likes": {
        "lt": 10
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/10.json)[ ]()

Which you can verify works with:

```
GET _refresh
POST twitter/_search?size=0&filter_path=hits.total
{
  "query": {
    "range": {
      "likes": {
        "lt": 10
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/11.json)[ ]()

Which results in a sensible `total` like this one:

```
{
  "hits": {
    "total": 0
  }
}
```

#### Automatic slicing

You can also let delete-by-query automatically parallelize using [Sliced Scroll](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-request-scroll.html#sliced-scroll) to slice on `_uid`. Use `slices` to specify the number of slices to use:

```
POST twitter/_delete_by_query?refresh&slices=5
{
  "query": {
    "range": {
      "likes": {
        "lt": 10
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/12.json)[ ]()

Which you also can verify works with:

```
POST twitter/_search?size=0&filter_path=hits.total
{
  "query": {
    "range": {
      "likes": {
        "lt": 10
      }
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-delete-by-query/13.json)[ ]()

Which results in a sensible `total` like this one:

```
{
  "hits": {
    "total": 0
  }
}
```

Setting `slices` to `auto` will let Elasticsearch choose the number of slices to use. This setting will use one slice per shard, up to a certain limit. If there are multiple source indices, it will choose the number of slices based on the index with the smallest number of shards.

Adding `slices` to `_delete_by_query` just automates the manual process used in the section above, creating sub-requests which means it has some quirks:

- You can see these requests in the [Tasks APIs](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-delete-by-query.html#docs-delete-by-query-task-api). These sub-requests are "child" tasks of the task for the request with `slices`.
- Fetching the status of the task for the request with `slices` only contains the status of completed slices.
- These sub-requests are individually addressable for things like cancellation and rethrottling.
- Rethrottling the request with `slices` will rethrottle the unfinished sub-request proportionally.
- Canceling the request with `slices` will cancel each sub-request.
- Due to the nature of `slices` each sub-request won’t get a perfectly even portion of the documents. All documents will be addressed, but some slices may be larger than others. Expect larger slices to have a more even distribution.
- Parameters like `requests_per_second` and `size` on a request with `slices` are distributed proportionally to each sub-request. Combine that with the point above about distribution being uneven and you should conclude that the using `size` with `slices` might not result in exactly `size` documents being `_delete_by_query`ed.
- Each sub-requests gets a slightly different snapshot of the source index though these are all taken at approximately the same time.

##### Picking the number of slices

If slicing automatically, setting `slices` to `auto` will choose a reasonable number for most indices. If you’re slicing manually or otherwise tuning automatic slicing, use these guidelines.

Query performance is most efficient when the number of `slices` is equal to the number of shards in the index. If that number is large, (for example, 500) choose a lower number as too many `slices` will hurt performance. Setting `slices` higher than the number of shards generally does not improve efficiency and adds overhead.

Delete performance scales linearly across available resources with the number of slices.

Whether query or delete performance dominates the runtime depends on the documents being reindexed and cluster resources.

### Update API

The update API allows to update a document based on a script provided. The operation gets the document (collocated with the shard) from the index, runs the script (with optional script language and parameters), and index back the result (also allows to delete, or ignore the operation). It uses versioning to make sure no updates have happened during the "get" and "reindex".

Note, this operation still means full reindex of the document, it just removes some network roundtrips and reduces chances of version conflicts between the get and the index. The `_source`field needs to be enabled for this feature to work.

For example, let’s index a simple doc:

```
PUT test/_doc/1
{
    "counter" : 1,
    "tags" : ["red"]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/1.json)[ ]()

####Scripted updates

```json
POST test/_doc/1/_update
{
    "script" : {
        "source": "ctx._source.counter += params.count",
        "lang": "painless",
        "params" : {
            "count" : 4
        }
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/2.json)[ ]()

We can add a tag to the list of tags (note, if the tag exists, it will still add it, since it’s a list):

```json
POST test/_doc/1/_update
{
    "script" : {
        "source": "ctx._source.tags.add(params.tag)",
        "lang": "painless",
        "params" : {
            "tag" : "blue"
        }
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/3.json)[ ]()

We can remove a tag from the list of tags. Note that the Painless function to `remove` a tag takes as its parameter the array index of the element you wish to remove, so you need a bit more logic to locate it while avoiding a runtime error. Note that if the tag was present more than once in the list, this will remove only one occurrence of it:

```json
POST test/_doc/1/_update
{
    "script" : {
        "source": "if (ctx._source.tags.contains(params.tag)) { ctx._source.tags.remove(ctx._source.tags.indexOf(params.tag)) }",
        "lang": "painless",
        "params" : {
            "tag" : "blue"
        }
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/4.json)[ ]()

In addition to `_source`, the following variables are available through the `ctx` map: `_index`, `_type`, `_id`, `_version`, `_routing` and `_now` (the current timestamp).

We can also add a new field to the document:

```json
POST test/_doc/1/_update
{
    "script" : "ctx._source.new_field = 'value_of_new_field'"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/5.json)[ ]()

Or remove a field from the document:

```
POST test/_doc/1/_update
{
    "script" : "ctx._source.remove('new_field')"
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/6.json)[ ]()

And, we can even change the operation that is executed. This example deletes the doc if the `tags`field contain `green`, otherwise it does nothing (`noop`):

```
POST test/_doc/1/_update
{
    "script" : {
        "source": "if (ctx._source.tags.contains(params.tag)) { ctx.op = 'delete' } else { ctx.op = 'none' }",
        "lang": "painless",
        "params" : {
            "tag" : "green"
        }
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/7.json)[ ]()

####Updates with a partial document

The update API also support passing a partial document, which will be merged into the existing document (simple recursive merge, inner merging of objects, replacing core "keys/values" and arrays). To fully replace the existing document, the [`index` API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html) should be used instead. The following partial update adds a new field to the existing document:

```
POST test/_doc/1/_update
{
    "doc" : {
        "name" : "new_name"
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/8.json)[ ]()

If both `doc` and `script` are specified, then `doc` is ignored. Best is to put your field pairs of the partial document in the script itself.

####Detecting noop updates

If `doc` is specified its value is merged with the existing `_source`. By default updates that don’t change anything detect that they don’t change anything and return "result": "noop" like this:

```
POST test/_doc/1/_update
{
    "doc" : {
        "name" : "new_name"
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/9.json)[ ]()

If `name` was `new_name` before the request was sent then the entire update request is ignored. The `result` element in the response returns `noop` if the request was ignored.

```
{
   "_shards": {
        "total": 0,
        "successful": 0,
        "failed": 0
   },
   "_index": "test",
   "_type": "_doc",
   "_id": "1",
   "_version": 7,
   "result": "noop"
}
```

You can disable this behavior by setting "detect_noop": false like this:

```
POST test/_doc/1/_update
{
    "doc" : {
        "name" : "new_name"
    },
    "detect_noop": false
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/10.json)[ ]()

####Upserts

If the document does not already exist, the contents of the `upsert` element will be inserted as a new document. If the document does exist, then the `script` will be executed instead:

```
POST test/_doc/1/_update
{
    "script" : {
        "source": "ctx._source.counter += params.count",
        "lang": "painless",
        "params" : {
            "count" : 4
        }
    },
    "upsert" : {
        "counter" : 1
    }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/11.json)[ ]()

#### `scripted_upsert`

If you would like your script to run regardless of whether the document exists or not — i.e. the script handles initializing the document instead of the `upsert` element — then set `scripted_upsert` to `true`:(如果您希望脚本运行，无论文档是否存在 - 即脚本处理初始化文档而不是upsert元素 - 然后将scripted_upsert设置为true)

```
POST sessions/session/dh3sgudg8gsrgl/_update
{
    "scripted_upsert":true,
    "script" : {
        "id": "my_web_session_summariser",
        "params" : {
            "pageViewEvent" : {
                "url":"foo.com/bar",
                "response":404,
                "time":"2014-01-01 12:32"
            }
        }
    },
    "upsert" : {}
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/12.json)[ ]()

#### `doc_as_upsert`

Instead of sending a partial `doc` plus an `upsert` doc, setting `doc_as_upsert` to `true` will use the contents of `doc` as the `upsert` value:

```
POST test/_doc/1/_update
{
    "doc" : {
        "name" : "new_name"
    },
    "doc_as_upsert" : true
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update/13.json)[ ]()

#####Parameters

The update operation supports the following query-string parameters:

| `retry_on_conflict`      | In between the get and indexing phases of the update, it is possible that another process might have already updated the same document. By default, the update will fail with a version conflict exception. The `retry_on_conflict` parameter controls how many times to retry the update before finally throwing an exception. |
| ------------------------ | ---------------------------------------- |
| `routing`                | Routing is used to route the update request to the right shard and sets the routing for the upsert request if the document being updated doesn’t exist. Can’t be used to update the routing of an existing document. |
| `timeout`                | Timeout waiting for a shard to become available. |
| `wait_for_active_shards` | The number of shard copies required to be active before proceeding with the update operation. See [here](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-wait-for-active-shards) for details. |
| `refresh`                | Control when the changes made by this request are visible to search. See [*?refresh*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-refresh.html). |
| `_source`                | Allows to control if and how the updated source should be returned in the response. By default the updated source is not returned. See [`source filtering`](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-request-source-filtering.html) for details. |
| `version`                | The update API uses the Elasticsearch’s versioning support internally to make sure the document doesn’t change during the update. You can use the `version` parameter to specify that the document should only be updated if its version matches the one specified. |

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

####The update API does not support versioning other than internal

External (version types `external` & `external_gte`) or forced (version type `force`) versioning is not supported by the update API as it would result in Elasticsearch version numbers being out of sync with the external system. Use the [`index` API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html) instead.

### Update By Query API

The simplest usage（用法） of `_update_by_query` just performs an update on every document in the index without changing the source. This is useful to [pick up a new property](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#picking-up-a-new-property) or some other online mapping change. Here is the API:

```
POST twitter/_update_by_query?conflicts=proceed
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/1.json)[ ]()

That will return something like this:

```
{
  "took" : 147,
  "timed_out": false,
  "updated": 120,
  "deleted": 0,
  "batches": 1,
  "version_conflicts": 0,
  "noops": 0,
  "retries": {
    "bulk": 0,
    "search": 0
  },
  "throttled_millis": 0,
  "requests_per_second": -1.0,
  "throttled_until_millis": 0,
  "total": 120,
  "failures" : [ ]
}
```

`_update_by_query` gets a snapshot of the index when it starts and indexes what it finds using `internal` versioning. That means that you’ll get a version conflict if the document changes between the time when the snapshot was taken and when the index request is processed. When the versions match the document is updated and the version number is incremented.

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

Since `internal` versioning does not support the value 0 as a valid version number, documents with version equal to zero cannot be updated using `_update_by_query` and will fail the request.

All update and query failures cause the `_update_by_query` to abort and are returned in the `failures`of the response. The updates that have been performed still stick. In other words, the process is not rolled back, only aborted. While the first failure causes the abort, all failures that are returned by the failing bulk request are returned in the `failures` element; therefore it’s possible for there to be quite a few failed entities.

If you want to simply count version conflicts not cause the `_update_by_query` to abort you can set `conflicts=proceed` on the url or `"conflicts": "proceed"` in the request body. The first example does this because it is just trying to pick up an online mapping change and a version conflict simply means that the conflicting document was updated between the start of the `_update_by_query` and the time when it attempted to update the document. This is fine because that update will have picked up the online mapping update.

Back to the API format, this will update tweets from the `twitter` index:

```
POST twitter/_doc/_update_by_query?conflicts=proceed
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/2.json)[ ]()

You can also limit `_update_by_query` using the [Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/query-dsl.html). This will update all documents from the`twitter` index for the user `kimchy`:

```
POST twitter/_update_by_query?conflicts=proceed
{
  "query": { 
    "term": {
      "user": "kimchy"
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/3.json)[ ]()

| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/1.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#CO40-1) | The query must be passed as a value to the `query` key, in the same way as the [Search API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-search.html). You can also use the `q` parameter in the same way as the search api. |
| ---------------------------------------- | ---------------------------------------- |
|                                          |                                          |

So far we’ve only been updating documents without changing their source. That is genuinely useful for things like [picking up new properties](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#picking-up-a-new-property) but it’s only half the fun. `_update_by_query` [supports scripts](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/modules-scripting-using.html)to update the document. This will increment the `likes` field on all of kimchy’s tweets:

```
POST twitter/_update_by_query
{
  "script": {
    "source": "ctx._source.likes++",
    "lang": "painless"
  },
  "query": {
    "term": {
      "user": "kimchy"
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/4.json)[ ]()

Just as in [Update API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update.html) you can set `ctx.op` to change the operation that is executed:

- `noop`

  Set `ctx.op = "noop"` if your script decides that it doesn’t have to make any changes. That will cause `_update_by_query` to omit that document from its updates. This no operation will be reported in the `noop` counter in the [response body](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#docs-update-by-query-response-body).

- `delete`

  Set `ctx.op = "delete"` if your script decides that the document must be deleted. The deletion will be reported in the `deleted` counter in the [response body](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#docs-update-by-query-response-body).

Setting `ctx.op` to anything else is an error. Setting any other field in `ctx` is an error.

Note that we stopped specifying `conflicts=proceed`. In this case we want a version conflict to abort the process so we can handle the failure.

This API doesn’t allow you to move the documents it touches, just modify their source. This is intentional! We’ve made no provisions for removing the document from its original location.

It’s also possible to do this whole thing on multiple indexes and multiple types at once, just like the search API:

```
POST twitter,blog/_doc,post/_update_by_query
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/5.json)[ ]()

If you provide `routing` then the routing is copied to the scroll query, limiting the process to the shards that match that routing value:

```
POST twitter/_update_by_query?routing=1
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/6.json)[ ]()

By default `_update_by_query` uses scroll batches of 1000. You can change the batch size with the `scroll_size` URL parameter:

```
POST twitter/_update_by_query?scroll_size=100
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/7.json)[ ]()

`_update_by_query` can also use the [Ingest Node](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/ingest.html) feature by specifying a `pipeline` like this:

```
PUT _ingest/pipeline/set-foo
{
  "description" : "sets foo",
  "processors" : [ {
      "set" : {
        "field": "foo",
        "value": "bar"
      }
  } ]
}
POST twitter/_update_by_query?pipeline=set-foo
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/8.json)[ ]()

####URL Parameters

In addition to the standard parameters like `pretty`, the Update By Query API also supports `refresh`, `wait_for_completion`, `wait_for_active_shards`, `timeout` and `scroll`.

Sending the `refresh` will update all shards in the index being updated when the request completes. This is different than the Update API’s `refresh` parameter which causes just the shard that received the new data to be indexed. Also unlike the Update API it does not support `wait_for`.

If the request contains `wait_for_completion=false` then Elasticsearch will perform some preflight checks, launch the request, and then return a `task` which can be used with [Tasks APIs](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#docs-update-by-query-task-api) to cancel or get the status of the task. Elasticsearch will also create a record of this task as a document at `.tasks/task/${taskId}`. This is yours to keep or remove as you see fit. When you are done with it, delete it so Elasticsearch can reclaim the space it uses.

`wait_for_active_shards` controls how many copies of a shard must be active before proceeding with the request. See [here](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-wait-for-active-shards) for details. `timeout` controls how long each write request waits for unavailable shards to become available. Both work exactly how they work in the [Bulk API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-bulk.html). As `_update_by_query`uses scroll search, you can also specify the `scroll` parameter to control how long it keeps the "search context" alive, eg `?scroll=10m`, by default it’s 5 minutes.

`requests_per_second` can be set to any positive decimal number (`1.4`, `6`, `1000`, etc) and throttles rate at which `_update_by_query` issues batches of index operations by padding each batch with a wait time. The throttling can be disabled by setting `requests_per_second` to `-1`.

The throttling is done by waiting between batches so that scroll that `_update_by_query` uses internally can be given a timeout that takes into account the padding. The padding time is the difference between the batch size divided by the `requests_per_second` and the time spent writing. By default the batch size is `1000`, so if the `requests_per_second` is set to `500`:

```
target_time = 1000 / 500 per second = 2 seconds
wait_time = target_time - write_time = 2 seconds - .5 seconds = 1.5 seconds
```

Since the batch is issued as a single `_bulk` request large batch sizes will cause Elasticsearch to create many requests and then wait for a while before starting the next set. This is "bursty" instead of "smooth". The default is `-1`.

####Response body

The JSON response looks like this:

```
{
  "took" : 147,
  "timed_out": false,
  "total": 5,
  "updated": 5,
  "deleted": 0,
  "batches": 1,
  "version_conflicts": 0,
  "noops": 0,
  "retries": {
    "bulk": 0,
    "search": 0
  },
  "throttled_millis": 0,
  "requests_per_second": -1.0,
  "throttled_until_millis": 0,
  "failures" : [ ]
}
```

- `took`

  The number of milliseconds from start to end of the whole operation.

- `timed_out`

  This flag is set to `true` if any of the requests executed during the update by query execution has timed out.

- `total`

  The number of documents that were successfully processed.

- `updated`

  The number of documents that were successfully updated.

- `deleted`

  The number of documents that were successfully deleted.

- `batches`

  The number of scroll responses pulled back by the update by query.

- `version_conflicts`

  The number of version conflicts that the update by query hit.

- `noops`

  The number of documents that were ignored because the script used for the update by query returned a `noop` value for `ctx.op`.

- `retries`

  The number of retries attempted by update-by-query. `bulk` is the number of bulk actions retried and `search` is the number of search actions retried.

- `throttled_millis`

  Number of milliseconds the request slept to conform to `requests_per_second`.

- `requests_per_second`

  The number of requests per second effectively executed during the update by query.

- `throttled_until_millis`

  This field should always be equal to zero in an `_update_by_query` response. It only has meaning when using the [Task API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#docs-update-by-query-task-api), where it indicates the next time (in milliseconds since epoch) a throttled request will be executed again in order to conform to `requests_per_second`.

- `failures`

  Array of failures if there were any unrecoverable errors during the process. If this is non-empty then the request aborted because of those failures. Update-by-query is implemented using batches and any failure causes the entire process to abort but all failures in the current batch are collected into the array. You can use the `conflicts` option to prevent reindex from aborting on version conflicts.

####Works with the Task API

You can fetch the status of all running update-by-query requests with the [Task API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html):

```
GET _tasks?detailed=true&actions=*byquery
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/9.json)[ ]()

The responses looks like:

```
{
  "nodes" : {
    "r1A2WoRbTwKZ516z6NEs5A" : {
      "name" : "r1A2WoR",
      "transport_address" : "127.0.0.1:9300",
      "host" : "127.0.0.1",
      "ip" : "127.0.0.1:9300",
      "attributes" : {
        "testattr" : "test",
        "portsfile" : "true"
      },
      "tasks" : {
        "r1A2WoRbTwKZ516z6NEs5A:36619" : {
          "node" : "r1A2WoRbTwKZ516z6NEs5A",
          "id" : 36619,
          "type" : "transport",
          "action" : "indices:data/write/update/byquery",
          "status" : {    
            "total" : 6154,
            "updated" : 3500,
            "created" : 0,
            "deleted" : 0,
            "batches" : 4,
            "version_conflicts" : 0,
            "noops" : 0,
            "retries": {
              "bulk": 0,
              "search": 0
            },
            "throttled_millis": 0
          },
          "description" : ""
        }
      }
    }
  }
}
```

| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/1.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#CO41-1) | this object contains the actual status. It is just like the response json with the important addition of the `total` field. `total` is the total number of operations that the reindex expects to perform. You can estimate the progress by adding the `updated`, `created`, and `deleted` fields. The request will finish when their sum is equal to the `total` field. |
| ---------------------------------------- | ---------------------------------------- |
|                                          |                                          |

With the task id you can look up the task directly. The following example retrieves information about task `r1A2WoRbTwKZ516z6NEs5A:36619`:

```
GET /_tasks/r1A2WoRbTwKZ516z6NEs5A:36619
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/10.json)[ ]()

The advantage of this API is that it integrates with `wait_for_completion=false` to transparently return the status of completed tasks. If the task is completed and `wait_for_completion=false` was set on it them it’ll come back with a `results` or an `error` field. The cost of this feature is the document that `wait_for_completion=false` creates at `.tasks/task/${taskId}`. It is up to you to delete that document.

####Works with the Cancel Task API

Any Update By Query can be canceled using the [Task Cancel API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html):

```
POST _tasks/r1A2WoRbTwKZ516z6NEs5A:36619/_cancel
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/11.json)[ ]()

The task ID can be found using the [tasks API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html).

Cancellation should happen quickly but might take a few seconds. The task status API above will continue to list the update by query task until this task checks that it has been cancelled and terminates itself.

####Rethrottling

The value of `requests_per_second` can be changed on a running update by query using the `_rethrottle` API:

```
POST _update_by_query/r1A2WoRbTwKZ516z6NEs5A:36619/_rethrottle?requests_per_second=-1
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/12.json)[ ]()

The task ID can be found using the [tasks API](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/tasks.html).

Just like when setting it on the `_update_by_query` API `requests_per_second` can be either `-1` to disable throttling or any decimal number like `1.7` or `12` to throttle to that level. Rethrottling that speeds up the query takes effect immediately but rethrotting that slows down the query will take effect on after completing the current batch. This prevents scroll timeouts.

####Slicing

Update-by-query supports [Sliced Scroll](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-request-scroll.html#sliced-scroll) to parallelize the updating process. This parallelization can improve efficiency and provide a convenient way to break the request down into smaller parts.

#### Manual slicing

Slice an update-by-query manually by providing a slice id and total number of slices to each request:

```
POST twitter/_update_by_query
{
  "slice": {
    "id": 0,
    "max": 2
  },
  "script": {
    "source": "ctx._source['extra'] = 'test'"
  }
}
POST twitter/_update_by_query
{
  "slice": {
    "id": 1,
    "max": 2
  },
  "script": {
    "source": "ctx._source['extra'] = 'test'"
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/13.json)[ ]()

Which you can verify works with:

```
GET _refresh
POST twitter/_search?size=0&q=extra:test&filter_path=hits.total
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/14.json)[ ]()

Which results in a sensible `total` like this one:

```
{
  "hits": {
    "total": 120
  }
}
```

#### Automatic slicing

You can also let update-by-query automatically parallelize using [Sliced Scroll](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/search-request-scroll.html#sliced-scroll) to slice on `_uid`. Use `slices` to specify the number of slices to use:

```
POST twitter/_update_by_query?refresh&slices=5
{
  "script": {
    "source": "ctx._source['extra'] = 'test'"
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/15.json)[ ]()

Which you also can verify works with:

```
POST twitter/_search?size=0&q=extra:test&filter_path=hits.total
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/16.json)[ ]()

Which results in a sensible `total` like this one:

```
{
  "hits": {
    "total": 120
  }
}
```

Setting `slices` to `auto` will let Elasticsearch choose the number of slices to use. This setting will use one slice per shard, up to a certain limit. If there are multiple source indices, it will choose the number of slices based on the index with the smallest number of shards.

Adding `slices` to `_update_by_query` just automates the manual process used in the section above, creating sub-requests which means it has some quirks:

- You can see these requests in the [Tasks APIs](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#docs-update-by-query-task-api). These sub-requests are "child" tasks of the task for the request with `slices`.
- Fetching the status of the task for the request with `slices` only contains the status of completed slices.
- These sub-requests are individually addressable for things like cancellation and rethrottling.
- Rethrottling the request with `slices` will rethrottle the unfinished sub-request proportionally.
- Canceling the request with `slices` will cancel each sub-request.
- Due to the nature of `slices` each sub-request won’t get a perfectly even portion of the documents. All documents will be addressed, but some slices may be larger than others. Expect larger slices to have a more even distribution.
- Parameters like `requests_per_second` and `size` on a request with `slices` are distributed proportionally to each sub-request. Combine that with the point above about distribution being uneven and you should conclude that the using `size` with `slices` might not result in exactly `size` documents being `_update_by_query`ed.
- Each sub-requests gets a slightly different snapshot of the source index though these are all taken at approximately the same time.

##### Picking the number of slices

If slicing automatically, setting `slices` to `auto` will choose a reasonable number for most indices. If you’re slicing manually or otherwise tuning automatic slicing, use these guidelines.

Query performance is most efficient when the number of `slices` is equal to the number of shards in the index. If that number is large, (for example, 500) choose a lower number as too many `slices` will hurt performance. Setting `slices` higher than the number of shards generally does not improve efficiency and adds overhead.

Update performance scales linearly across available resources with the number of slices.

Whether query or update performance dominates the runtime depends on the documents being reindexed and cluster resources.

####Pick up a new property

Say you created an index without dynamic mapping, filled it with data, and then added a mapping value to pick up more fields from the data:

```
PUT test
{
  "mappings": {
    "_doc": {
      "dynamic": false,   
      "properties": {
        "text": {"type": "text"}
      }
    }
  }
}

POST test/_doc?refresh
{
  "text": "words words",
  "flag": "bar"
}
POST test/_doc?refresh
{
  "text": "words words",
  "flag": "foo"
}
PUT test/_mapping/_doc   
{
  "properties": {
    "text": {"type": "text"},
    "flag": {"type": "text", "analyzer": "keyword"}
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/17.json)[ ]()

| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/1.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#CO42-1) | This means that new fields won’t be indexed, just stored in `_source`. |
| ---------------------------------------- | ---------------------------------------- |
| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/2.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-update-by-query.html#CO42-2) | This updates the mapping to add the new `flag` field. To pick up the new field you have to reindex all documents with it. |

Searching for the data won’t find anything:

```
POST test/_search?filter_path=hits.total
{
  "query": {
    "match": {
      "flag": "foo"
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/18.json)[ ]()

```
{
  "hits" : {
    "total" : 0
  }
}
```

But you can issue an `_update_by_query` request to pick up the new mapping:

```
POST test/_update_by_query?refresh&conflicts=proceed
POST test/_search?filter_path=hits.total
{
  "query": {
    "match": {
      "flag": "foo"
    }
  }
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-update-by-query/19.json)[ ]()

```
{
  "hits" : {
    "total" : 1
  }
}
```

You can do the exact same thing when adding a field to a multifield.

### Multi Get API

Multi GET API allows to get multiple documents based on an index, type (optional) and id (and possibly routing). The response includes a `docs` array with all the fetched documents in order corresponding to the original multi-get request (if there was a failure for a specific get, an object containing this error is included in place in the response instead). The structure of a successful get is similar in structure to a document provided by the [get](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-get.html) API.

Here is an example:

```
GET /_mget
{
    "docs" : [
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "1"
        },
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "2"
        }
    ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/1.json)[ ]()

The `mget` endpoint can also be used against an index (in which case it is not required in the body):

```
GET /test/_mget
{
    "docs" : [
        {
            "_type" : "_doc",
            "_id" : "1"
        },
        {
            "_type" : "_doc",
            "_id" : "2"
        }
    ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/2.json)[ ]()

And type:

```
GET /test/_doc/_mget
{
    "docs" : [
        {
            "_id" : "1"
        },
        {
            "_id" : "2"
        }
    ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/3.json)[ ]()

In which case, the `ids` element can directly be used to simplify the request:

```
GET /test/_doc/_mget
{
    "ids" : ["1", "2"]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/4.json)[ ]()

####Source filtering

By default, the `_source` field will be returned for every document (if stored). Similar to the [get](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-get.html#get-source-filtering) API, you can retrieve only parts of the `_source` (or not at all) by using the `_source` parameter. You can also use the url parameters `_source`,`_source_includes` & `_source_excludes` to specify defaults, which will be used when there are no per-document instructions.

For example:

```json
GET /_mget
{
    "docs" : [
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "1",
            "_source" : false
        },
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "2",
            "_source" : ["field3", "field4"]
        },
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "3",
            "_source" : {
                "include": ["user"],
                "exclude": ["user.location"]
            }
        }
    ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/5.json)[ ]()

####Fields

Specific stored fields can be specified to be retrieved per document to get, similar to the [stored_fields](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-get.html#get-stored-fields) parameter of the Get API. For example:

```
GET /_mget
{
    "docs" : [
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "1",
            "stored_fields" : ["field1", "field2"]
        },
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "2",
            "stored_fields" : ["field3", "field4"]
        }
    ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/6.json)[ ]()

Alternatively(作为选择), you can specify the `stored_fields` parameter in the query string as a default to be applied to all documents.

```
GET /test/_doc/_mget?stored_fields=field1,field2
{
    "docs" : [
        {
            "_id" : "1" 
        },
        {
            "_id" : "2",
            "stored_fields" : ["field3", "field4"] 
        }
    ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/7.json)[ ]()

| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/1.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-multi-get.html#CO43-1) | Returns `field1` and `field2` |
| ---------------------------------------- | ----------------------------- |
| [![img](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/callouts/2.png)](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-multi-get.html#CO43-2) | Returns `field3` and `field4` |

####Routing

You can also specify routing value as a parameter:

```
GET /_mget?routing=key1
{
    "docs" : [
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "1",
            "routing" : "key2"
        },
        {
            "_index" : "test",
            "_type" : "_doc",
            "_id" : "2"
        }
    ]
}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-multi-get/8.json)[ ]()

In this example, document `test/_doc/2` will be fetch from shard corresponding to routing key `key1`but document `test/_doc/1` will be fetch from shard corresponding to routing key `key2`.

####Security

See [*URL-based access control*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/url-access-control.html)

### Bulk API

The bulk API makes it possible to perform many index/delete operations in a single API call. This can greatly increase the indexing speed.

**Client support for bulk requests**

Some of the officially supported clients provide helpers to assist with bulk requests and reindexing of documents from one index to another:

- Perl

  See [Search::Elasticsearch::Client::5_0::Bulk](https://metacpan.org/pod/Search::Elasticsearch::Client::5_0::Bulk) and [Search::Elasticsearch::Client::5_0::Scroll](https://metacpan.org/pod/Search::Elasticsearch::Client::5_0::Scroll)

- Python

  See [elasticsearch.helpers.*](http://elasticsearch-py.readthedocs.org/en/master/helpers.html)

The REST API endpoint is `/_bulk`, and it expects the following newline delimited JSON (NDJSON) structure:

```
action_and_meta_data\n
optional_source\n
action_and_meta_data\n
optional_source\n
....
action_and_meta_data\n
optional_source\n
```

**NOTE**: the final line of data must end with a newline character `\n`. Each newline character may be preceded by a carriage return `\r`. When sending requests to this endpoint the `Content-Type` header should be set to `application/x-ndjson`.

The possible actions are `index`, `create`, `delete` and `update`. `index` and `create` expect a source on the next line, and have the same semantics(语义) as the `op_type` parameter to the standard index API (i.e. create will fail if a document with the same index and type exists already, whereas index will add or replace a document as necessary). `delete` does not expect a source on the following line, and has the same semantics as the standard delete API. `update` expects that the partial doc, upsert and script and its options are specified on the next line.

If you’re providing text file input to `curl`, you **must** use the `--data-binary` flag instead of plain `-d`. The latter doesn’t preserve newlines. Example:

```
$ cat requests
{ "index" : { "_index" : "test", "_type" : "_doc", "_id" : "1" } }
{ "field1" : "value1" }
$ curl -s -H "Content-Type: application/x-ndjson" -XPOST localhost:9200/_bulk --data-binary "@requests"; echo
{"took":7, "errors": false, "items":[{"index":{"_index":"test","_type":"_doc","_id":"1","_version":1,"result":"created","forced_refresh":false}}]}
```

Because this format uses literal `\n`'s as delimiters, please be sure that the JSON actions and sources are not pretty printed. Here is an example of a correct sequence of bulk commands:

```
POST _bulk
{ "index" : { "_index" : "test", "_type" : "_doc", "_id" : "1" } }
{ "field1" : "value1" }
{ "delete" : { "_index" : "test", "_type" : "_doc", "_id" : "2" } }
{ "create" : { "_index" : "test", "_type" : "_doc", "_id" : "3" } }
{ "field1" : "value3" }
{ "update" : {"_id" : "1", "_type" : "_doc", "_index" : "test"} }
{ "doc" : {"field2" : "value2"} }
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-bulk/1.json)[ ]()

The result of this bulk operation is:

```
{
   "took": 30,
   "errors": false,
   "items": [
      {
         "index": {
            "_index": "test",
            "_type": "_doc",
            "_id": "1",
            "_version": 1,
            "result": "created",
            "_shards": {
               "total": 2,
               "successful": 1,
               "failed": 0
            },
            "status": 201,
            "_seq_no" : 0,
            "_primary_term": 1
         }
      },
      {
         "delete": {
            "_index": "test",
            "_type": "_doc",
            "_id": "2",
            "_version": 1,
            "result": "not_found",
            "_shards": {
               "total": 2,
               "successful": 1,
               "failed": 0
            },
            "status": 404,
            "_seq_no" : 1,
            "_primary_term" : 2
         }
      },
      {
         "create": {
            "_index": "test",
            "_type": "_doc",
            "_id": "3",
            "_version": 1,
            "result": "created",
            "_shards": {
               "total": 2,
               "successful": 1,
               "failed": 0
            },
            "status": 201,
            "_seq_no" : 2,
            "_primary_term" : 3
         }
      },
      {
         "update": {
            "_index": "test",
            "_type": "_doc",
            "_id": "1",
            "_version": 2,
            "result": "updated",
            "_shards": {
                "total": 2,
                "successful": 1,
                "failed": 0
            },
            "status": 200,
            "_seq_no" : 3,
            "_primary_term" : 4
         }
      }
   ]
}
```

The endpoints are `/_bulk`, `/{index}/_bulk`, and `{index}/{type}/_bulk`. When the index or the index/type are provided, they will be used by default on bulk items that don’t provide them explicitly.

A note on the format. The idea here is to make processing of this as fast as possible. As some of the actions will be redirected to other shards on other nodes, only `action_meta_data` is parsed on the receiving node side.

Client libraries using this protocol should try and strive to do something similar on the client side, and reduce buffering as much as possible.

The response to a bulk action is a large JSON structure with the individual（单独的） results of each action that was performed in the same order as the actions that appeared in the request. The failure of a single action does not affect the remaining actions.

There is no "correct" number of actions to perform in a single bulk call. You should experiment with different settings to find the optimum（最适宜的） size for your particular workload.

If using the HTTP API, make sure that the client does not send HTTP chunks, as this will slow things down.

####Optimistic Concurrency Control

Each `index` and `delete` action within a bulk API call may include the `if_seq_no` and `if_primary_term`parameters in their respective action and meta data lines. The `if_seq_no` and `if_primary_term`parameters control how operations are executed, based on the last modification to existing documents. See [*Optimistic concurrency control*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/optimistic-concurrency-control.html) for more details.

####Versioning

Each bulk item can include the version value using the `version` field. It automatically follows the behavior of the index / delete operation based on the `_version` mapping. It also support the `version_type` (see [versioning](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-versioning))

####Routing

Each bulk item can include the routing value using the `routing` field. It automatically follows the behavior of the index / delete operation based on the `_routing` mapping.

####Wait For Active Shards

When making bulk calls, you can set the `wait_for_active_shards` parameter to require a minimum number of shard copies to be active before starting to process the bulk request. See [here](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-index_.html#index-wait-for-active-shards) for further details and a usage example.

####Refresh

Control when the changes made by this request are visible to search. See [refresh](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/docs-refresh.html).

![Note](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/images/icons/note.png)

Only the shards that receive the bulk request will be affected by `refresh`. Imagine a `_bulk?refresh=wait_for` request with three documents in it that happen to be routed to different shards in an index with five shards. The request will only wait for those three shards to refresh. The other two shards of that make up the index do not participate(参与) in the `_bulk` request at all.

####Update

When using `update` action `retry_on_conflict` can be used as field in the action itself (not in the extra payload line), to specify how many times an update should be retried in the case of a version conflict.

The `update` action payload, supports the following options: `doc` (partial document), `upsert`, `doc_as_upsert`, `script`, `params` (for script), `lang` (for script) and `_source`. See update documentation for details on the options. Example with update actions:

```json
POST _bulk
{ "update" : {"_id" : "1", "_type" : "_doc", "_index" : "index1", "retry_on_conflict" : 3} }
{ "doc" : {"field" : "value"} }
{ "update" : { "_id" : "0", "_type" : "_doc", "_index" : "index1", "retry_on_conflict" : 3} }
{ "script" : { "source": "ctx._source.counter += params.param1", "lang" : "painless", "params" : {"param1" : 1}}, "upsert" : {"counter" : 1}}
{ "update" : {"_id" : "2", "_type" : "_doc", "_index" : "index1", "retry_on_conflict" : 3} }
{ "doc" : {"field" : "value"}, "doc_as_upsert" : true }
{ "update" : {"_id" : "3", "_type" : "_doc", "_index" : "index1", "_source" : true} }
{ "doc" : {"field" : "value"} }
{ "update" : {"_id" : "4", "_type" : "_doc", "_index" : "index1"} }
{ "doc" : {"field" : "value"}, "_source": true}
```

[COPY AS CURL]()[VIEW IN CONSOLE](http://localhost:5601/app/kibana#/dev_tools/console?load_from=https://www.elastic.co/guide/en/elasticsearch/reference/6.6/snippets/docs-bulk/2.json)[ ]()

####Security

See [*URL-based access control*](https://www.elastic.co/guide/en/elasticsearch/reference/6.6/url-access-control.html)