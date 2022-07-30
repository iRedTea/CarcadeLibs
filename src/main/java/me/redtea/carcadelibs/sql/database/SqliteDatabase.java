package me.redtea.carcadelibs.sql.database;

import lombok.*;
import me.redtea.carcadelibs.sql.ResponseHandler;
import me.redtea.carcadelibs.sql.SqlDatabase;
import me.redtea.carcadelibs.sql.SqlStatement;
import org.sqlite.*;

import java.io.*;
import java.sql.*;

public abstract class SqliteDatabase implements SqlDatabase {

    private final SQLiteDataSource dataSource = new SQLiteDataSource();
    private Connection connection;

    //@Builder(buildMethodName = "create")
    public SqliteDatabase(File databaseFile, String database) throws SQLException {
        this("jdbc:sqlite:" + databaseFile, database);
        createTables();
    }

    public abstract void createTables();

    public SqliteDatabase(String url, String database) throws SQLException {
        this.dataSource.setDatabaseName(database);
        this.dataSource.setUrl(url);
        this.refreshConnection();
    }

    @Override
    public <V> V executeQuery(boolean async, @NonNull String sql, @NonNull ResponseHandler<ResultSet, V> handler, Object... objects) {
        return handle(async, () -> {
            try (SqlStatement statement = new SqlStatement(getConnection(), sql, objects)) {
                return handler.handleResponse(statement.executeQuery());
            }
        });
    }

    @Override
    public int execute(boolean async, @NonNull String sql, Object... objects) {
        return handle(async, () -> {
            try (SqlStatement statement = new SqlStatement(getConnection(), sql, objects)) {
                return statement.execute();
            }
        });
    }

    @Override
    public Connection getConnection() {
        try {
            return this.refreshConnection();
        } catch (SQLException e) {
            throw new RuntimeException("[SqliteDatabase] Error connecting to database", e);
        }
    }

    private Connection refreshConnection() throws SQLException {
        if(this.connection == null || this.connection.isClosed() || !this.connection.isValid(1000)) {
            this.connection = this.dataSource.getConnection();
        }
        return this.connection;
    }

    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}
