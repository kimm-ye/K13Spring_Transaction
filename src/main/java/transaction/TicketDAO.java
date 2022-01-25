package transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

//servlet-context.xml파일에서 TicketDAO 설정해놓음
public class TicketDAO {

	/*
	멤버변수 선언
		: Spring-JDBC를 사용하기 위한 멤버변수와 setter() 정의
		트랜잭션 처리를 위한 매니저클래스의 멤버변수와 setter() 정의
	 */
	JdbcTemplate template;
	PlatformTransactionManager transactionManager;
	/*
	servlet-context.xml에서 빈 생성시 setter()를 통해 초기화한다.
	 */
	
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}
	
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	//기본생성자
	public TicketDAO() {
		System.out.println("TicketDAO생성자호출");
	}
	
	//티켓구매와 결제를 위한 메서드로 트랜잭션 처리를 한다.
	//트랜잭션 처리란 한번에 처리하는 것을 말하며, try ~ catch를 통해 하나라도 잘못되면 예외처리를 해버린다..
	public void buyTicket(final TicketDTO dto) {
		
		System.out.println("buyTicket()메소드 호출");
		System.out.println(dto.getCustomerId() + "님이 티켓 " + dto.getAmount() + "장을 구매합니다.");
		
		//DAO에서 트랜잭션 처리를 위한 객체생성
		TransactionDefinition def = new DefaultTransactionDefinition();
		//해당 객체를 통해 최종 트랜잭션 처리를 하게 된다.
		TransactionStatus status = transactionManager.getTransaction(def);
		
		/*
		두개의 업무를 하나로 처리하고, 두개 중 하나가 잘못되면 롤백처리한다.
		
		2개의 업무를 하나의 프로세스로 처리하기 위해 try~catch로 묶어준다.
		만약 하나라도 업무에 문제(예외)가 생기게 된다면 예외가 발생하여 모든
		업무는 rollback처리된다.
		문제가 없다면 commit되어 DB에 반영된다.
		 */
		try {
			//티켓 구매 금액에 대한 DB 처리
			template.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					
					/*
					티켓의 가격은 장당 10000원 이라고 가정하고, 구매수에 곱해서 입력한다.
					 */
					String query = " INSERT INTO transaction_pay (customerId, amount) VALUES (?, ?) ";
					
					PreparedStatement psmt = con.prepareStatement(query);
					psmt.setString(1, dto.getCustomerId());
					psmt.setInt(2, dto.getAmount()*10000);
					
					return psmt;
				}
			});
			
			//티켓 매수에 대한 DB처리
			/*
			check제약조건에 의해 매수가 5장을 초과하는 경우 제약조건 위배로
			쿼리 에러가 발생하게 된다.
			 */
			template.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

					String query = " INSERT INTO transaction_ticket (customerId, countNum) VALUES (?, ?) ";
					
					PreparedStatement psmt = con.prepareStatement(query);
					psmt.setString(1, dto.getCustomerId());
					psmt.setInt(2, dto.getAmount());
					
					return psmt;
				}
			});
			
			//티켓매수가 5장 이하라면 정상처리되어 모든 작업이 commit 된다.
			System.out.println("카드결제와 티켓구매 모두 정상처리 되었습니다.");
			transactionManager.commit(status);
		}
		catch (Exception e) {
			//매수를 초과하면 모든 작업은 rollback 처리된다.
			System.out.println("제약조건을 위배하여 카드결제와 티켓구매 모두가 취소되었습니다.");
			transactionManager.rollback(status);
		}
	}
}
