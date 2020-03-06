package com.zd.crud.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zd.crud.bean.Employee;
import com.zd.crud.bean.Msg;
import com.zd.crud.service.EmployeeService;

/**
 * 	����Ա��CRUD����
 * @author zd
 *
 */
@Controller
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	/**
	 *	 Ա��ɾ������
	 *	������������һ
	 *	����ɾ����1-2-3
	 *	����ɾ����1
	 *	
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{ids}",method=RequestMethod.DELETE)
	public Msg deleteEmpById(@PathVariable("ids") String ids) {
		//����ɾ��
		if(ids.contains("-")) {
			String[] str_ids = ids.split("-");
			//��װid�ļ���
			List<Integer> del_ids = new ArrayList<Integer>();
			for(String string : str_ids) {
				del_ids.add(Integer.parseInt(string));
			}
			employeeService.deleteBatch(del_ids);
		//����ɾ��
		}else {
			Integer id = Integer.parseInt(ids);
			employeeService.deleteEmpById(id);
		}
		return Msg.success();
	}
	
	/**
	 * 	���ֱ�ӷ���ajax=PUT��ʽ������
	 * 	��װ������
	 * 	Employee [empId=1001, empName=null, gender=null, email=null, dId=null, department=null]
	 * 	
	 * 	���⣺
	 * 	�������������ݣ�
	 * 	����Employee������װ���ϣ�
	 * 	update tbl_emp	where emp_id = 1001
	 * 
	 * 	ԭ��
	 * 	Tomcat��
	 * 		1�����������е����ݣ���װһ��map��
	 * 		2��request.getparameter("empName")�ͻ�����map��ȡֵ��
	 * 		3��SpringMVC��װPOJO�����ʱ��
	 * 			���POJO��ÿ�����Ե�ֵ��request.getParameter("email");
	 * 	AJAX����PUT����������Ѫ����
	 * 		PUT�����������е����ݣ�request.getparameter("empName")�ò���
	 * 		Tomcatһ����PUT�Ͳ����װ�������е�����Ϊmap��ֻ��POST��ʽ������Ż��װ������Ϊmap
	 * 
	 * 	���������
	 * 	����Ҫ��֧��ֱ�ӷ���PUT֮�������Ҫ��װ�������е�����
	 * 	1�������ϣ�HttpPutFormContentFilter
	 * 	2���������ã����������е����ݽ�����װ��һ��map��
	 * 	3��request�����°�װ��request.getParameter()����д���ͻ���Լ���װ��map��ȡ����
	 * 
	 * 	Ա�����·���
	 * @param employee
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{empId}",method=RequestMethod.PUT)
	public Msg saveEmp(Employee employee,HttpServletRequest request) {
		System.out.println("�������е����ݣ�"+request.getParameter("gender"));
		System.out.println("��Ҫ���µ�Ա�����ݣ�"+employee);
		employeeService.updateEmp(employee);
		return Msg.success();
	}
	
	/**
	 * 	����Ա��id��ѯԱ����Ϣ
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/emp/{id}",method=RequestMethod.GET)
	public Msg getEmp(@PathVariable("id") Integer id) {
		Employee employee = employeeService.getEmp(id);
		return Msg.success().add("emp", employee);
	}
	
	/**
	 * 	����û����Ƿ����
	 * @param empName
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkUser")
	public Msg checkUser(@RequestParam("empName")String empName) {
		//���ж��û����Ƿ��ǺϷ��ı��ʽ
		String regex = "(^[a-zA-Z0-9_-]{6,16}$)|(^[\\u2E80-\\u9FFF]{2,5}$)";
		if(!empName.matches(regex)) {
			return Msg.fail().add("va_msg", "�û���������6-16λ���ֺ���ĸ����ϻ���2-5λ����");
		}
		
		//���ݿ��û����ظ�У��
		Boolean b = employeeService.checkUser(empName);
		if(b) {
			return Msg.success();
		}
		return Msg.fail().add("va_msg", "�û����ظ�");
	}
	
	/**
	 * 	Ա������
	 * 	1��֧��JSR303У��
	 * 	2������Hibernate-Validator
	 * @param employee
	 * @return
	 */
	@RequestMapping(value="/emp",method=RequestMethod.POST)
	@ResponseBody
	public Msg saveEmp(@Valid Employee employee,BindingResult result) {
		if(result.hasErrors()) {
			//У��ʧ�ܣ�Ӧ�÷���ʧ�ܣ���ģ̬������ʾУ��ʧ�ܵĴ�����Ϣ
			Map<String,Object> map = new HashMap<String,Object>();
			List<FieldError> errors = result.getFieldErrors();
			for(FieldError fieldError : errors) {
				System.out.println("������ֶ�����"+fieldError.getField());
				System.out.println("������Ϣ��"+fieldError.getDefaultMessage());
				map.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
			return Msg.fail().add("errorFields", map);
		}else {
			employeeService.saveEmp(employee);
			return Msg.success();
		}
	}
	
	/**
	 * 	Ҫʹ��@ResponseBody����Ҫ����Jackson��
	 * @param pn
	 * @return
	 */
	@RequestMapping("/emps")
	@ResponseBody
	public Msg getEmpsWithJson(@RequestParam(value="pn",defaultValue="1")Integer pn) {
		//�ⲻ��һ����ҳ��ѯ
		//����PageHelper��ҳ���
		//�ڲ�ѯ֮ǰֻ��Ҫ����PageHelper�е�startPage����������ҳ���Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		//startPage��������Ĳ�ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		//ʹ��PageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ�������
		//��װ����ϸ�ķ�ҳ��Ϣ�����������ǲ�ѯ��������ݣ�����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps,5);
		
		return Msg.success().add("pageInfo",page);
	}
	
	/**
	 * 	��ѯԱ������(��ҳ��ѯ)
	 * @return
	 */
//	@RequestMapping("/emps")
	public String getEmps(@RequestParam(value="pn",defaultValue="1")Integer pn,Model model) {
		//�ⲻ��һ����ҳ��ѯ
		//����PageHelper��ҳ���
		//�ڲ�ѯ֮ǰֻ��Ҫ����PageHelper�е�startPage����������ҳ���Լ�ÿҳ�Ĵ�С
		PageHelper.startPage(pn, 5);
		//startPage��������Ĳ�ѯ����һ����ҳ��ѯ
		List<Employee> emps = employeeService.getAll();
		//ʹ��PageInfo��װ��ѯ��Ľ����ֻ��Ҫ��pageInfo����ҳ�������
		//��װ����ϸ�ķ�ҳ��Ϣ�����������ǲ�ѯ��������ݣ�����������ʾ��ҳ��
		PageInfo page = new PageInfo(emps,5);
		model.addAttribute("pageInfo", page);
		
		return "list";
	}
	
}
