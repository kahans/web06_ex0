package spms.listeners;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import spms.controls.ApplicationContext;

// MySqlMemberDao �쟻�슜
@WebListener
public class ContextLoaderListener implements ServletContextListener {
	private static ApplicationContext applicationContext;
	//변수의 생명주기, static은 객체를 다른 객체를 생성하지 않기 위해 락시킨거다.
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext sc = event.getServletContext();
			String propertiesPath = sc.getRealPath( 
					sc.getInitParameter("contextConfigLocation"));
			
			
			applicationContext = new ApplicationContext(propertiesPath);

			// 객체관리 하는 코드를  삭제
			/*
			 * InitialContext initialContext = new InitialContext(); DataSource
			 * ds = (DataSource)initialContext.lookup(
			 * "java:comp/env/jdbc/studydb");
			 * 
			 * MySqlMemberDao memberDao = new MySqlMemberDao();
			 * memberDao.setDataSource(ds);
			 * 
			 * sc.setAttribute("/auth/login.do", new
			 * LogInController().setMemberDao(memberDao));
			 * sc.setAttribute("/auth/logout.do", new LogOutController());
			 * sc.setAttribute("/member/list.do", new
			 * MemberListController().setMemberDao(memberDao));
			 * sc.setAttribute("/member/add.do", new
			 * MemberAddController().setMemberDao(memberDao));
			 * sc.setAttribute("/member/update.do", new
			 * MemberUpdateController().setMemberDao(memberDao));
			 * sc.setAttribute("/member/delete.do", new
			 * MemberDeleteController().setMemberDao(memberDao));
			 */

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}
}
