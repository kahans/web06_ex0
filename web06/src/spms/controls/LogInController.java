package spms.controls;

import java.util.Map;

import javax.servlet.http.HttpSession;

import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.vo.Member;

public class LogInController implements Controller,DataBinding {
	private MemberDao memberDao;
	public LogInController setMemberDao(MemberDao memberDao){
		this.memberDao = memberDao;
		return this;
	}
	public Object[] getDataBinders() {
		// TODO Auto-generated method stub
		return new Object[]{"loginInfo", spms.vo.Member.class};
	}
	@Override
	public String execute(Map<String, Object> model) throws Exception {
		Member loginInfo = (Member) model.get("loginInfo");
		if (loginInfo.getEmail() == null) {
			return "/auth/LogInForm.jsp";

		} else { // �쉶�썝 �벑濡앹쓣 �슂泥��븷 �븣
			//MemberDao memberDao = (MemberDao) model.get("memberDao");
			Member member = memberDao.exist(loginInfo.getEmail(), loginInfo.getPassword());
			System.out.println("아이디 체크 : "+loginInfo.getEmail());
			System.out.println("비번 체크 : "+loginInfo.getPassword());
			System.out.println("맴버 확인 : "+member);
			if (member != null) {
				HttpSession session = (HttpSession) model.get("session");
				session.setAttribute("member", member);
				return "redirect:../member/list.do";
			} else {
				return "/auth/LogInFail.jsp";
			}
		}
	}
}
