package com.example.demo.db;

import com.example.demo.db.bean.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class OperationRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Operation> getAllOperationsByAccId(long participantId, long accountId) {
        String sql = "SELECT o.* FROM accounts a " +
                "JOIN operations o ON a.account_id=o.account_id " +
                "WHERE a.participant_id = ? AND a.account_id = ? " +
                "ORDER BY o.date DESC";

        return jdbcTemplate.query(sql, new Object[]{participantId, accountId},
                new int[]{Types.INTEGER, Types.INTEGER},
                (resultSet, i) -> OperationRepository.this.toOperation(resultSet));
    }

    private Operation toOperation(ResultSet resultSet) throws SQLException {
        Operation operation = new Operation();
        operation.setOperationId(resultSet.getInt("operation_id"));
        operation.setTransactionId(resultSet.getInt("transaction_id"));
        operation.setAccountId(resultSet.getInt("account_id"));
        operation.setType(resultSet.getInt("type"));
        operation.setDate(resultSet.getLong("date"));
        operation.setAmount(resultSet.getLong("amount"));
        return operation;
    }
}
