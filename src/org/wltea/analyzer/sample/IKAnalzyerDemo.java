/**
 * IK ���ķִ�  �汾 5.0.1
 * IK Analyzer release 5.0.1
 * <p>
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Դ������������(linliangyi2005@gmail.com)�ṩ
 * ��Ȩ���� 2012�������蹤����
 * provided by Linliangyi and copyright 2012 by Oolong studio
 */
package org.wltea.analyzer.sample;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.cfg.CityExtDicConfig;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DistExtDicConfig;
import org.wltea.analyzer.cfg.ProvExtDicConfig;
import org.wltea.analyzer.lucene.IKAnalyzerExt;

/**
 * ʹ��IKAnalyzer-DictionaryLibrarys���зִʵ���ʾ
 * 2020-05-20
 *
 */
public class IKAnalzyerDemo {

    public static void main(String[] args) {
    	String str="���ɹ�ǰ������˹�ɹ��������ر�����²����Ӵ�ǰ����������";
        //����IK�ִ�����ʹ��smart�ִ�ģʽ
    	
    	//ʡ���ʵ�ִ�
    	Configuration prov_config=ProvExtDicConfig.getInstance();
//        Analyzer prov_analyzer = new IKAnalyzerExt(prov_config,true,false);
        String prov=analysis(str,prov_config);
        if(prov.length()>1) {
        	System.out.println(prov);
        	str=str.replaceFirst(prov, "");
        }
    	
        
        //�м��ʵ�ִ�
    	Configuration city_config=CityExtDicConfig.getInstance();
    	String city=analysis(str,city_config);
    	if(city.length()>1) {
    		System.out.println(city);
    		str=str.replaceFirst(city, "");
    	}
    	
        //�ؼ��ʵ�ִ�
    	Configuration dist_config=DistExtDicConfig.getInstance();
    	System.out.println(analysis(str,dist_config));
    	str=str.replaceFirst(analysis(str,dist_config), "");
    	System.out.println(str);
        
    }
    public static String analysis(String str,Configuration config) {
    	Analyzer analyzer = new IKAnalyzerExt(config,true,false);
    	//��ȡLucene��TokenStream����
        TokenStream ts = null;
        String result="";
        try {
            ts = analyzer.tokenStream("", new StringReader(str));
            //��ȡ��Ԫλ������
            OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
            //��ȡ��Ԫ�ı�����
            CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
            //��ȡ��Ԫ�ı�����
            TypeAttribute type = ts.addAttribute(TypeAttribute.class);
            PositionIncrementAttribute pos = ts.addAttribute(PositionIncrementAttribute.class);

            //����TokenStream������StringReader��
            ts.reset();
            //������ȡ�ִʽ��
            while (ts.incrementToken()) {
            	if(result.equals("")) {
            		result=term.toString();
            		break;
            	}
            }
            //�ر�TokenStream���ر�StringReader��
            ts.end();   // Perform end-of-stream operations, e.g. set the final offset.
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //�ͷ�TokenStream��������Դ
            if (ts != null) {
                try {
                    ts.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
    
}
