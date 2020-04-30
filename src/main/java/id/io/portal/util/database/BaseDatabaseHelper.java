package id.io.portal.util.database;

import id.io.portal.manager.ConnectionManager;
import id.io.portal.util.log.AppLogger;
import java.sql.SQLException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.PreparedBatch;
import org.jdbi.v3.core.statement.Update;

public class BaseDatabaseHelper {

    protected AppLogger log;

    protected AppLogger getLogger(Class<?> clazz) {
        return new AppLogger(clazz);
    }

    protected Handle getHandle() throws SQLException {
        return ConnectionManager.getHandle();
    }

    protected Handle getHandle(RowMapper<?>... rowMappers) throws SQLException {
        Handle h = getHandle();

        if (rowMappers != null && rowMappers.length > 0) {
            for (RowMapper<?> mapper : rowMappers) {
                h.registerRowMapper(mapper);
            }
        }
        return h;
    }

    protected boolean executeUpdate(Update update) {
        return update.execute() > 0;
    }

    protected int executeAndGetId(Update update) {
        return update.executeAndReturnGeneratedKeys().mapTo(Integer.class).one();
    }

    protected boolean executeBatch(PreparedBatch batch) {
        int[] resultArr = batch.execute();

        for (int result : resultArr) {
            if (result < 0) {
                return false;
            }
        }
        return true;
    }

}
