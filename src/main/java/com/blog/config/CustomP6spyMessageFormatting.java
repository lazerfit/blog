package com.blog.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

public class CustomP6spyMessageFormatting implements MessageFormattingStrategy {


    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category,
        String prepared, String sql, String url) {
        if (sql.contains("SPRING_SESSION") || sql.contains("SPRING_SESSION_ATTRIBUTES")) {
            return "IGNORE";
        }

        // Original format
        return String.format("%s | took %s ms | %s | connection %s\n %s",
            now, elapsed, category, connectionId, prepared);
    }
}
