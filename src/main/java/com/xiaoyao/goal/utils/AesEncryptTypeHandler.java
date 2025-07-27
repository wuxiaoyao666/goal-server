package com.xiaoyao.goal.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 逍遥
 */
public class AesEncryptTypeHandler extends BaseTypeHandler<String> {

    private static final String Secret = "SengeSenge520...";
    private static final AES aes = SecureUtil.aes(Secret.getBytes());

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (StrUtil.isNotBlank(parameter)) {
            byte[] encrypted = aes.encrypt(parameter);
            ps.setBytes(i, encrypted);
        } else {
            ps.setBytes(i, null);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        byte[] bytes = rs.getBytes(columnName);
        return decryptBytes(bytes);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        byte[] bytes = rs.getBytes(columnIndex);
        return decryptBytes(bytes);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        byte[] bytes = cs.getBytes(columnIndex);
        return decryptBytes(bytes);
    }

    private String decryptBytes(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;
        return aes.decryptStr(bytes, StandardCharsets.UTF_8);
    }
}
