package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

/**
 * Created by ������ on 2017/8/30.
 * <p>
 * IK�ִ��������ࡣ���������ļ��з�������ӷִ���(���빤����)��
 */
public class IKTokenizerFactory extends TokenizerFactory {

    private boolean useSmart;

    /**
     * ��{@code managed-schema}���ݵ�ֵ�С����� useSmart ��ֵ
     * @param args
     */
    public IKTokenizerFactory(Map<String, String> args) {
        super(args);
        /*
         * �ж�Map�������Ƿ����useSmart������л�ȡ��key��Ӧ��value��
         * ���û��,������Ĭ��ֵ��Ҳ���ǵ��������� false
         */
        useSmart = this.getBoolean(args, "useSmart", false);// ִ���꣬useSmart�ᱻ��map�Ƴ�

        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        return new IKTokenizer(factory, useSmart);
    }
}
