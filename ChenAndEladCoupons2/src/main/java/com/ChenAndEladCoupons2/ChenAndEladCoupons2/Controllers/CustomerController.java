package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Controllers;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.UserDetails;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponCustomExceptions;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.CustomerService;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.Category;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.enums.ClientType;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.util.JWTutil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coupons") //http://locahost:8080/coupons
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
/**
 * a class that contains the methods of the Customer Controller Rest Api (post get Put Delete)
 */
public class CustomerController {

    private final CustomerService customerService;
    private final JWTutil jwTutil;
    @PostMapping("customerLogin")
    public ResponseEntity<?> customerLogin(@RequestBody UserDetails userData) {
        if (customerService.login(userData.getEmail(), userData.getPassword())) {
            String myToken = jwTutil.generateToken(new UserDetails( userData.getEmail(),customerService.getCustomerLoggedIn().getId(), ClientType.customer));
            return new ResponseEntity<>(myToken, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("purchaseCoupon")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void purchaseCoupon(@RequestBody Coupon coupon) throws CouponCustomExceptions {
        customerService.purchaseCoupon (coupon.getId());
    }

    //GET ALL COUPONS PER CUSTOMER
    @GetMapping("getCouponsByCustomer") // http://localhost:8080/search?id=1
    public ResponseEntity<?> getCouponsByCustomer(){
        return ResponseEntity.ok (customerService.getAllCouponsPerCustomer ());
    }

    //GET ALL COUPONS PER CUSTOMER BY CATEGORY
    @GetMapping("getCouponsPerCustomerByCategory") // http://localhost:8080/search?id=1
    public ResponseEntity<?> getCouponsPerCustomerByCategory(@RequestParam Category category){
        return ResponseEntity.ok (customerService.getAllCouponsByCategoryPerCustomer (category));
    }

    @GetMapping("getCouponsPerCustomerByMaxPrice") // http://localhost:8080/search?id=1
    public ResponseEntity<?> getCouponsPerCustomerByMaxPrice(@RequestParam int lowPrice, int maxPrice){
        return ResponseEntity.ok (customerService.getAllCouponsByMaxPricePerCustomer (maxPrice));
    }

    @GetMapping("getOneCustomer")
    public ResponseEntity<?> getOneCustomer(@RequestParam (defaultValue = "01")int id) {
        customerService.getOneCustomer (customerService.getCustomerLoggedIn ().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}


