package com.objcoding.calcite.csv.filter;

import org.apache.calcite.linq4j.Enumerator;
import org.apache.calcite.rex.RexCall;
import org.apache.calcite.rex.RexLiteral;
import org.apache.calcite.rex.RexNode;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.util.Source;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 0.0.1
 */
public class CsvEnumerator<E> implements Enumerator<E> {

    private E current;

    private BufferedReader br;

    private String name;


    public CsvEnumerator(Source source, RexNode rexNode) {
        try {
            this.br = new BufferedReader(source.reader());
            String s = this.br.readLine();
            if (rexNode != null) {
                filter(rexNode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void filter(RexNode rexNode) {
        if (rexNode.isA(Arrays.asList(SqlKind.EQUALS))) {
            final RexCall call = (RexCall) rexNode;
            RexNode left = call.getOperands().get(0);
            RexNode right = call.getOperands().get(1);
            name = (String) ((RexLiteral) right).getValue2();
        }
    }

    @Override
    public E current() {
        return current;
    }

    @Override
    public boolean moveNext() {
        try {
            for (; ; ) {
                String line = br.readLine();
                if (line == null) {
                    return false;
                }
                if (StringUtils.isNotBlank(name)) {
                    if (!line.contains(name)) {
                        continue;
                    }
                }
                if (StringUtils.isBlank(name)) {
                    return false;
                }
                current = (E) line.split(",");    // 如果是多列，这里要多个值
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 出现异常走这里
     */
    @Override
    public void reset() {
        System.out.println("报错了兄弟，不支持此操作");
    }

    /**
     * InputStream流在这里关闭
     */
    @Override
    public void close() {

    }
}
