## 搜索docker镜像
docker search mongo

## 拉取mongo镜像
docker pull mongo

## 创建mongodb容器实例
docker run -d -p 27017:27017 -e MONGO_ROOT_PASSWORD=zhengdongfa --name mongo_brand mongo:latest

## 进入容器中的mongodb，写入示例文档，方便测试是否能够连接上mongodb
docker exec -it 2c7af95ee8b0 mongo admin
``` 
> show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
> use test
switched to db test
> db
test
> show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
> db.test.insert({"name":"zdf"})
WriteResult({ "nInserted" : 1 })
> db.test.insert({"name":"zhengdongfa"})
WriteResult({ "nInserted" : 1 })
> show dbs
admin   0.000GB
config  0.000GB
local   0.000GB
test    0.000GB
> use runoob
switched to db runoob
> db
runoob
> db.runoob.insert({"name":"zhengdongfa"})
WriteResult({ "nInserted" : 1 })
> db.runoob.insert({"name":"zdf"})
WriteResult({ "nInserted" : 1 })
```