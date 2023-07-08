package com.github.cms.service;

import com.github.cms.dao.CustomerDao;
import com.github.cms.javabean.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {


    private CustomerDao customerDao = new CustomerDao();

    /**
     * @Description: 返回所有客户对象
     * @param :
     * @return: java.util.List<com.atguigu.cms.javabean.Customer> 返回一个集合
     */

    public List<Customer> getList() {

        try {
            return customerDao.queryList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 添加新客户
     * @param customer: 要添加的客户对象
     * @return: void
     */

    public void addCustomer(Customer customer)  {
        try {
            customerDao.insertCustomer(customer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 返回指定id的客户对象记录
     * @param id: 客户id
     * @return: com.atguigu.cms.javabean.Customer 返回封装了客户信息的Customer对象
     */

    public Customer getCustomer(int id) {

        try {
            return customerDao.queryById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 改指定id号的客户对象的信息
     * @param id 客户id
     * @param cust: 封装了客户信息的Customer对象
     * @return: boolean 删除成功返回true
     */

    public boolean modifyCustomer(int id, Customer cust)  {
        int rows = 0;
        try {
            rows = customerDao.updateCustomer(cust);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rows > 0;
    }

    /**
     * @Description: 删除指定id号的的客户对象记录
     * @param id:    客户id
     * @return: boolean 删除成功返回true
     */

    public boolean removeCustomer(int id) {
        int rows = 0;
        try {
            rows = customerDao.deleteCustomer(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rows > 0;
    }

}
