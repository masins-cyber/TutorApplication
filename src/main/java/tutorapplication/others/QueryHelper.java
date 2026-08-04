package tutorapplication.others;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryHelper {

    private QueryHelper() {
        // Private constructor to prevent instantiation
    }

    // Functional Interface to map a ResultSet row to the desired object
    @FunctionalInterface
    public interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    // Functional Interface to bind parameters into the PreparedStatement
    @FunctionalInterface
    public interface StatementSetter {
        void setParameters(PreparedStatement stmt) throws SQLException;
    }

    // Executes a SELECT query, manages PreparedStatement resources, and maps the ResultSet
    @SuppressWarnings("SqlSourceToSinkFlow")
    public static <T> T executeQuery(String query, StatementSetter setter, ResultSetMapper<T> mapper) throws SQLException {
        //noinspection TryStatementWithMultipleResources,SqlSourceToSinkFlow
        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            if (setter != null) {
                setter.setParameters(stmt);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                return mapper.map(rs);
            }
        }
    }

    // Executes an INSERT, UPDATE, or DELETE SQL statement
    @SuppressWarnings("SqlSourceToSinkFlow")
    public static void executeUpdate(String query, StatementSetter setter) throws SQLException {
        //noinspection TryStatementWithMultipleResources,SqlSourceToSinkFlow
        try (Connection conn = Connect.getInstance().getDBConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            if (setter != null) {
                setter.setParameters(stmt);

            }
            stmt.executeUpdate();
        }
    }
}
