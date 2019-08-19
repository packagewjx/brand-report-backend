#!/usr/bin/env bash

init=""

function initDatabase() {
if [ ${INDEX_FILE} ]
then
    init="${init} --indexFileName=${INDEX_FILE}"
fi

if [ ${COLLECTION_FILE} ]
then
    init="${init} --collectionFileName=${COLLECTION_FILE}"
fi

if [ ${BRAND_FILE} ]
then
    init="${init} --brandFileName=${BRAND_FILE}"
fi

if [ "$init" == "" ]
then
    echo "不初始化数据库"
else
    echo "初始化数据库"
    init="--initializeDatabase=true ${init}"
    java -jar /tmp/brand-report-backend-0.0.1-SNAPSHOT.jar ${init}
    # 如果检查到这个文件，则不会初始化
    touch /inited
fi
}

# 控制仅在初次启动时尝试初始化数据库
if [ ! -f /inited ]
then
    initDatabase
fi

echo "正在启动品牌管理系统后端程序"
java -jar /tmp/brand-report-backend-0.0.1-SNAPSHOT.jar