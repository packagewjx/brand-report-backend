# 计分系统设计

## Index注解设计

**注解在系统中的定义是Map<String, String>**，因此下面所有的设置都需要是字符串类型。如果是一个对象，则改为Json字符串，且为显示正常，需要在一行内定义完毕。

### 指定计分类型

计分类型使用`score_type`指定，根据目前实现的计分逻辑，拥有如下几种取值

- `bool`: 布尔型数据计分逻辑
- `enum`: 枚举型数据计分逻辑
- `ratio`: 根据品牌的指标数据占该指标行业统计的总值的比值计算分数
- `step`: 阶梯式算分，给定不同的分数区间，计算分数
- `multiply`: 乘法式给分，根据数据值，乘以给定的基准分数，取得最后的分数
- `score-ratio`: 指标是比例型，如百分比，最终给分是总分乘以指标的值得出

### 计分类型额外注解

#### bool

|注解名|描述|值类型|
|-----|-----|----|
|`score_bool_true-score`|值为true时得到的分数|数字|
|`score_bool_false-score`|值为false时得到的分数|数字|

#### enum

|注解名|描述|值类型|
|-----|-----|----|
|`score_enum_score-definition`|分数的定义|EnumScoreDefinition类|

##### EnumScoreDefinition

json样例

```json
{"definition":{"世界先进水平":100,"国内先进水平":80,"行业先进水平":60,"其他":30}}
```

#### ratio

|注解名|描述|值类型|
|-----|-----|----|
|`score_ratio_total-score`|总分，最终得分是总分乘以比值|数字|

#### step

|注解名|描述|值类型|
|-----|-----|----|
|`score_step_score-definition`|分数定义|StepScoreDefinition类|

##### StepScoreDefinition

json样例

```json
{"intervalSplit":[1000,2000],"intervalScore":[30,60,100]}
```

#### multiply

|注解名|描述|值类型|
|-----|-----|----|
|`score_multiply_multiplier`|乘法基准分。最终得分是指标的值乘以这个基准分|数字|

#### score-ratio

|注解名|描述|值类型|
|-----|-----|----|
|`score_score_ratio_total_score`|比例型分数的满分，若值是1，则拿满分，否则按比例乘以总分得到|数字|