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
 */
package org.wltea.analyzer.core;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.dic.Dictionary;

/**
 * IK�ִ�������
 *
 */
public final class IKSegmenter {
	
	//�ַ���reader
	private Reader input;
	//�ִ���������
	private Configuration cfg;
	//�ִ���������
	private AnalyzeContext context;
	//�ִʴ������б�
	private List<ISegmenter> segmenters;
	//�ִ�����þ���
	private IKArbitrator arbitrator;
	

	/**
	 * IK�ִ������캯��
	 * @param input 
	 * @param useSmart Ϊtrue��ʹ�����ִܷʲ���
	 * 
	 * �����ִܷʣ�ϸ����������п��ܵ��зֽ��
	 * ���ִܷʣ� �ϲ����ʺ����ʣ��Էִʽ�����������ж�
	 */
	public IKSegmenter(Reader input , boolean useSmart){
		this.input = input;
		this.cfg = DefaultConfig.getInstance();
		this.cfg.setUseSmart(useSmart);
		this.init();
	}

	
	/**
	 * IK�ִ������캯��
	 * @param input
	 * @param cfg ʹ���Զ����Configuration����ִ���
	 * 
	 */
	public IKSegmenter(Reader input , Configuration cfg){
		this.input = input;
		this.cfg = cfg;
		this.init();
	}
	
	/**
	 * ��ʼ��
	 */
	private void init(){
		//��ʼ���ʵ䵥��
		Dictionary.initial(this.cfg);
		//��ʼ���ִ�������
		this.context = new AnalyzeContext(this.cfg);
		//�����ӷִ���
		this.segmenters = this.loadSegmenters();
		//��������þ���
		this.arbitrator = new IKArbitrator();
	}
	
	/**
	 * ��ʼ���ʵ䣬�����ӷִ���ʵ��
	 * @return List<ISegmenter>
	 */
	private List<ISegmenter> loadSegmenters(){
		List<ISegmenter> segmenters = new ArrayList<ISegmenter>(4);
		//������ĸ���ӷִ���
		segmenters.add(new LetterSegmenter()); 
		//�������������ʵ��ӷִ���
		segmenters.add(new CN_QuantifierSegmenter());
		//�������Ĵʵ��ӷִ���
		segmenters.add(new CJKSegmenter());
		return segmenters;
	}
	
	/**
	 * �ִʣ���ȡ��һ����Ԫ
	 * @return Lexeme ��Ԫ����
	 * @throws IOException
	 */
	public synchronized Lexeme next()throws IOException{
		Lexeme l = null;
		while((l = context.getNextLexeme()) == null ){
			/*
			 * ��reader�ж�ȡ���ݣ����buffer
			 * ���reader�Ƿִζ���buffer�ģ���ôbufferҪ  ������λ����
			 * ��λ�����ϴζ���ĵ�δ���������
			 */
			int available = context.fillBuffer(this.input);
			if(available <= 0){
				//reader�Ѿ�����
				context.reset();
				return null;
				
			}else{
				//��ʼ��ָ��
				context.initCursor();
				do{
        			//�����ӷִ���
        			for(ISegmenter segmenter : segmenters){
        				segmenter.analyze(context);
        			}
        			//�ַ��������ӽ����꣬��Ҫ�����µ��ַ�
        			if(context.needRefillBuffer()){
        				break;
        			}
   				//��ǰ�ƶ�ָ��
				}while(context.moveCursor());
				//�����ӷִ�����Ϊ����ѭ�����г�ʼ��
				for(ISegmenter segmenter : segmenters){
					segmenter.reset();
				}
			}
			//�Էִʽ������崦��
			this.arbitrator.process(context, this.cfg.useSmart());			
			//���ִʽ��������������������δ�зֵĵ���CJK�ַ�
			context.outputToResult();
			//��¼���ηִʵĻ�����λ��
			context.markBufferOffset();			
		}
		return l;
	}

	/**
     * ���÷ִ�������ʼ״̬
     * @param input
     */
	public synchronized void reset(Reader input) {
		this.input = input;
		context.reset();
		for(ISegmenter segmenter : segmenters){
			segmenter.reset();
		}
	}
}
