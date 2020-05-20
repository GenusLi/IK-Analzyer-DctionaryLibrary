
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
 */
package org.wltea.analyzer.core;

import java.util.LinkedList;
import java.util.List;

import org.wltea.analyzer.dic.Dictionary;
import org.wltea.analyzer.dic.Hit;


/**
 *  ����-�պ����ӷִ���
 */
class CJKSegmenter implements ISegmenter {
	
	//�ӷִ�����ǩ
	static final String SEGMENTER_NAME = "CJK_SEGMENTER";
	//������ķִ�hit����
	private List<Hit> tmpHits;
	
	
	CJKSegmenter(){
		this.tmpHits = new LinkedList<Hit>();
	}

	/* (non-Javadoc)
	 * @see org.wltea.analyzer.core.ISegmenter#analyze(org.wltea.analyzer.core.AnalyzeContext)
	 */
	public void analyze(AnalyzeContext context) {
		if(CharacterUtil.CHAR_USELESS != context.getCurrentCharType()){
			
			//���ȴ���tmpHits�е�hit
			if(!this.tmpHits.isEmpty()){
				//����ʶζ���
				Hit[] tmpArray = this.tmpHits.toArray(new Hit[this.tmpHits.size()]);
				for(Hit hit : tmpArray){
					hit = Dictionary.getSingleton(context.getConfig()).matchWithHit(context.getSegmentBuff(), context.getCursor() , hit);
					if(hit.isMatch()){
						//�����ǰ�Ĵ�
						Lexeme newLexeme = new Lexeme(context.getBufferOffset() , hit.getBegin() , context.getCursor() - hit.getBegin() + 1 , Lexeme.TYPE_CNWORD);
						context.addLexeme(newLexeme);
						
						if(!hit.isPrefix()){//���Ǵ�ǰ׺��hit����Ҫ����ƥ�䣬�Ƴ�
							this.tmpHits.remove(hit);
						}
						
					}else if(hit.isUnmatch()){
						//hit���Ǵʣ��Ƴ�
						this.tmpHits.remove(hit);
					}					
				}
			}			
			
			//*********************************
			//�ٶԵ�ǰָ��λ�õ��ַ����е���ƥ��
			Hit singleCharHit = Dictionary.getSingleton(context.getConfig()).matchInMainDict(context.getSegmentBuff(), context.getCursor(), 1);
			if(singleCharHit.isMatch()){//���ֳɴ�
				//�����ǰ�Ĵ�
				Lexeme newLexeme = new Lexeme(context.getBufferOffset() , context.getCursor() , 1 , Lexeme.TYPE_CNWORD);
				context.addLexeme(newLexeme);

				//ͬʱҲ�Ǵ�ǰ׺
				if(singleCharHit.isPrefix()){
					//ǰ׺ƥ�������hit�б�
					this.tmpHits.add(singleCharHit);
				}
			}else if(singleCharHit.isPrefix()){//����Ϊ��ǰ׺
				//ǰ׺ƥ�������hit�б�
				this.tmpHits.add(singleCharHit);
			}
			

		}else{
			//����CHAR_USELESS�ַ�
			//��ն���
			this.tmpHits.clear();
		}
		
		//�жϻ������Ƿ��Ѿ�����
		if(context.isBufferConsumed()){
			//��ն���
			this.tmpHits.clear();
		}
		
		//�ж��Ƿ�����������
		if(this.tmpHits.size() == 0){
			context.unlockBuffer(SEGMENTER_NAME);
			
		}else{
			context.lockBuffer(SEGMENTER_NAME);
		}
	}

	/* (non-Javadoc)
	 * @see org.wltea.analyzer.core.ISegmenter#reset()
	 */
	public void reset() {
		//��ն���
		this.tmpHits.clear();
	}

}
