package me.redtea.carcadelibs.sql;

import com.google.common.util.concurrent.*;
import lombok.*;

import java.sql.*;
import java.util.concurrent.*;

public interface SqlDatabase {

    ExecutorService THREAD_POOL = Executors.newCachedThreadPool(new ThreadFactoryBuilder()
            .setNameFormat("LAST MySQL-Worker #%s")
            .setDaemon(true)
            .build());
    int execute(boolean async, @NonNull String sql, Object...objects);
    <V> V executeQuery(boolean async, @NonNull String sql, @NonNull ResponseHandler<ResultSet, V> handler, Object...objects);

    Connection getConnection();

    void closeConnection() throws SQLException;

    default <V> V handle(boolean async, Callable<V> callable) {
        if(async) {
            Future<V> future = THREAD_POOL.submit(callable);
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Failed to execute async query", e);
            }
        } else {
            try {
                return callable.call();
            } catch (Exception e) {
                throw new RuntimeException("Failed to execute sync query", e);
            }
        }
    }

    default ResultSet query(String sql, Object... objects) {
        try {
            SqlStatement statement = new SqlStatement(getConnection(), sql, objects);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException("[SQL] Error executing query to database", e);
        }
    }
}
