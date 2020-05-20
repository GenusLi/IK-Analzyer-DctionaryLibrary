# IK-Analzyer-DctionaryLibrary简介

1. IK分词器的修改版本，添加词典库，支持多个独立词典。
2. 根据项目需求，对IK分词官方版本进行修改，可以在不同业务场景下使用不同的词典进行分词。已确保得到的terms中的第一个元素，一定为符合本次业务场景的词汇。
3. IK-Analzyer-DictionaryLibrary基于IKAnalyzer2017_6_6_0版本进行修改，需要依赖lucene-analyzers-common-6.6.0、lucene-core-6.6.0、lucene-queryparser-6.6.0

### 修改内容
1. 添加自定义词库功能，可以创建配置类对象来使用不同的词典
2. 将自定义词库的useSmart属性移动到XML配置文件中（原生默认配置类保持不变，仍需要在构造分词器时传入参数）



