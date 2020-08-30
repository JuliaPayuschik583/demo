package com.example.demo.db;

import com.example.demo.db.bean.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

@Repository
public class AccountRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Account getById(long id) {
        String sql = "select * from accounts where account_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Account(rs));

    }

    public List<Account> getAll() {
        return jdbcTemplate.query("select * from accounts", (resultSet, i) -> {
            return toAccount(resultSet);
        });
    }

    public List<Account> getAccountListByParticipantId(long participantId) {
        String sql = "SELECT a.* FROM accounts a " +
                "JOIN participants p ON a.participant_id=p.participant_id " +
                "WHERE p.participant_id = ?";

        return jdbcTemplate.query(sql, new Object[]{participantId},
                new int[]{Types.INTEGER},
                (resultSet, i) -> toAccount(resultSet));
    }

    private Account toAccount(ResultSet resultSet) throws SQLException {
        Account person = new Account();
        person.setAccountId(resultSet.getInt("account_id"));
        person.setParticipantId(resultSet.getInt("participant_id"));
        person.setAmount(resultSet.getLong("amount"));
        person.setCurrency(resultSet.getString("currency"));
        return person;
    }

    public void insert(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (participant_id, volume, currency) VALUES (?, ?)";
        int result = jdbcTemplate.update(sql, new Object[] { account.getParticipantId(), account.getAmount(),
                account.getCurrency() });
        System.out.println(result);
        if (result == 0) throw new SQLException("Account not inserted");
    }

    @Transactional
    public void addAccount(Account account) throws SQLException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertSql = "INSERT INTO accounts (participant_id, volume, currency) VALUES (?, ?, ?)";
        int insertResult = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(insertSql);
            ps.setLong(1, account.getParticipantId());
            ps.setLong(2, account.getAmount());
            ps.setString(3, account.getCurrency());
            return ps;
        }, keyHolder);
        System.out.println(insertResult);
        if (insertResult == 0) throw new SQLException("Account not inserted");
    }

}
