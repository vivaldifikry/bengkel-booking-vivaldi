package com.bengkel.booking.services;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.MemberCustomer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DataProcess {
    static Scanner input = new Scanner(System.in);

    public static List<Customer> filterCustomerListById(List<Customer> customerList, String id){
        List<Customer> chosed = new ArrayList<>();

        for (Customer iscustomer : customerList) {
            if (iscustomer.getCustomerId().equalsIgnoreCase(id)) {
                chosed.add(iscustomer);
            }
        }
        return chosed;
    }

    public static void topUpSaldo(List<Customer> customerList, String id){
        List<Customer> chosed = filterCustomerListById(customerList, id);

        System.out.println("Masukan besaran Top Up:");
        double topUpSaldo = input.nextDouble();

        for (Customer customer : chosed) {
            if (customer instanceof MemberCustomer) {
                MemberCustomer memberCustomer = (MemberCustomer) customer;
                double currentSaldo = memberCustomer.getSaldoCoin();
                memberCustomer.setSaldoCoin(currentSaldo + topUpSaldo);
            } else {
                // Jika Customer bukan instance dari MemberCustomer, mungkin kita ingin melakukan sesuatu yang lain,
                // misalnya menampilkan pesan bahwa top-up hanya berlaku untuk MemberCustomer.
                System.out.println("Maaf fitur ini hanya untuk Member saja");
            }
        }


    }


}
