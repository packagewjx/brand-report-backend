#!/usr/bin/env bash

# 项目根目录
cd ../

# 构建jar文件
echo "构建后台程序jar文件"
mvn package -DskipTests spring-boot:repackage
cp target/brand-report-backend-0.0.1-SNAPSHOT.jar docker/backend/
cp -r data/ docker/backend/