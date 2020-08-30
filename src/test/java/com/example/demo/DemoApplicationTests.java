package com.example.demo;

import com.example.demo.controller.AccountController;
import com.example.demo.db.AccountRepository;
import com.example.demo.db.OperationRepository;
import com.example.demo.db.ParticipantRepository;
import com.example.demo.db.bean.Account;
import com.example.demo.db.bean.Operation;
import com.example.demo.db.bean.Participant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
		DemoApplication.class,
		H2TestProfileConfig.class})
@ActiveProfiles("test")
class DemoApplicationTests {

	@Autowired
	private AccountController controller;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ParticipantRepository participantRepository;

	@Autowired
	private OperationRepository operationRepository;

	private final static Random RANDOM = new Random();

	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

	@Test
	void loadAccountList() {
		List<Account> accountList = accountRepository.getAll();
		assertThat(accountList).isNotNull();
		assertThat(accountList).isNotEmpty();
	}

	@Test
	void loadParticipantList() {
		List<Participant> participantList = participantRepository.getAll();
		assertThat(participantList).isNotNull();
		assertThat(participantList).isNotEmpty();
	}

	@Test
	void moveInnerMoney() throws Exception {
		List<Participant> participantList = participantRepository.getAll();
		Participant participant = participantList.get(0);
		List<Account> accountList = accountRepository.getAccountListByParticipantId(participant.getParticipantId());
		System.out.println(accountList);
		assertThat(accountList.size() >= 2).isTrue();
		Account from = accountList.get(0);
		Account to = accountList.get(1);
		long amount = 10;
		//oper1
		participantRepository.sendMoney(participant.getParticipantId(), participant.getParticipantId(),
					from.getAccountId(), to.getAccountId(), amount);
		//check operation: amount
		this.checkOperAmount(from, to, amount);

		//oper2
		participantRepository.sendMoney(participant.getParticipantId(), participant.getParticipantId(),
				from.getAccountId(), to.getAccountId(), amount);
		//need get 2 oper with type=0(minus -10)
		this.checkOperationByAccount(participant, from, amount, OperationType.MINUS);

		//need get 2 oper with type=1(plus +10)
		this.checkOperationByAccount(participant, to, amount, OperationType.PLUS);
	}

	private void checkOperAmount(Account from, Account to, long amount) {
		Account newFrom = accountRepository.getById(from.getAccountId());
		assertThat(newFrom).isNotNull();
		Account newTo = accountRepository.getById(to.getAccountId());
		assertThat(newTo).isNotNull();
		long newFromVolume = from.getAmount() - amount;
		assertThat(newFromVolume == newFrom.getAmount()).isTrue();
		long newToVolume = to.getAmount() + amount;
		assertThat(newToVolume == newTo.getAmount()).isTrue();
	}

	private void checkOperationByAccount(Participant participant, Account account, long amount, OperationType type) {
		List<Operation> operationList = operationRepository.getAllOperationsByAccId(participant.getParticipantId(),
				account.getAccountId());
		assertThat(operationList).isNotNull();
		assertThat(operationList).isNotEmpty();
		assertThat(operationList.size() >= 2).isTrue();
		for (Operation operation : operationList) {
			assertThat(operation).isNotNull();
			assertThat(operation.getAmount()).isNotNull();
			assertThat(operation.getAmount() == amount).isTrue();
			assertThat(operation.getType() == type.getType()).isTrue();
		}
	}
}
