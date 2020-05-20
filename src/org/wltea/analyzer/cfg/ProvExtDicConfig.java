package org.wltea.analyzer.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * 省级地域配置实现
 *
 */
public class ProvExtDicConfig implements Configuration {

	//配置文件路径
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
	
	private ProvExtDicConfig() {
		props = new Properties();
		
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
	@Override
	public String getDictionaryName() {
		return "province";
	}

}
