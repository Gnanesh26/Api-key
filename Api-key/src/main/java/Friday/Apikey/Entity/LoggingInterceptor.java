package Friday.Apikey.Entity;



import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.sql.*;
import java.time.LocalDateTime;

//
//
//public class LoggingInterceptor implements HandlerInterceptor {
//    private final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
//    private final String dbUrl = "jdbc:mysql://localhost:3306/Friday";
//    private final String dbUsername = "root";
//    private final String dbPassword = "Gnanesh143@";
//
//    private int modificationCount = 0; // Tracks the modification count
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//            throws Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        String requestUrl = request.getRequestURL().toString();
//        String httpMethod = request.getMethod();
//        String userAgent = request.getHeader("User-Agent");
//        String username = authentication != null ? authentication.getName() : "anonymous";
//        String role = getRole(authentication);
//
//        logger.info("Request URL: {}", requestUrl);
//        logger.info("HTTP Method: {}", httpMethod);
//        logger.info("User Agent: {}", userAgent);
//        logger.info("User: {}", username);
//        logger.info("Role: {}", role);
//
//        // Update access count, log access, and modification count
//        storeLog(requestUrl, httpMethod, userAgent, username, role);
//        incrementModificationCount(requestUrl);
//
//        return true;
//    }
//
//    private void storeLog(String requestUrl, String httpMethod, String userAgent, String username, String role) {
//        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            String selectSql = "SELECT access_count, log_time FROM logs WHERE request_url = ?";
//            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
//                selectStatement.setString(1, requestUrl);
//                ResultSet resultSet = selectStatement.executeQuery();
//                if (resultSet.next()) {
//                    int accessCount = resultSet.getInt("access_count") + 1;
//                    String existingLogTime = resultSet.getString("log_time");
//                    String newLogTime = existingLogTime + "," + LocalDateTime.now().toString();
//
//                    String updateSql = "UPDATE logs SET access_count = ?, log_time = ? WHERE request_url = ?";
//                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
//                        updateStatement.setInt(1, accessCount);
//                        updateStatement.setString(2, newLogTime);
//                        updateStatement.setString(3, requestUrl);
//                        updateStatement.executeUpdate();
//                    }
//                } else {
//                    String insertSql = "INSERT INTO logs (request_url, http_method, user_agent, username, role, access_count, log_time, modification_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
//                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
//                        insertStatement.setString(1, requestUrl);
//                        insertStatement.setString(2, httpMethod);
//                        insertStatement.setString(3, userAgent);
//                        insertStatement.setString(4, username);
//                        insertStatement.setString(5, role);
//                        insertStatement.setInt(6, 1);
//                        insertStatement.setString(7, LocalDateTime.now().toString());
//                        insertStatement.setInt(8, 0); // Set initial modification count to 0
//                        insertStatement.executeUpdate();
//                    }
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String getRole(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return "anonymous";
//        }
//
//        for (GrantedAuthority authority : authentication.getAuthorities()) {
//            return authority.getAuthority();
//        }
//
//        return "unknown";
//    }
//
//    private void incrementModificationCount(String requestUrl) {
//        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
//            String updateSql = "UPDATE logs SET modification_count = modification_count + 1 WHERE request_url = ?";
//            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
//                updateStatement.setString(1, requestUrl);
//                updateStatement.executeUpdate();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    }

public class LoggingInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);
    private final String dbUrl = "jdbc:mysql://localhost:3306/Friday";
    private final String dbUsername = "root";
    private final String dbPassword = "Gnanesh143@";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String requestUrl = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        String userAgent = request.getHeader("User-Agent");
        String username = authentication != null ? authentication.getName() : "anonymous";
        String role = getRole(authentication);

        logger.info("Request URL: {}", requestUrl);
        logger.info("HTTP Method: {}", httpMethod);
        logger.info("User Agent: {}", userAgent);
        logger.info("User: {}", username);
        logger.info("Role: {}", role);

        // Update access count and log access
        storeLog(requestUrl, httpMethod, userAgent, username, role);

        return true;
    }

    private void storeLog(String requestUrl, String httpMethod, String userAgent, String username, String role) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword)) {
            String selectSql = "SELECT access_count, log_time FROM logs WHERE request_url = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql)) {
                selectStatement.setString(1, requestUrl);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next()) {
                    int accessCount = resultSet.getInt("access_count") + 1;
                    String existingLogTime = resultSet.getString("log_time");
                    String newLogTime = existingLogTime + "," + LocalDateTime.now().toString();

                    String updateSql = "UPDATE logs SET access_count = ?, log_time = ? WHERE request_url = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                        updateStatement.setInt(1, accessCount);
                        updateStatement.setString(2, newLogTime);
                        updateStatement.setString(3, requestUrl);
                        updateStatement.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO logs (request_url, http_method, user_agent, username, role, access_count, log_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        insertStatement.setString(1, requestUrl);
                        insertStatement.setString(2, httpMethod);
                        insertStatement.setString(3, userAgent);
                        insertStatement.setString(4, username);
                        insertStatement.setString(5, role);
                        insertStatement.setInt(6, 1);
                        insertStatement.setString(7, LocalDateTime.now().toString());
                        insertStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getRole(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            return authority.getAuthority();
        }

        return "unknown";
    }
}


