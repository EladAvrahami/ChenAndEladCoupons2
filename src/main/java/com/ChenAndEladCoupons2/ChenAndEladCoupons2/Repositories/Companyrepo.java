package com.ChenAndEladCoupons2.ChenAndEladCoupons2.Repositories;

import com.ChenAndEladCoupons2.ChenAndEladCoupons2.Beans.Company;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * an interface that connects by smart dialect to the Company Table in the DB
 */
public interface Companyrepo extends JpaRepository<Company,Integer> {

    Company findCompanyById(int id);
    Company findByEmailAndPassword(String email, String password);
    Company findCompanyByNameOrEmail(String name, String email);
    Company findByNameOrEmail(String name, String email);
}
