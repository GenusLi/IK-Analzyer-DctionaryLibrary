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
 * �ַ���ʶ�𹤾���
 */
package org.wltea.analyzer.core;

/**
 *
 * �ַ���ʶ�𹤾���
 */
class CharacterUtil {
	
	public static final int CHAR_USELESS = 0;
	
	public static final int CHAR_ARABIC = 0X00000001;
	
	public static final int CHAR_ENGLISH = 0X00000002;
	
	public static final int CHAR_CHINESE = 0X00000004;
	
	public static final int CHAR_OTHER_CJK = 0X00000008;
	
	
	/**
	 * ʶ���ַ�����
	 * @param input
	 * @return int CharacterUtil������ַ����ͳ���
	 */
	static int identifyCharType(char input){
		if(input >= '0' && input <= '9'){
			return CHAR_ARABIC;
			
		}else if((input >= 'a' && input <= 'z')
				|| (input >= 'A' && input <= 'Z')){
			return CHAR_ENGLISH;
			
		}else {
			Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);
			
			if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
					|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
					|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A){
				//Ŀǰ��֪�������ַ�UTF-8����
				return CHAR_CHINESE;
				
			}else if(ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS //ȫ�������ַ����պ��ַ�
					//�����ַ���
					|| ub == Character.UnicodeBlock.HANGUL_SYLLABLES 
					|| ub == Character.UnicodeBlock.HANGUL_JAMO
					|| ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
					//�����ַ���
					|| ub == Character.UnicodeBlock.HIRAGANA //ƽ����
					|| ub == Character.UnicodeBlock.KATAKANA //Ƭ����
					|| ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS){
				return CHAR_OTHER_CJK;
				
			}
		}
		//�����Ĳ���������ַ�
		return CHAR_USELESS;
	}
	
	/**
	 * �����ַ���񻯣�ȫ��ת��ǣ���дתСд����
	 * @param input
	 * @return char
	 */
	static char regularize(char input){
        if (input == 12288) {
            input = (char) 32;
            
        }else if (input > 65280 && input < 65375) {
            input = (char) (input - 65248);
            
        }else if (input >= 'A' && input <= 'Z') {
        	input += 32;
		}
        
        return input;
	}
}
