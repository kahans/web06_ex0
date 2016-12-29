package spms.listeners;

// 서버에서 제공하는 DateSource 사용하기
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import spms.controls.LogInController;
import spms.controls.LogOutController;
import spms.controls.MemberAddController;
import spms.controls.MemberDeleteController;
import spms.controls.MemberListController;
import spms.controls.MemberUpdateController;
import spms.dao.MySqlMemberDao;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();

			InitialContext initialContext = new InitialContext();
			DataSource ds = (DataSource) initialContext.lookup("java:comp/env/jdbc/studydb");
			System.out.println("시작 리스너");
			//MemberDao memberDao = new MemberDao();
			MySqlMemberDao memberDao = new MySqlMemberDao();
			// datasource를 Dao에 주입
			memberDao.setDataSource(ds);
			
			/*
			 * 1.공통 저장소 : dao
			 * 2.공통 저장소 : controller(+dao)
			 * */
			//sc.setAttribute("memberDao", memberDao);
			
			sc.setAttribute("/member/list.do", new MemberListController().setMemberDao(memberDao));
			sc.setAttribute("/member/add.do", new MemberAddController().setMemberDao(memberDao));
			sc.setAttribute("/member/update.do", new MemberUpdateController().setMemberDao(memberDao));
			sc.setAttribute("/member/delete.do", new MemberDeleteController().setMemberDao(memberDao));
			sc.setAttribute("/auth/login.do", new LogInController().setMemberDao(memberDao));
			sc.setAttribute("/auth/logout.do", new LogOutController().setMemberDao(memberDao));
			
			
			
			

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}
