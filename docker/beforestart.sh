#!/usr/bin/env bash

# 项目根目录
cd ../

# 构建jar文件
echo "构建后台程序jar文件"
mvn package spring-boot:repackage
cp target/brand-report-backend-0.0.1-SNAPSHOT.jar docker/backend/

# 复制初始数据
echo "复制初始数据库文件"
mkdir -p docker/mongo/init/data
cp -a data/. docker/mongo/init/data