
package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Controllers;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Company;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Customer;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.UserDetails;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponCustomExceptions;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.AdminService;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.CompanyService;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.CustomerService;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.ClientType;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.util.JWTutil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping ("coupons") //http://locahost:8080/coupons
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
/**
 * a class that contains the methods of the Admin Controller Rest Api (post get Put Delete)
 */
public class AdminController {

    private  final JWTutil jwTutil;

    private final AdminService adminService;
    private final CompanyService companyService;
    private final CustomerService customerService;

    //GET ALL COMPANIES
    @GetMapping("getAllCompanies")//http://localhost:8080/coupons/getallcompanies
    public ResponseEntity<?> getCompanies(){
        return ResponseEntity.ok (adminService.getAllCompanies ());
    }

    @PostMapping("adminLogin")
    public ResponseEntity<?> adminLogin(@RequestBody UserDetails userData) {
        if (adminService.login(userData.getEmail(), userData.getPassword())) {
            String myToken = jwTutil.generateToken(new UserDetails(userData.getEmail(),0, ClientType.admin));
            return new ResponseEntity<>(myToken, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);    }
    //GET COMPANY LOGGED IN
    @GetMapping("companyLoggedIn") //http://localhost:8080/coupons/companyLoggedIn
    public ResponseEntity<?> getCompanyLoggedIn(){
        return ResponseEntity.ok (adminService.getCompany (companyService.getCompanyLoggedIn ()));
    }
    //GET ONE COMPANY
    @GetMapping("getOneCompany/{id}") //http://localhost:8080/coupons/getOneCompany/
    public ResponseEntity<?> getOneCompany(@PathVariable int id) {
        return ResponseEntity.ok(adminService.getCompanyById(id));
    }

    //ADD A NEW COMPANY
    @PostMapping("createcompany")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addCompany(@RequestBody Company company) throws CouponCustomExceptions {
        adminService.addCompany (company);
    }

//    @PostMapping("addCompany")
//    public ResponseEntity<?> addCompany(@RequestHeader(name = "Authorization") String token,@RequestBody Company company){
//        if (jwTutil.validateToken(token)) {
//            return ResponseEntity.ok()
//                    .header("Authorization", jwTutil.generateToken(new UserDetails(
//                            jwTutil.extractEmail(token),
//                            ClientType.admin
//                    )))
//                    .body(adminService.addCompany(company));
//        }
//        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//    }
    //UPDATE COMPANY
    @PutMapping("updateCompany")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateCompany(@RequestBody Company company) throws CouponCustomExceptions {
        try {
            adminService.updateCompany (company);
        } catch (CouponCustomExceptions e) {
            //return new ResponseEntity<>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
            throw new CouponCustomExceptions ("Data was already reported as updated");
        }
    }

    //Object->Json
    //DELETE COMPANY
    @DeleteMapping("deleteCompany/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable int id) {
        adminService.deleteCompany (id);
        System.out.println("Company " + id + " was deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //ADD NEW CUSTOMER
    @PostMapping("addCustomer")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addCustomer(@RequestBody Customer customer) throws CouponCustomExceptions {
        adminService.addCustomer (customer);
    }

    //UPDATE CUSTOMER
    @PostMapping("updateCustomer")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateCustomer(@RequestBody Customer customer) throws CouponCustomExceptions {
        try {
            adminService.updateCustomer (customer);
        } catch (CouponCustomExceptions e) {
            //return new ResponseEntity<>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
            throw new CouponCustomExceptions ("Data was already reported as updated");
        }
    }
    //DELETE ONE CUSTOMER
    @DeleteMapping("deleteCustomer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int id) throws CouponCustomExceptions {
        adminService.deleteCustomer(id);
        //System.out.println("Customer " + id + " was deleted");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //GET ALL CUSTOMERS
        @GetMapping("getallcustomers")
    public ResponseEntity<?> getCustomers(){
        return ResponseEntity.ok (adminService.getAllCustomers ());
    }
    //GET ONE CUSTOMER
    @GetMapping("getOneCustomer/{id}") //http://localhost:8080/coupons/getOneCustomer/
    public ResponseEntity<?> getOneCustomer(@PathVariable int id) {
        return ResponseEntity.ok(adminService.getOneCustomer (id));
    }


    @GetMapping("getOneCompany")
    public ResponseEntity<?> getOneCompany() {
        return ResponseEntity.ok(companyService.getCompanyLoggedIn ());
    }


}






