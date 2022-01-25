package com.kosmo.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import transaction.TicketDAO;
import transaction.TicketDTO;

@Controller
public class TransactionController {

	//트랜잭션 매니저를 활용한 트랜잭션 처리
	/*
	servlet-context.xml에서 미리 생성한 빈을 자동주입받는다.
	 */
	private TicketDAO dao;
	
	@Autowired
	public void setDao(TicketDAO dao) {
		this.dao = dao;
		System.out.println("@Autowired => TicketDAO 주입성공");
	}
	
	//티켓구매페이지
	@RequestMapping("/transaction/buyTicketMain.do")
	public String buyTicketMain() {
		
		return "08Transaction/buyTicketMain";
	}
	
	//티켓구매처리페이지
	@RequestMapping("/transaction/buyTicketAction.do")
	public String buyTicketAction(TicketDTO ticketDTO, Model model) {
		
		//구매페이지에서 전송한 폼값을 한번에 처리하기 위해 커맨드객체를 사용함.
		dao.buyTicket(ticketDTO);
		model.addAttribute("ticketInfo",ticketDTO);
		return "08Transaction/buyTicketAction";
	}
	
}
