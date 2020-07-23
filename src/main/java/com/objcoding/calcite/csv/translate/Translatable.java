package com.objcoding.calcite.csv.translate;

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
public class Translatable {

    public static void main(String[] args) {
        try {
            // 字符串方式
            String model = Resources.asCharSource(Resources.getResource("model-translate.json"), Charset.defaultCharset()).read();
            Connection connection = DriverManager.getConnection("jdbc:calcite:model=inline:" + model);

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select name from sales.DEPTS where name in ('Sales')");
            System.out.println(JSON.toJSONString(getData(resultSet)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> getData(ResultSet resultSet) throws Exception {
        List<Map<String, Object>> list = Lists.newArrayList();
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
