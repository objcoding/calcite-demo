package com.objcoding.calcite.csv.scan;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * @author zhangchenghui.dev@gmail.com
 * @since 0.0.1
 */
public class Scannable {

    /**
     * 测试的时候用字符串 defaultSchema 默认数据库 name 数据库名称 type custom factory
     * 请求接收类，该类会实例化Schema也就是数据库类，Schema会实例化Table实现类，Table会实例化数据类。
     * operand 动态参数，ScheamFactory的create方法会接收到这里的数据
     */
    public static void main(String[] args) {
        try {
            // 字符串方式
            String model = Resources.asCharSource(Resources.getResource("model-scan.json"), Charset.defaultCharset()).read();
            Connection connection = DriverManager.getConnection("jdbc:calcite:model=inline:" + model);
            Statement statement = connection.createStatement();
            String sql = "select * from test_csv.TEST01 where name1 = 'hello'";
            ResultSet resultSet = statement.executeQuery(sql);
            System.out.println(JSON.toJSONString(getData(resultSet)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Map<String,Object>> getData(ResultSet resultSet)throws Exception{
        List<Map<String,Object>> list = Lists.newArrayList();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnSize = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<String, Object> map = Maps.newLinkedHashMap();
            for (int i = 1; i < columnSize + 1; i++) {
                map.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        return list;
    }

}
