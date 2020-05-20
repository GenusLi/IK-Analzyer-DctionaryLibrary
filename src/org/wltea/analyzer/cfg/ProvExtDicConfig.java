package org.wltea.analyzer.cfg;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

/**
 * ʡ����������ʵ��
 *
 */
public class ProvExtDicConfig implements Configuration {

	//�����ļ�·��
	private static final String FILE_NAME="CustomExtProv.cfg.xml";
	//�����ļ�������
	private Properties props;
	//key-���ʵ�·��
	private static final String EXT_DICT="ext_dict";
	//key-ֹͣ�ʵ�·��
	private static final String EXT_STOP="ext_stopwords";
	//key-���ʴʵ�·��
	private static final String EXT_QUANTIFIER="ext_quantifier";
	private static final String USE_SMART="use_smart";
	private boolean useSmart=true;
	public boolean getUseSmart() {
		return this.useSmart;
	}
	/**
	 * ���ص���
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
					System.out.println("��ȡ"+FILE_NAME+"�ļ���use_smart����ʧ�ܣ���Ĭ��Ϊtrue");
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
	 * ��ȡ���ʵ�
	 */
	@Override
	public String getMainDictionary() {
		String main=props.getProperty(EXT_DICT);
		return props.getProperty(EXT_DICT);
	}

	/**
	 * ��ȡ���ʴʵ�
	 */
	@Override
	public String getQuantifierDicionary() {
		return props.getProperty(EXT_QUANTIFIER);
	}

	/**
	 * ��ȡ��չ�ʵ�
	 */
	@Override
	public List<String> getExtDictionarys() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * ��ȡֹͣ�ʵ�
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
