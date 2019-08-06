#!/usr/bin/env bash

mongoimport --db brand_report --collection brand --file /docker-entrypoint-initdb.d/data/brand.json
mongoimport --db brand_report --collection collection --file /docker-entrypoint-initdb.d/data/collection.json
mongoimport --db brand_report --collection index --file /docker-entrypoint-initdb.d/data/index.json