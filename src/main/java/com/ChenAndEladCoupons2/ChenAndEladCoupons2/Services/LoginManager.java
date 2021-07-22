package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.LoginException;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.ClientType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
@RequestMapping("admin")
/**
 * A class that check the user's details and allows access and instantiating an Object for it
 */
public class LoginManager {

    @Autowired
    private AdminService adminService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CustomerService customerService;
    private ClientService clientService;


    public ClientService login(String email, String password, ClientType clientType) throws LoginException {
        String failedLoginMsg = "the email and password do not match any client please try again";
        String loginSuccess = "you are now logged in as ";
        switch (clientType) {
            case admin:
                if (!adminService.login (email, password)) {
                    throw new LoginException (failedLoginMsg);
                }
                System.out.printf (loginSuccess + "'%s'\n", email);
                return adminService;
            case company:
                if (companyService.login (email, password)) {
                    System.out.printf (loginSuccess + "'%s'\n", email);
                    return companyService;
                }
                throw new LoginException (failedLoginMsg);
            case customer:
                if (!customerService.login (email, password)) {
                    throw new LoginException (failedLoginMsg);
                }
                System.out.printf (loginSuccess + "'%s'\n", email);
                return customerService;
            default:
                System.out.println ("thank you");
        }
        return clientService;
    }
}
