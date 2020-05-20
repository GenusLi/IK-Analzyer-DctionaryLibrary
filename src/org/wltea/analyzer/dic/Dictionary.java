/**
 * IK ���ķִ�  �汾 5.0
 * IK Analyzer release 5.0
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Դ������������(linliangyi2005@gmail.com)�ṩ
 * ��Ȩ���� 2012�������蹤����
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 * 
 */
/**
 * ��ԭ��һ�ʵ䱾�޸�Ϊ�ʵ��
 * �ʵ�����÷�ʽ
 * ʵ��org.wltea.analyzer.cfg.Configuration�ӿ�
 * ���*.cfg.xml�����ļ�ָ���ʵ䱾λ�á�ֹͣ�ʴʵ䱾λ�á����ʴʵ䱾λ�ã��������Ȳ��
 * */
package org.wltea.analyzer.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wltea.analyzer.cfg.Configuration;

/**
 * �ʵ������,����ģʽ
 */
public class Dictionary {

	// �ʵ��
	private static final Map<String, Dictionary> Dictionary_Library = new HashMap<String, Dictionary>();

//	/*
//	 * �ʵ䵥��ʵ��
//	 */
//	private static Dictionary singleton;

	/*
	 * ���ʵ����
	 */
	private DictSegment _MainDict;

	/*
	 * ֹͣ�ʴʵ�
	 */
	private DictSegment _StopWordDict;
	/*
	 * ���ʴʵ�
	 */
	private DictSegment _QuantifierDict;

	/**
	 * ���ö���
	 */
	private Configuration cfg;

	private Dictionary(Configuration cfg) {
		this.cfg = cfg;
		this.loadMainDict(); // �������ʵ�
		this.loadStopWordDict(); // ����ֹͣ�ʵ�
		this.loadQuantifierDict(); // �������ʴʵ�
	}

//	/**
//	 * �ʵ��ʼ��
//	 * ����IK Analyzer�Ĵʵ����Dictionary��ľ�̬�������дʵ��ʼ��
//	 * ֻ�е�Dictionary�౻ʵ�ʵ���ʱ���ŻῪʼ����ʵ䣬
//	 * �⽫�ӳ��״ηִʲ�����ʱ��
//	 * �÷����ṩ��һ����Ӧ�ü��ؽ׶ξͳ�ʼ���ֵ���ֶ�
//	 * @return Dictionary
//	 */
//	public static Dictionary initial(Configuration cfg){
//		if(singleton == null){
//			synchronized(Dictionary.class){
//				if(singleton == null){
//					singleton = new Dictionary(cfg);
//					return singleton;
//				}
//			}
//		}
//		return singleton;
//	}

	/**
	 * �ʵ��ʼ�� ����IK Analyzer�Ĵʵ����Dictionary��ľ�̬�������дʵ��ʼ��
	 * ֻ�е�Dictionary�౻ʵ�ʵ���ʱ���ŻῪʼ����ʵ䣬 �⽫�ӳ��״ηִʲ�����ʱ�� �÷����ṩ��һ����Ӧ�ü��ؽ׶ξͳ�ʼ���ֵ���ֶ�
	 * 
	 * @return Dictionary
	 */
	public static Dictionary initial(Configuration cfg) {
		if (Dictionary_Library.get(cfg.getDictionaryName()) == null) {
			synchronized (Dictionary.class) {
				if (Dictionary_Library.get(cfg.getDictionaryName()) == null) {
					Dictionary_Library.put(cfg.getDictionaryName(), new Dictionary(cfg));
					return Dictionary_Library.get(cfg.getDictionaryName());
				}
			}
		}
		return Dictionary_Library.get(cfg.getDictionaryName());
	}


//	/**
//	 * ��ȡ�ʵ䵥��ʵ��
//	 * 
//	 * @return Dictionary ��������
//	 */
//	public static Dictionary getSingleton() {
//		if (singleton == null) {
//			throw new IllegalStateException("�ʵ���δ��ʼ�������ȵ���initial����");
//		}
//		return singleton;
//	}

	/**
	 * ����������Ӵʵ���ȡ�ʵ䵥��ʵ��
	 */
	public static Dictionary getSingleton(Configuration config) {
		return Dictionary_Library.get(config.getDictionaryName());
	}
	
	/**
	 * ���������´���
	 * 
	 * @param words Collection<String>�����б�
	 */
	public void addWords(Collection<String> words) {
		if (words != null) {
			for (String word : words) {
				if (word != null) {
					// �������ش��������ڴ�ʵ���
//					singleton._MainDict.fillSegment(word.trim().toLowerCase().toCharArray());
					_MainDict.fillSegment(word.trim().toLowerCase().toCharArray());
				}
			}
		}
	}

	/**
	 * �����Ƴ������Σ�����
	 * 
	 * @param words
	 */
	public void disableWords(Collection<String> words) {
		if (words != null) {
			for (String word : words) {
				if (word != null) {
					// �������δ���
//					singleton._MainDict.disableSegment(word.trim().toLowerCase().toCharArray());
					_MainDict.disableSegment(word.trim().toLowerCase().toCharArray());
				}
			}
		}
	}

