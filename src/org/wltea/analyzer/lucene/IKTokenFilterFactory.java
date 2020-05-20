package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.util.TokenFilterFactory;

import java.util.Map;

/**
 * Created by ������ on 2017/9/12.
 */
public class IKTokenFilterFactory extends TokenFilterFactory {

    private boolean useSingle;
    private boolean useItself;
    /**
     * Initialize this factory via a set of key-value pairs.
     *
     * ��{@code managed-schema}���ݵ�ֵ�С����� useSingle ��ֵ
     *
     * @param args
     */
    public IKTokenFilterFactory(Map<String, String> args) {
        super(args);

        /*
         * �ж�Map�������Ƿ����useSingle������л�ȡ��key��Ӧ��value��
         * ���û��,������Ĭ��ֵ��Ҳ���ǵ��������� false
         */
        useSingle = this.getBoolean(args, "useSingle", false);// ִ���꣬useSingle�ᱻ��map�Ƴ�

        useItself = this.getBoolean(args, "useItself", true);// IKTokenFilter����IKTokenizer�����Ӣ�ĺ�����ԭ��㵥Ԫ

        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    public TokenStream create(TokenStream input) {
        return new IKTokenFilter(input, useSingle, useItself);
    }
}
