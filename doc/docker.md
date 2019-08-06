# Docker部署文档

## 环境配置

安装docker与docker-compose程序

#### （非必须）拉取镜像

拉取我们需要使用的两个镜像

```
docker pull openjdk:8-jre
docker pull mongo:4.0
```

## 部署方法

### 初始化

在部署之前，需要执行docker文件夹下的`beforestart.sh`脚本文件。

注意，需要工作目录切换到docker文件夹，否则会出错。

脚本将会构建程序，并复制项目根目录中`data`的内容，以初始化数据库。

#### 数据库初始化

系统初始化时，将使用项目根目录`data`文件夹下的文件初始化数据，所需要的文件有：

- `brand.json`：品牌数据文件
- `collection.json`：品牌指标数据文件
- `index.json`：指标元数据文件

注意，上述json文件都需要是能让`mongoimport`程序使用的json文件，具体格式是一行一个json对象。

### 构建

```
docker-compose build
```

将会构建镜像

### 运行

在docker文件夹中（下列命令均需要在这个文件夹执行），执行命令

```
docker-compose up -d
```

程序将会在后台执行。

若之前没有构建镜像，则会自动构建

#### 日志查看

使用如下命令查看日志信息

```
docker-compose logs <服务名>
```

服务名可以是

- brand-report-backend
- mongo

### 停止

```
docker-compose stop
```

### 删除

```
docker-compose down
```

将会停止并删除所有服务的容器



## 日志查看

日志文件会输出到容器内的/var/log/message/brand-report-backend文件夹，而docker-compose的配置则将该文件夹挂载到了backend/logs内，可以查看该文件夹的日志文件，也可以在`docker-compose.yml`中修改为需要的文件夹。

## 数据库

数据库默认挂载到`mongo/db`文件夹中，因此只要存在文件，则能够一直使用该数据库，无论是创建多少次。