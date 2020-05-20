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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;
import org.wltea.analyzer.cfg.Configuration;

/**
 * IK��������Lucene Analyzer�ӿ�ʵ��
 * <p>
 * ����Lucene 6.6.0 �汾
 * <p>
 * Modified by ������ on 2017/8/29.
 */
public final class IKAnalyzer extends Analyzer {

    private boolean useSmart;
    private boolean useSingle;
    private boolean useItself;

    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public boolean useSingle() {
        return useSingle;
    }

    public void setUseSingle(boolean useSingle) {
        this.useSingle = useSingle;
    }

    public boolean useItself() {
        return useItself;
    }

    public void setUseItself(boolean useItself) {
        this.useItself = useItself;
    }

    /**
     * IK������Lucene  Analyzer�ӿ�ʵ����
     * <p>
     * Ĭ��ϸ�����з��㷨
     */
    public IKAnalyzer() {
        this(false, false, false);
    }

    /**
     * IK������Lucene Analyzer�ӿ�ʵ����
     *
     * @param useSmart  ��Ϊtrueʱ���ִ������������з�
     * @param useSingle �Ƿ����Ӣ�ĺ������������з�
     * @param useItself �Ƿ���Ӣ�ĺ�����ԭ��㵥Ԫ
     */
    public IKAnalyzer(boolean useSmart, boolean useSingle, boolean useItself) {
        super();
        this.useSmart = useSmart;
        this.useSingle = useSingle;
        this.useItself = useItself;
    }
    
    /**
     * ����Analyzer�ӿڣ�����ִ����
     */
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer _IKTokenizer = new IKTokenizer(this.useSmart());
        IKTokenFilter _IKTokenFilter = new IKTokenFilter(_IKTokenizer, useSingle, useItself);
        return new TokenStreamComponents(_IKTokenizer, _IKTokenFilter);
    }

}
