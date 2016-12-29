package spms.controls;

import java.util.Map;

import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.vo.Member;

public class MemberAddController implements Controller, DataBinding {
	private MemberDao memberDao;
	public MemberAddController setMemberDao(MemberDao memberDao){
		this.memberDao = memberDao;
		return this;
	}
	
	@Override
	public Object[] getDataBinders() {
		// TODO Auto-generated method stub
		return new Object[]{"member", spms.vo.Member.class};
	}
	
	@Override
	public String execute(Map<String, Object> model) throws Exception {
		Member member = (Member) model.get("member");
		if (member.getEmail()== null) {
			return "/member/MemberForm.jsp";

		} else {
			//MemberDao memberDao = (MemberDao) model.get("memberDao");

			
			System.out.println("add컨트롤러 체크 : " +member );
			memberDao.insert(member);

			return "redirect:list.do";
		}
	}	
}
