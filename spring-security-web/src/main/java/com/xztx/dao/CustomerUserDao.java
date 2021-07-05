package com.xztx.dao;

import com.xztx.model.CustomerUserDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CustomerUserDao {

    List<CustomerUserDTO> getUserList(CustomerUserDTO customerUserDTO);

}
