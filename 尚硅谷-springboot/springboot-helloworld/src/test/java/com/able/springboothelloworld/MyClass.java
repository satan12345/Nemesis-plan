package com.able.springboothelloworld;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

/**
 * @author jipeng
 * @date 2019-03-15 19:57
 * @description
 */
@Slf4j
public class MyClass {
    public static void main(String[] args){
        ModelMapper modelMapper = new ModelMapper();
        Address address=new Address();
        address.setCity("南京市");
        address.setStreet("中山大街");
        Name name=new Name();
        name.setFirstName("旗木");
        name.setLastName("卡卡西");
        Customer customer=new Customer();
        customer.setName(name);

        Order order=new Order();
        order.setBillingAddress(address);
        order.setCustomer(customer);
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        log.info("orderDto={}",orderDTO);
    }
}

@Data
class Order {
    Customer customer;
    Address billingAddress;
}
@Data
class Customer {
    Name name;
}
@Data
class Name {
    String firstName;
    String lastName;
}
@Data
class Address {
    String street;
    String city;
}
@Data
class OrderDTO {
    String customerFirstName;
    String customerLastName;
    String billingStreet;
    String billingCity;
}
