# 计分系统设计

## Index注解设计

### 指定计分类型

计分类型使用`score.type`指定，根据目前实现的计分逻辑，拥有如下几种取值

- `bool`: 布尔型数据计分逻辑
- `enum`: 枚举型数据计分逻辑
- `ratio`: 根据品牌的指标数据占该指标总值的比值计算分数
- `step`: 阶梯式算分，给定不同的分数区间，计算分数
- `multiply`: 乘法式给分，根据数据值，乘以给定的基准分数，取得最后的分数

### 计分类型额外注解

#### bool

|注解名|描述|值类型|
|-----|-----|----|
|`score.bool.true-score`|值为true时得到的分数|数字|
|`score.bool.false-score`|值为false时得到的分数|数字|

#### enum


|注解名|描述|值类型|
|-----|-----|----|
|`score.enum.score-definition|分数的定义|EnumScoreDefinition类|

#### ratio

|注解名|描述|值类型|
|-----|-----|----|
|`score.ratio.total-score`|总分，最终得分是总分乘以比值|数字|

#### step

|注解名|描述|值类型|
|-----|-----|----|
|`score.step.score-definition`|分数定义|StepScoreDefinition类|

#### multiply

|注解名|描述|值类型|
|-----|-----|----|
|`score.multiply.multiplier`|乘法基准分。最终得分是指标的值乘以这个基准分|数字|