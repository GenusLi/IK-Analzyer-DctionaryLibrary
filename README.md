# IK-Analzyer-DctionaryLibrary简介

1. IK分词器的修改版本，添加词典库，支持多个独立词典。
2. 根据项目需求，对IK分词官方版本进行修改，可以在不同业务场景下使用不同的词典进行分词。已确保得到的terms中的第一个元素，一定为符合本次业务场景的词汇。
3. IK-Analzyer-DictionaryLibrary基于IKAnalyzer2017_6_6_0版本进行修改，需要依赖lucene-analyzers-common-6.6.0、lucene-core-6.6.0、lucene-queryparser-6.6.0

### 修改内容
1. 添加自定义词库功能，可以创建配置类对象来使用不同的词典
2. 将自定义词库的useSmart属性移动到XML配置文件中（原生默认配置类保持不变，仍需要在构造分词器时传入参数）

### 下面是使用自定义词典库的demo
#### 自定义配置类 ProvExtDicConfig.java
```java
/**
 * 省级地域配置实现
 *
 */
public class ProvExtDicConfig implements Configuration {

	//配置文件路径，路径为项目的resource文件夹，可以在构造方法中修改路径位置
	private static final String FILE_NAME="CustomExtProv.cfg.xml";
	//配置文件上下文
	private Properties props;
	//key-主词典路径
	private static final String EXT_DICT="ext_dict";
	//key-停止词典路径
	private static final String EXT_STOP="ext_stopwords";
	//key-量词词典路径
	private static final String EXT_QUANTIFIER="ext_quantifier";
	private static final String USE_SMART="use_smart";
	private boolean useSmart=true;
	public boolean getUseSmart() {
		return this.useSmart;
	}
	/**
	 * 返回单例
	 */
	public static Configuration getInstance() {
		return new ProvExtDicConfig();
	}
	
  /**
  * 词典在词典库中的名称，注意应保持词典名称的唯一性，否则可能会出现词典不被加载或者词典被覆盖问题
  */
  @Override
	public String getDictionaryName() {
		return "province";
	}
  
	private ProvExtDicConfig() {
		props = new Properties();
		
    //此处可以修改要读取的配置文件位置
		InputStream input = this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
		if(input != null){
			try {
				props.loadFromXML(input);
				try{
					useSmart=Boolean.getBoolean(props.getProperty(USE_SMART));
				}catch (Exception e) {
					System.out.println("读取"+FILE_NAME+"文件，use_smart属性失败，已默认为true");
				}
				
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean useSmart() {
		return useSmart;
	}

	@Override
	public void setUseSmart(boolean useSmart) {
		this.useSmart=useSmart;
		
	}

	/**
	 * 获取主词典
	 */
	@Override
	public String getMainDictionary() {
		String main=props.getProperty(EXT_DICT);
		return props.getProperty(EXT_DICT);
	}

	/**
	 * 获取量词词典
	 */
	@Override
	public String getQuantifierDicionary() {
		return props.getProperty(EXT_QUANTIFIER);
	}

	/**
	 * 获取扩展词典
	 */
	@Override
	public List<String> getExtDictionarys() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取停止词典
	 */
	@Override
	public List<String> getExtStopWordDictionarys() {
		List<String> list=new ArrayList<String>();
		list.add(props.getProperty(EXT_STOP));
		return list;
	}

}
```
#### 自定义配置xml文件 CustomExtProv.cfg.xml
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">  
<properties>
	<!-- 词典组件名称 -->
	<comment>自定义省级地域分词扩展配置</comment>
	<!--用户可以在这里配置自己的省级地域扩展字典 -->
	<entry key="ext_dict">province.dic</entry> 
	
	<!--用户可以在这里配置量词字典，已默认为IK内置量词词典 -->
	<entry key="ext_quantifier">org/wltea/analyzer/dic/quantifier.dic</entry>
	
	<!--用户可以在这里配置扩展停止词字典，取值为IK默认停止词典路径-->
	<entry key="ext_stopwords">stopword.dic</entry> 
	
	<!--用户可以在这里配置智能分词（true粗粒度,false细粒度）-->
	<entry key="use_smart">true</entry> 
	
</properties>
```
#### 词典文件部分内容 province.dic
```
重庆市
四川省
湖北省
湖南省
广东省
北京市
天津市
河北省
山西省
```
