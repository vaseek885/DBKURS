package com.vsaf.common;
import java.sql.*;

public class Passport implements SQLData {
    public int num1;
    public int num2;
    private String sql_type;
    
    public Passport(int l, int r) {
        this.num1 = l;
        this.num2 = r;
    }

    public String getSQLTypeName() {
        return sql_type;
    }

    public void readSQL(SQLInput stream, String type)
        throws SQLException {
        sql_type = type;
        num1 = stream.readInt();
        num2 = stream.readInt();
    }

    public void writeSQL(SQLOutput stream)
        throws SQLException {
        stream.writeInt(num1);
        stream.writeInt(num2);
    }
}