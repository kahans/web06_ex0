package spms.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Component {//�������̽��� �Ȱ��� Ŭ������ �������, �������̽� �տ� @�� �پ� �ִ�. �׷��� �ֳ����̼��̴�.

	String value() default "";	//mySqlMemberDaoŬ������ @component(value="")�ۼ��� 
								//Component�ֳ����̼��� value�� ���´�.
	
	
}
