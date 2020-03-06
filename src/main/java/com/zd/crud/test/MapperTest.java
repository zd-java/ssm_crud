package com.zd.crud.test;

import java.util.UUID;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zd.crud.bean.Department;
import com.zd.crud.bean.Employee;
import com.zd.crud.dao.DepartmentMapper;
import com.zd.crud.dao.EmployeeMapper;

/**
 * 	����dao��Ĺ���
 * @author zd
 * 	�Ƽ�Spring����Ŀ�Ϳ���ʹ��Spring�ĵ�Ԫ���ԣ������Զ�ע��������Ҫ�����
 * 	1������SpringTest��ģ��
 * 	2��@ContextConfigurationָ��Spring�����ļ���λ��
 * 	3��ֱ��@AutowiredҪʹ�õ��������
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:applicationContext.xml"})
public class MapperTest {

	@Autowired
	private DepartmentMapper departmentMapper;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	/**
	 * ����DepartmentMapper
	 */
	@Test
	public void testCRUD() {
		/*//1������SpringIOC����
		ApplicationContext ioc = new ClassPathXmlApplicationContext("applicationContext.xml");
		//2���������л�ȡmapper
		DepartmentMapper bean = ioc.getBean(DepartmentMapper.class);*/
		
		System.out.println(departmentMapper);
		
		//1�����뼸������
//		departmentMapper.insertSelective(new Department(null,"������"));
//		departmentMapper.insertSelective(new Department(null,"���Բ�"));
		
		//2������Ա�����ݣ�����Ա������
//		employeeMapper.insertSelective(new Employee(null,"Jerry","M","Jerry@zd.com",1));
		
		//3������������Ա����������ʹ�ÿ���ִ������������sqlSession
		/*for() {
			employeeMapper.insertSelective(new Employee(null,"","M","Jerry@zd.com",1));
		}*/
		EmployeeMapper mapper = sqlSession.getMapper(EmployeeMapper.class);
		for(int i=0;i<1000;i++) {
			String uuid = UUID.randomUUID().toString().substring(0, 5) + i;
			mapper.insertSelective(new Employee(null, uuid, "M", uuid+"@zd.com", 1));
		}
		System.out.println("�������");
		
		
		
		
	}
}
