package com.xztx.security;

import com.xztx.dao.CustomerUserDao;
import com.xztx.model.CustomerUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerUserDao customerUserDao;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        logger.info("用户名为: " + name);
        // 1、根据name去数据库查询，是否存在
        CustomerUserDTO customerUserDTO = new CustomerUserDTO();
        customerUserDTO.setCustomerUserName(name);
        List<CustomerUserDTO> userList = customerUserDao.getUserList(customerUserDTO);
        if(userList == null || userList.size() < 1) {
            throw new UsernameNotFoundException("用户名找不到! ");
        }
        CustomerUserDTO userDTO = userList.get(0);
        // 2、数据查询出来的密码加密(一般存到数据库里的密码已经加过密了)
        String password = passwordEncoder.encode(userDTO.getCustomerUserPassword());

        // 3、封装用户信息和权限, 这个password密码，是数据库查询出来的密码
        return new User(name, password, AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN"));
    }
}
