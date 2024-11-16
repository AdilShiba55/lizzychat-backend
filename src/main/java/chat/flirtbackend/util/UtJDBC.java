package chat.flirtbackend.util;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class UtJDBC {
    @SneakyThrows
    public static boolean contains(ResultSet resultSet, String neededName) {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for(int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
            String columnName = resultSetMetaData.getColumnLabel(i+1);
            if(columnName.equals(neededName)) {
                return true;
            }
        }
        return false;
    }
}
