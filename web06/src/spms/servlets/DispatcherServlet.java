package spms.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import spms.bind.DataBinding;
import spms.bind.ServletRequestDataBinder;
import spms.controls.*;
import spms.vo.Member;

@SuppressWarnings("serial")
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		String servletPath = request.getServletPath();
		try {

			Controller controller = null;
			ServletContext sc = this.getServletContext();
			Map<String, Object> model = new HashMap<String, Object>();
			// model 안에 memberDao 주입
			// 문제 1 : 모든 컨트롤러가 dao를 주입받게 된다.
			// model.put("memberDao", sc.getAttribute("memberDao"));
			// System.out.println(sc.getAttribute("memberDao")+" : sc 체크");

			// begin = 요청분기 & pageController 결정
			model.put("session", request.getSession());
			controller = (Controller) sc.getAttribute(servletPath);

			// 다형성 연산자 instanceof(객체[왼쪽]+instanceof+타입[오른쪽] 위치)
			if (controller instanceof DataBinding) {
				// 모델객체를 자동으로 만들어 주는 메서드를 호출한다.
				// controller.getDataBinders();()호출 필요한 모델타입을 풀어낸다.
				Object[] dataBinders = ((DataBinding) controller).getDataBinders();
				// 만들어야 할 객체타입 객체 이름
				String dataName = null;
				Class<?> dataType = null;
				Object dataObj = null;
				for (int i = 0; i < dataBinders.length; i += 2) {
					dataName = (String) dataBinders[i];
					dataType = (Class<?>) dataBinders[i + 1];
					// some매서드 호출 -> dataType+request
					dataObj = ServletRequestDataBinder.bind(request, dataType, dataName);
					// name -> dataType.setName(name값);
					model.put(dataName, dataObj);
				}
			}

			// 컨트롤 호출을 통해 view 이름을 리턴 받습니다.
			System.out.println("controller확인 : " + controller);
			String viewUrl = controller.execute(model);
			// 맵 안에 있는 내용
			// map -> request.attribute로 옮겨간다.
			for (String key : model.keySet()) {
				request.setAttribute(key, model.get(key));
			}
			if (viewUrl.startsWith("redirect:")) {
				response.sendRedirect(viewUrl.substring(9));
				return;

			} else {
				RequestDispatcher rd = request.getRequestDispatcher(viewUrl);
				rd.include(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", e);
			RequestDispatcher rd = request.getRequestDispatcher("/Error.jsp");
			rd.forward(request, response);
		}
	}
}
