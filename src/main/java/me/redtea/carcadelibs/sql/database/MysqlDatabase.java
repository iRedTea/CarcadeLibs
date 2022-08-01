package me.redtea.carcadelibs.sql.database;

import com.mysql.jdbc.jdbc2.optional.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.*;
import me.redtea.carcadelibs.sql.SqlDatabase;
import me.redtea.carcadelibs.sql.ResponseHandler;
import me.redtea.carcadelibs.sql.SqlStatement;

import java.sql.*;

public abstract class MysqlDatabase implements SqlDatabase {
    private Connection connection;

    private final HikariDataSource src;

    //@Builder(buildMethodName = "create")
    public MysqlDatabase(String host, int port, String user, String password, String database) throws SQLException {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);

        src = new HikariDataSource(config);

        connection = src.getConnection();

        /*this.dataSource.setServerName(host);
        this.dataSource.setPort(port);
        this.dataSource.setUser(user);
        this.dataSource.setPassword(password);
        this.dataSource.setDatabaseName(database);
        this.dataSource.setEncoding("UTF-8");
        this.refreshConnection();
         */
        createTables();
    }

    public abstract void createTables();

    @Override
    public int execute(boolean async, @NonNull String sql, Object... objects) {
        return handle(async, () -> {
            try (SqlStatement statement = new SqlStatement(getConnection(), sql, objects)) {
                return statement.execute();
            }
        });
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
    public ResultSet executeQuery(boolean async, @NonNull String sql, Object... objects) {
        return handle(async, () -> {
            try (SqlStatement statement = new SqlStatement(getConnection(), sql, objects)) {
                return statement.executeQuery();
            }
        });
    }

    @Override
    public Connection getConnection() {
        try {
            return this.refreshConnection();
        } catch (SQLException e) {
            throw new RuntimeException("[MysqlDatabase] Error connecting to database", e);
        }
    }

    private Connection refreshConnection() throws SQLException {
        if (this.connection == null || this.connection.isClosed() || !this.connection.isValid(1000)) {
            this.connection = this.src.getConnection();
        }
        return this.connection;
    }

    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }
}
