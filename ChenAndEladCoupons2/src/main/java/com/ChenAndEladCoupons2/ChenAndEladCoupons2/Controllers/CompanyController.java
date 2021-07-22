package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Controllers;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Coupon;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.UserDetails;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Exceptions.CouponCustomExceptions;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.AdminService;
import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Services.CompanyService;
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
 * a class that contains the methods of the Company Controller Rest Api (post get Put Delete)
 */
public class CompanyController {

    private final CompanyService companyService;
    private final AdminService adminService;
    private final JWTutil jwTutil;

    @PostMapping("companyLogin")
    public ResponseEntity<?> companyLogin(@RequestBody UserDetails userData) {
        if (companyService.login(userData.getEmail(), userData.getPassword())) {
            String myToken = jwTutil.generateToken(new UserDetails(userData.getEmail(), companyService.getCompanyLoggedIn().getId(), ClientType.company));
            return new ResponseEntity<>(myToken, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("addCoupon")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void addCoupon(@RequestBody Coupon coupon) throws CouponCustomExceptions {
        companyService.addCoupon(coupon);
    }

//    @DeleteMapping("deleteCoupon/{id}")
//    @ResponseStatus(code = HttpStatus.OK)
//    public ResponseEntity<?> deleteCoupon(@PathVariable int CouponID) {
//        companyService.deleteCoupon(CouponID);
//        System.out.println("Coupon " + CouponID + " was deleted");
//        return ResponseEntity.ok(companyService.deleteCoupon(CouponID));
//    }
    @DeleteMapping("deleteCoupon/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteCompany(@PathVariable int id) {
        companyService.deleteCoupon(id);
    }

    @PutMapping("updateCoupon")
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public void updateCoupon(@RequestBody Coupon coupon) throws CouponCustomExceptions {
        try {
            companyService.updateCoupon(coupon);
        } catch (CouponCustomExceptions e) {
            //return new ResponseEntity<>(e.getMessage(), HttpStatus.ALREADY_REPORTED);
            throw new CouponCustomExceptions("Data was already reported as updated");
        }
    }

    //GET ALL COUPONS PER COMPANY
    @GetMapping("getCouponsByCompany/{id}") // http://localhost:8080/search?id=1
    public ResponseEntity<?> getCouponsByCompany(@PathVariable int id) {
        return ResponseEntity.ok(companyService.getAllCouponsPerCompany(id));
    }

    @GetMapping("getCouponsByCompany") // http://localhost:8080/search?id=1
    public ResponseEntity<?> getCouponsByCompany() {
        return ResponseEntity.ok(companyService.getAllCouponsPerCompany());
    }

    //GET ONE COMPANY LOGGED IN
    @GetMapping("getOneCompanyLoggedIn") //http://localhost:8080/coupons/getOneCompanyLoggedIn
    public ResponseEntity<?> getCompanyThatLoggedIn() {
        return ResponseEntity.ok(companyService.getCompanyThatLoggedIn());
    }

    //GET ALL COUPONS PER COMPANY BY CATEGORY
    @GetMapping("getCouponsByCategory") // http://localhost:8080/search?id=1
    public ResponseEntity<?> getCouponsByCategory(@RequestParam Category category) {
        return ResponseEntity.ok(companyService.getAllCouponsByCategoryPerCompany(companyService.getCompanyLoggedIn().getId(), category));
    }

    //GET ALL COUPONS PER COMPANY BY MAX PRICE
    @GetMapping("getCouponsByMaxPrice") // http://localhost:8080/search?id=1
    public ResponseEntity<?> getCouponsByMaxPrice(@RequestParam int lowPrice, int maxPrice) {
        return ResponseEntity.ok(companyService.getAllCouponsByPricePerCompany(companyService.getCompanyLoggedIn().getId(), lowPrice, maxPrice));
    }
}


