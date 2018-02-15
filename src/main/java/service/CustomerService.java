package service;

import model.Customer;

import java.util.List;

public class CustomerService implements GenericService<Customer, Integer> {
    @Override
    public boolean save(Customer customer) {
        return false;
    }

    @Override
    public Customer getById(Integer integer) {
        return null;
    }

    @Override
    public List<Customer> getAll() {
        return null;
    }

    @Override
    public boolean update(Integer integer, Customer customer) {
        return false;
    }

    @Override
    public boolean remove(Customer customer) {
        return false;
    }
}
