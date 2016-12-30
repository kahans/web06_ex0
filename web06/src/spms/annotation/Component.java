package spms.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Component {//인터페이스랑 똑같은 클래스를 만들었고, 인터페이스 앞에 @가 붙어 있다. 그래서 애노테이션이다.

	String value() default "";	//mySqlMemberDao클래스에 @component(value="")작성시 
								//Component애노테이션이 value를 갖는다.
	
	
}
