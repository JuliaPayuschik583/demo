package com.example.demo.db;

import com.example.demo.OperationType;
import com.example.demo.TransactionStatus;
import com.example.demo.Utils;
import com.example.demo.db.bean.Account;
import com.example.demo.db.bean.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ParticipantRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CurrencyRepository currencyRepository;

    public void insert(Participant participant) throws SQLException {
        String sql = "INSERT INTO participants (name) VALUES (?)";
        int result = jdbcTemplate.update(sql, new Object[] { participant.getName() });
        System.out.println(result);
        if (result == 0) throw new SQLException("Participant not inserted");
    }

    public List<Participant> getAll() {
        return jdbcTemplate.query("select * from participants", new RowMapper<Participant>() {
            @Override
            public Participant mapRow(ResultSet resultSet, int i) throws SQLException {
                return ParticipantRepository.this.toParticipant(resultSet);
            }
        });
    }

    private Participant toParticipant(ResultSet resultSet) throws SQLException {
        Participant participant = new Participant();
        participant.setParticipantId(resultSet.getInt("participant_id"));
        participant.setName(resultSet.getString("name"));
        return participant;
    }

    @Transactional
    public void sendMoney(long fromParticipantId, long toParticipantId, long fromAccId, long toAccId,
                          final long amount) throws Exception {
        final long unixTimestamp = Utils.getUnixTimestamp();

        //set in transaction
        int trId = transactionRepository.insertTransaction(fromParticipantId, toParticipantId,
                fromAccId, toAccId, unixTimestamp);

        TransactionStatus trStatus;
        String mess = null;

        String sql = "SELECT * FROM accounts WHERE participant_id = ? AND account_id = ?";

        Account fromAccount = jdbcTemplate.queryForObject(sql, new Object[]{fromParticipantId, fromAccId}, (rs, rowNum) ->
                new Account(rs));

        Account toAccount = jdbcTemplate.queryForObject(sql, new Object[]{toParticipantId, toAccId}, (rs, rowNum) ->
                new Account(rs));

        if (fromAccount == null || toAccount == null) {
            throw new Exception("wrong accounts param");
        }

        final long fromAmount;

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            //recalculate
            Integer factor = currencyRepository.getFactor(fromAccount.getCurrency(), toAccount.getCurrency());
            if (factor == null) {
                throw new Exception("wrong currency params");
            }
            fromAmount = fromAccount.getAmount() * factor;
        } else {
            fromAmount = fromAccount.getAmount();
        }

        if (fromAmount < amount) {
            trStatus = TransactionStatus.ERROR;
            mess = "not enough money";
        } else {
            String upFromAccSql = "UPDATE accounts SET amount = amount - ? WHERE account_id = ?";

            int upResult1 = jdbcTemplate.update(upFromAccSql, new Object[]{amount, fromAccount.getAccountId()});
            System.out.println(upResult1);
            if (upResult1 == 0) throw new SQLException("Account(from) not updated");

            String upToAccSql = "UPDATE accounts SET amount = amount + ? WHERE account_id = ?";

            int upResult2 = jdbcTemplate.update(upToAccSql, new Object[]{amount, toAccount.getAccountId()});
            System.out.println(upResult2);
            if (upResult2 == 0) throw new SQLException("Account(to) not updated");

            //up status transaction
            trStatus = TransactionStatus.SUCCESSFUL;

            //set operations
            this.insertOperation(trId, fromAccount.getAccountId(), OperationType.MINUS, amount, unixTimestamp);
            this.insertOperation(trId, toAccount.getAccountId(), OperationType.PLUS, amount, unixTimestamp);
        }

        //up status
        transactionRepository.updateTransactionStatus(trId, trStatus, mess);

    }

    private void insertOperation(final int trId, final long accountId, final OperationType type, final long amount,
                                 final long unixTimestamp) throws SQLException {
        String insertOperSql = "INSERT INTO operations (transaction_id, account_id, type, date, amount) " +
                "VALUES (?, ?, ?, ?, ?)";
        int result = jdbcTemplate.update(insertOperSql,
                new Object[] { trId, accountId, type.getType(), unixTimestamp, amount });
        System.out.println(result);
        if (result == 0) throw new SQLException("operation not inserted");
    }

}
