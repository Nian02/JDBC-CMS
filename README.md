# JDBC-CMS
利用JavaSE技术，进行控制台输出的客户管理系统！主要功能包括：**客户展示、客户删除、客户添加、客户修改、退出系统**。

## 上手指南

### 安装环境

1. 开发工具：IntelliJ IDEA
2. JDK版本：17
3. 项目编码：UTF-8
4. MySQL 8.0

### 安装步骤

**从GitHub中部署项目到本地**：

1. 在IDEA中选中file-->new--> Project from Version Conrtrol
2. 填写需要导入的github链接

**创建MySQL表**：

```SQL
CREATE TABLE t_customer(
  id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户主键',
  NAME VARCHAR(20) COMMENT '客户名称',
  gender VARCHAR(4) COMMENT '客户姓名',
  age INT COMMENT '客户年龄',
  salary DOUBLE(8,1) COMMENT '客户工资',
  phone VARCHAR(11) COMMENT '客户电话')
```

**设置properties文件**：

```
driverClassName=com.mysql.cj.jdbc.Driver  
username=root  
password=password  
url=jdbc:mysql:///github
```

>数据库连接的配置文件在`druid.properties`中，由JDBCTools进行读入。需要在配置文件中设置自己环境里正确的用户id和密码等内容，否则将会连接失败。

## 运行

在Main方法中直接运行，在控制台中根据菜单的指引进行增加、删除、查询客户信息等操作，完成对数据库的管理。

## 项目概述

在此对项目的结构以及采用的多线程的知识作一个简要的介绍：

- dao：CustomerDao类，继承BaseDao类，存放增删查改的具体实现方法（包装`update()`等方法，传入Customer类或用户id）
- Javabean：存放客户信息Customer类
- main：CustomerManger类，程序入口，创建CustomerView类型对象，使用`enterMainMenu()`方法
- service：**CustomerService类：提供增删查改服务**（本质上就是CustomerDao类的封装，增加异常机制）
- utils：存放工具类
	- BaseDao：存放数据库的具体动作的类，包括`update()`方法更新、`query()`方法查询返回集合、`queryBean()`方法查询返回对象；
	- JDBCTools：使得SQL语句能够通过**连接池**对数据库进行连接，包括`getConnection()`方法获取连接、`free()`方法释放连接。
- view：主控模块，负责菜单显示和用户交互
	- CustomerView类：提供用户交互的菜单
	- KeyboardUtility类：读取键盘输入信息

