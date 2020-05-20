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
package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeFactory;
import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;
import java.io.Reader;

/**
 * IK�ִ��� Lucene Tokenizer��������
 * <p>
 * ����Lucene 6.6.0 �汾
 * <p>
 * Modified by ������ on 2017/8/29.
 */
public final class IKTokenizer extends Tokenizer {

    //IK�ִ���ʵ��
    private IKSegmenter _IKImplement;

    //��Ԫ�ı�����
    private final CharTermAttribute termAtt;
    //��Ԫλ������
    private final OffsetAttribute offsetAtt;
    //��Ԫ�������ԣ������Է���ο�org.wltea.analyzer.core.Lexeme�еķ��ೣ����
    private final TypeAttribute typeAtt;
    //��¼���һ����Ԫ�Ľ���λ��
    private int endPosition;

    /**
     * Lucene 6.6.0 Tokenizer�������๹�캯��
     * <p>
     * IK����������
     *
     * @param useSmart
     */
    public IKTokenizer(boolean useSmart) {
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input, useSmart);
    }

    /**
     * Lucene 6.6.0 Tokenizer�������๹�캯��
     * <p>
     * �ִ�����������
     *
     * @param factory
     * @param useSmart
     */
    public IKTokenizer(AttributeFactory factory, boolean useSmart) {
        super(factory);
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input, useSmart); //�ֶ������췽���е���init�������شʵ�
    }

    /**
     * ֧���Զ������õĹ�����
     */
    public IKTokenizer(Configuration config) {
        offsetAtt = addAttribute(OffsetAttribute.class);
        termAtt = addAttribute(CharTermAttribute.class);
        typeAtt = addAttribute(TypeAttribute.class);
        _IKImplement = new IKSegmenter(input, config);
    }
    

    /* (non-Javadoc)
     * @see org.apache.lucene.analysis.TokenStream#incrementToken()
     */
    @Override
    public boolean incrementToken() throws IOException {
        //������еĴ�Ԫ����
        clearAttributes();
        Lexeme nextLexeme = _IKImplement.next();
        if (nextLexeme != null) {
            //��Lexemeת��Attributes
            //���ô�Ԫ�ı�
            termAtt.append(nextLexeme.getLexemeText());
            //���ô�Ԫ����
            termAtt.setLength(nextLexeme.getLength());
            //���ô�Ԫλ��
            offsetAtt.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());
            //��¼�ִʵ����λ��
            endPosition = nextLexeme.getEndPosition();
            //��¼��Ԫ����
            typeAtt.setType(nextLexeme.getLexemeTypeString());
            //����true��֪�����¸���Ԫ
            return true;
        }
        //����false��֪��Ԫ������
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.lucene.analysis.Tokenizer#reset(java.io.Reader)
     */
    @Override
    public void reset() throws IOException {
        super.reset();
        _IKImplement.reset(input);
    }

    @Override
    public final void end() {
        // set final offset
        int finalOffset = correctOffset(this.endPosition);
        offsetAtt.setOffset(finalOffset, finalOffset);
    }
}
