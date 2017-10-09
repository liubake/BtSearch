本程序为BT爬虫，可以爬取DHT网络中其它节点存在的种子信息。



模块介绍：

BtSearch.Util   辅助的工具类<br/>
BtSearch.Model   数据模型层

BtSearch.Dao   数据访问层

BtSearch.Service   业务逻辑层

BtSearch.Spider   BT爬虫

BtSearch.Web   种子展示和搜索页面（功能暂未实现，之后会借助 ElasticSearch 实现搜索功能）



开发工具：IDEA



爬虫运行环境 JDK8+、Redis、MongoDB



DHT协议参考：

1、http://www.bittorrent.org/beps/bep_0005.html

2、http://blog.csdn.net/xxxxxx91116/article/details/7970815



PeerWire协议参考：

1、http://blog.chinaunix.net/uid-24399976-id-3060019.html

2、http://www.aneasystone.com/archives/2015/05/analyze-magnet-protocol-using-wireshark.html



其它说明：

由于内网穿透的原因，爬虫运行时需要您的网络有外网IP，否则很难获取到其它节点的下载种子的通知，判断运营商分配给您的是不是外网IP方法，查看您的路由器分配到的IP地址是不是和通过搜索引擎查询到的自己的IP一致，如果一致的话在路由器中设置对应的端口和爬虫所在电脑的监听端口进行绑定即可。
