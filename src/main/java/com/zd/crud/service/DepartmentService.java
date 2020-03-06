package com.zd.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zd.crud.bean.Department;
import com.zd.crud.dao.DepartmentMapper;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentMapper departmentMapper;

	/**
	 * 查询所有的部门信息
	 * @return
	 */
	public List<Department> getDepts() {
		
		List<Department> list = departmentMapper.selectByExample(null);
		return list;
	}
	
}
