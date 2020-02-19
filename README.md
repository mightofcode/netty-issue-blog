# yinwangblog
yinwangblog让你拥有一个跟王垠一样的博客

基于GitHub issue博文、评论  
使用GitHub api V4读取issue，每分钟进行一次全量更新  
基于netty的静态文件服务器，不存在seo问题，单机QPS1000  

## 1, 下载release中的发布版本
解压后目录结构如下：  
├── data             静态文件目录  
├── config.json       配置文件  
├── start.sh          启动脚本  
├── start.bat            启动脚本        
├── yinwangblog-0.jar   jar包  

## 2，配置config.json

```
{
  "githubToken": "",    //github token，用与访问github api，获取方式:https://github.com/settings/tokens
  "serverIp": "0.0.0.0",   //服务器ip
  "serverPort": 9701,      //服务器端口
  "siteRoot": "http://127.0.0.1:9701",  //http服务器网址
  "githubName": "mightofcode",       //github name   
  "githubRepo": "blog",              //github 仓库   会从这个仓库的issues读取blog
  "blogName": "我没有在扯淡",       //博客名
  "links": [                      //首页右上角的链接
    {
      "title": "微博",
      "href": "https://www.baidu.com/"
    },
    {
      "title": "付费",
      "href": "https://www.baidu.com/"
    },
    {
      "title": "联系",
      "href": "https://www.baidu.com/"
    }
  ]
}

```
## 3,启动服务器
```
java -jar yinwangblog-0.jar config.json
```
或者直接  ./start.sh  

## 4,通过浏览器访问博客

你可以修改data下面的静态文件来调整网站样式