	/**
	 * ����ƥ�����ʵ�
	 * 
	 * @param charArray
	 * @return Hit ƥ��������
	 */
	public Hit matchInMainDict(char[] charArray) {
//		return singleton._MainDict.match(charArray);
		return _MainDict.match(charArray);
	}

	/**
	 * ����ƥ�����ʵ�
	 * 
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public Hit matchInMainDict(char[] charArray, int begin, int length) {
//		return singleton._MainDict.match(charArray, begin, length);
		return _MainDict.match(charArray, begin, length);
	}

	/**
	 * ����ƥ�����ʴʵ�
	 * 
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return Hit ƥ��������
	 */
	public Hit matchInQuantifierDict(char[] charArray, int begin, int length) {
//		return singleton._QuantifierDict.match(charArray, begin, length);
		return _QuantifierDict.match(charArray, begin, length);
	}

	/**
	 * ����ƥ���Hit��ֱ��ȡ��DictSegment����������ƥ��
	 * 
	 * @param charArray
	 * @param currentIndex
	 * @param matchedHit
	 * @return Hit
	 */
	public Hit matchWithHit(char[] charArray, int currentIndex, Hit matchedHit) {
		DictSegment ds = matchedHit.getMatchedDictSegment();
		return ds.match(charArray, currentIndex, 1, matchedHit);
	}

	/**
	 * �ж��Ƿ���ֹͣ��
	 * 
	 * @param charArray
	 * @param begin
	 * @param length
	 * @return boolean
	 */
	public boolean isStopWord(char[] charArray, int begin, int length) {
//		return singleton._StopWordDict.match(charArray, begin, length).isMatch();
		return _StopWordDict.match(charArray, begin, length).isMatch();
	}

	/**
	 * �������ʵ估��չ�ʵ�
	 */
	private void loadMainDict() {
		// ����һ�����ʵ�ʵ��
		_MainDict = new DictSegment((char) 0);
		// ��ȡ���ʵ��ļ�
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(cfg.getMainDictionary());
		if (is == null) {
			throw new RuntimeException("Main Dictionary not found!!!");
		}

		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
				}
			} while (theWord != null);

		} catch (IOException ioe) {
			System.err.println("Main Dictionary loading exception.");
			ioe.printStackTrace();

		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ������չ�ʵ�
		this.loadExtDict();
	}

	/**
	 * �����û����õ���չ�ʵ䵽���ʿ��
	 */
	private void loadExtDict() {
		// ������չ�ʵ�����
		List<String> extDictFiles = cfg.getExtDictionarys();
		if (extDictFiles != null) {
			InputStream is = null;
			for (String extDictName : extDictFiles) {
				// ��ȡ��չ�ʵ��ļ�
				System.out.println("������չ�ʵ䣺" + extDictName);
				is = this.getClass().getClassLoader().getResourceAsStream(extDictName);
				// ����Ҳ�����չ���ֵ䣬�����
				if (is == null) {
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							// ������չ�ʵ����ݵ����ڴ�ʵ���
							// System.out.println(theWord);
							_MainDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
						}
					} while (theWord != null);

				} catch (IOException ioe) {
					System.err.println("Extension Dictionary loading exception.");
					ioe.printStackTrace();

				} finally {
					try {
						if (is != null) {
							is.close();
							is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * �����û���չ��ֹͣ�ʴʵ�
	 */
	private void loadStopWordDict() {
		// ����һ�����ʵ�ʵ��
		_StopWordDict = new DictSegment((char) 0);
		// ������չֹͣ�ʵ�
		List<String> extStopWordDictFiles = cfg.getExtStopWordDictionarys();
		if (extStopWordDictFiles != null) {
			InputStream is = null;
			for (String extStopWordDictName : extStopWordDictFiles) {
				System.out.println("������չֹͣ�ʵ䣺" + extStopWordDictName);
				// ��ȡ��չ�ʵ��ļ�
				is = this.getClass().getClassLoader().getResourceAsStream(extStopWordDictName);
				// ����Ҳ�����չ���ֵ䣬�����
				if (is == null) {
					continue;
				}
				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"), 512);
					String theWord = null;
					do {
						theWord = br.readLine();
						if (theWord != null && !"".equals(theWord.trim())) {
							// System.out.println(theWord);
							// ������չֹͣ�ʵ����ݵ��ڴ���
							_StopWordDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
						}
					} while (theWord != null);

				} catch (IOException ioe) {
					System.err.println("Extension Stop word Dictionary loading exception.");
					ioe.printStackTrace();

				} finally {
					try {
						if (is != null) {
							is.close();
							is = null;
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * �������ʴʵ�
	 */
	private void loadQuantifierDict() {
		// ����һ�����ʵ�ʵ��
		_QuantifierDict = new DictSegment((char) 0);
		// ��ȡ���ʴʵ��ļ�
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(cfg.getQuantifierDicionary());
		if (is == null) {
			throw new RuntimeException("Quantifier Dictionary not found!!!");
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"), 512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					_QuantifierDict.fillSegment(theWord.trim().toLowerCase().toCharArray());
				}
			} while (theWord != null);

		} catch (IOException ioe) {
			System.err.println("Quantifier Dictionary loading exception.");
			ioe.printStackTrace();

		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
