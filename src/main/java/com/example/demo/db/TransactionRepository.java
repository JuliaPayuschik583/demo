package com.example.demo.db;

import com.example.demo.TransactionStatus;
import com.example.demo.db.bean.Transaction;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TransactionRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Transaction> getAllTransaction() {
        return jdbcTemplate.query("select * from transactions", (resultSet, i) -> {
            return toTransaction(resultSet);
        });
    }

    public List<Transaction> getAllTransactionByPartId(int participantId) {
        return jdbcTemplate.query("select * from transactions where from_participant_id=?",
                new Object[]{participantId}, (resultSet, i) -> {
            return toTransaction(resultSet);
        });
    }

    private Transaction toTransaction(ResultSet resultSet) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(resultSet.getInt("transaction_id"));
        transaction.setFromParticipantId(resultSet.getInt("from_participant_id"));
        transaction.setToParticipantId(resultSet.getInt("to_participant_id"));
        transaction.setFromAccountId(resultSet.getInt("from_account_id"));
        transaction.setToAccountId(resultSet.getInt("to_account_id"));
        transaction.setDate(resultSet.getLong("date"));
        transaction.setStatus(resultSet.getInt("status"));
        transaction.setMessage(resultSet.getString("message"));
        return transaction;
    }

    public int insertTransaction(long fromParticipantId, long toParticipantId, long fromAccId, long toAccId, long unixTimestamp) throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertTrSql = "INSERT INTO transactions (from_participant_id, to_participant_id, from_account_id, to_account_id, " +
                "date, status) VALUES (?,?,?, ?,?,?)";
        int insertRes = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertTrSql);
            ps.setLong(1, fromParticipantId);
            ps.setLong(2, toParticipantId);
            ps.setLong(3, fromAccId);
            ps.setLong(4, toAccId);
            ps.setLong(5, unixTimestamp);
            ps.setInt(6, TransactionStatus.START.getStatus());
            return ps;
        }, keyHolder);
        System.out.println(insertRes);
        if (insertRes == 0) throw new SQLException("transaction not inserted");

        return (int) keyHolder.getKey();
    }

    public void updateTransactionStatus(final int trId, final TransactionStatus trStatus, final String mess) throws SQLException {
        StringBuffer upTrSql
                = new StringBuffer("UPDATE transactions SET status = ?");

        final Object[] args;
        if (Strings.isNotEmpty(mess)) {
            upTrSql.append(", message = ?");
            args = new Object[3];
            args[0] = trStatus.getStatus();
            args[1] = mess;
        } else {
            args = new Object[2];
            args[0] = trStatus.getStatus();
        }
        args[args.length-1] = trId;

        upTrSql.append(" WHERE transaction_id = ?");

        int upTrRes = jdbcTemplate.update(upTrSql.toString(), args);
        System.out.println(upTrRes);
        if (upTrRes == 0) throw new SQLException("transaction not updated");
    }
}
