# 品牌报告系统后台

## 初始化

在执行文件的时候，加入如下的命令，以初始化数据库

```
--initializeDatabase=true [--indexFileName=<index.json路径>] [--collectionFileName=<collection.json路径>] [--brandFileName=<brand.json路径>]
```

每个json文件均是一行一个json对象字符串，且该字符串即为对应类对象序列化的字符串。初始化程序将试图读取这个json并序列化为相应的对象，添加额外的数据后，导入数据库中。