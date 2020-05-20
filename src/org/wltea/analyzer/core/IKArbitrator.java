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

import java.util.Stack;
import java.util.TreeSet;

/**
 * IK�ִ�����þ���
 */
class IKArbitrator {

	IKArbitrator(){
		
	}
	
	/**
	 * �ִ����崦��
	 * @param orgLexemes
	 * @param useSmart
	 */
	void process(AnalyzeContext context , boolean useSmart){
		QuickSortSet orgLexemes = context.getOrgLexemes();
		Lexeme orgLexeme = orgLexemes.pollFirst();
		
		LexemePath crossPath = new LexemePath();
		while(orgLexeme != null){
			if(!crossPath.addCrossLexeme(orgLexeme)){
				//�ҵ���crossPath���ཻ����һ��crossPath	
				if(crossPath.size() == 1 || !useSmart){
					//crossPathû������ ���� �������崦��
					//ֱ�������ǰcrossPath
					context.addLexemePath(crossPath);
				}else{
					//�Ե�ǰ��crossPath�������崦��
					QuickSortSet.Cell headCell = crossPath.getHead();
					LexemePath judgeResult = this.judge(headCell, crossPath.getPathLength());
					//������崦����judgeResult
					context.addLexemePath(judgeResult);
				}
				
				//��orgLexeme�����µ�crossPath��
				crossPath = new LexemePath();
				crossPath.addCrossLexeme(orgLexeme);
			}
			orgLexeme = orgLexemes.pollFirst();
		}
		
		
		//��������path
		if(crossPath.size() == 1 || !useSmart){
			//crossPathû������ ���� �������崦��
			//ֱ�������ǰcrossPath
			context.addLexemePath(crossPath);
		}else{
			//�Ե�ǰ��crossPath�������崦��
			QuickSortSet.Cell headCell = crossPath.getHead();
			LexemePath judgeResult = this.judge(headCell, crossPath.getPathLength());
			//������崦����judgeResult
			context.addLexemePath(judgeResult);
		}
	}
	
	/**
	 * ����ʶ��
	 * @param lexemeCell ����·������ͷ
	 * @param fullTextLength ����·���ı�����
	 * @param option ��ѡ���·��
	 * @return
	 */
	private LexemePath judge(QuickSortSet.Cell lexemeCell , int fullTextLength){
		//��ѡ·������
		TreeSet<LexemePath> pathOptions = new TreeSet<LexemePath>();
		//��ѡ���·��
		LexemePath option = new LexemePath();
		
		//��crossPath����һ�α���,ͬʱ���ر��α������г�ͻ��Lexemeջ
		Stack<QuickSortSet.Cell> lexemeStack = this.forwardPath(lexemeCell , option);
		
		//��ǰ��Ԫ������������ģ������ѡ·������
		pathOptions.add(option.copy());
		
		//��������ʣ�����
		QuickSortSet.Cell c = null;
		while(!lexemeStack.isEmpty()){
			c = lexemeStack.pop();
			//�ع���Ԫ��
			this.backPath(c.getLexeme() , option);
			//�������λ�ÿ�ʼ���ݹ飬���ɿ�ѡ����
			this.forwardPath(c , option);
			pathOptions.add(option.copy());
		}
		
		//���ؼ����е����ŷ���
		return pathOptions.first();

	}
	
	/**
	 * ��ǰ��������Ӵ�Ԫ������һ���������Ԫ���
	 * @param LexemePath path
	 * @return
	 */
	private Stack<QuickSortSet.Cell> forwardPath(QuickSortSet.Cell lexemeCell , LexemePath option){
		//������ͻ��Lexemeջ
		Stack<QuickSortSet.Cell> conflictStack = new Stack<QuickSortSet.Cell>();
		QuickSortSet.Cell c = lexemeCell;
		//��������Lexeme����
		while(c != null && c.getLexeme() != null){
			if(!option.addNotCrossLexeme(c.getLexeme())){
				//��Ԫ���棬���ʧ�������lexemeStackջ
				conflictStack.push(c);
			}
			c = c.getNext();
		}
		return conflictStack;
	}
	
	/**
	 * �ع���Ԫ����ֱ�����ܹ�����ָ���Ĵ�Ԫ
	 * @param lexeme 
	 * @param l
	 */
	private void backPath(Lexeme l  , LexemePath option){
		while(option.checkCross(l)){
			option.removeTail();
		}
		
	}
	
}
