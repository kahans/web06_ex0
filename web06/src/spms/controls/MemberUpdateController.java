package spms.controls;

import java.util.Map;

import spms.bind.DataBinding;
import spms.dao.MemberDao;
import spms.vo.Member;

public class MemberUpdateController implements Controller, DataBinding{
	private MemberDao memberDao;
	public MemberUpdateController setMemberDao(MemberDao memberDao){
		this.memberDao = memberDao;
		return this;
	}
	public Object[] getDataBinders() {
		// TODO Auto-generated method stub
		return new Object[]{
				"no", Integer.class,
				"member", spms.vo.Member.class};
	}
	@Override
	public String execute(Map<String, Object> model) throws Exception {

		//MemberDao memberDao = (MemberDao) model.get("memberDao");
		Member member = (Member) model.get("member");
		Integer no = (Integer) model.get("no");
		if (member.getEmail()== null) {
			
			member = memberDao.selectOne(no);
			model.put("member", member);
			return "/member/MemberUpdateForm.jsp";

		} else {
			
			memberDao.update(member);
			return "redirect:list.do";
		}
	}
}
