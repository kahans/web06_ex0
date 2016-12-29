package spms.controls;

import java.util.Map;

import spms.bind.DataBinding;
import spms.dao.MemberDao;

public class MemberDeleteController implements Controller,DataBinding{
	private MemberDao memberDao;
	public MemberDeleteController setMemberDao(MemberDao memberDao){
		this.memberDao = memberDao;
		return this;
	}
	public Object[] getDataBinders() {
		// TODO Auto-generated method stub
		return new Object[]{"no", Integer.class};
	}
	@Override
	public String execute(Map<String, Object> model) throws Exception {
		//MemberDao memberDao = (MemberDao) model.get("memberDao");

		Integer no = (Integer) model.get("no");
		memberDao.delete(no);

		return "redirect:list.do";
	}
}
