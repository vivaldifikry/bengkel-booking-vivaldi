package com.bengkel.booking.services;

import com.bengkel.booking.models.Customer;
import com.bengkel.booking.models.ItemService;
import com.bengkel.booking.models.MemberCustomer;
import com.bengkel.booking.models.Vehicle;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Validation {
	
	public static String validasiInput(String question, String errorMessage, String regex) {
		Scanner input = new Scanner(System.in);
	    String result;
	    boolean isLooping = true;
	    do {
			System.out.print(question);
			result = input.nextLine();

			//validasi menggunakan matches
			if (result.matches(regex)) {
				isLooping = false;
			}else {
				System.out.println(errorMessage);
			}

	    } while (isLooping);

	    return result;
	  }
	
	public static int validasiNumberWithRange(String question, String errorMessage, String regex, int max, int min) {
	    int result;
	    boolean isLooping = true;
	    do {
	      	result = Integer.valueOf(validasiInput(question, errorMessage, regex));
	      	if (result >= min && result <= max) {
	        	isLooping = false;
	      	}else {
	        	System.out.println("Pilihan angka " + min + " s.d " + max);
	      	}
	    } while (isLooping);

	    return result;
	  }

	public static boolean searchCostumerId(List<Customer> customerList, String id){
		for(Customer customers : customerList){
			if(Objects.equals(customers.getCustomerId(), id)){
				return true;
			}
		}
		return false;
	}

	public static String searchCostumerPassword(List<Customer> customerList, String id){
		String password = "";
		for(Customer customers : customerList){
			if(Objects.equals(customers.getCustomerId(), id)){
				password = customers.getPassword();
			}
		}
		return password;
	}

	public static boolean searchVehicleId(List<Customer> customerList, String customerId, String vehicleId){

		for (Customer customer : customerList) {
			if (Objects.equals(customer.getCustomerId(), customerId)) {
				for (Vehicle vehicle : customer.getVehicles()) {
					if (Objects.equals(vehicle.getVehiclesId(), vehicleId)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean searchServiceId(List<ItemService> itemServiceList, String pilihService){

		for (ItemService itemService : itemServiceList) {
			if (Objects.equals(itemService.getServiceId(), pilihService)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMember(List<Customer> customerList, String id){
		List<Customer> filteredCustomer = DataProcess.filterCustomerListById(customerList, id);

		for (Customer customer : filteredCustomer) {
			if(customer instanceof MemberCustomer)
				return true;
		}
		return false;
	}


}
