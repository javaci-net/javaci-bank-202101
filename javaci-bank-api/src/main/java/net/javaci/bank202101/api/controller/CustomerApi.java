package net.javaci.bank202101.api.controller;

import java.security.Principal;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import net.javaci.bank202101.api.dto.CustomerListDto;
import net.javaci.bank202101.api.dto.CustomerSaveDto;
import net.javaci.bank202101.db.dao.CustomerDao;
import net.javaci.bank202101.db.model.Customer;
import net.javaci.bank202101.db.model.enumaration.CustomerStatusType;

@Slf4j
@RestController
@RequestMapping(CustomerApi.API_CUSTOMER_BASE_URL)
public class CustomerApi {

    static final String API_CUSTOMER_BASE_URL = "/api/customer";

    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @PostMapping("/register")
    private Long add(@RequestBody CustomerSaveDto customerSaveDto) {
        
        Optional<Customer> customerDBCheck = customerDao.findByCitizenNumber(customerSaveDto.getCitizenNumber());
        
        if(customerDBCheck.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Customer with given number already exists " + customerSaveDto.getCitizenNumber() );
        }
        
        Customer customer = modelMapper.map(customerSaveDto, Customer.class);
        // TODO Encrypt password
        
        customerDao.save(customer);
        
        log.info("Customer added with {} id", customer.getId());
        
        return customer.getId();
    }
    
    @PutMapping("/update/{citizenNumber}")
    private boolean update(
        @PathVariable("citizenNumber") String citizenNumber, 
        @RequestBody CustomerSaveDto customerSaveDto
    ) {
        Optional<Customer> existingCustomer = customerDao.findByCitizenNumber(citizenNumber);
        if(existingCustomer.isEmpty()) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Given citizen number to update does not exist " + citizenNumber
           );
        }
        
        if(!customerSaveDto.getCitizenNumber().equals(citizenNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Citizen number cannjot be changed"
            );
        }
        
        Customer customer = modelMapper.map(customerSaveDto, Customer.class);
        customer.setId(existingCustomer.get().getId());
        
        customerDao.save(customer);
        
        return true;
    }
    
    @GetMapping("/getInfo")
    private CustomerListDto getInfo(String citizenNumber) {
        Optional<Customer> existingCustomer = customerDao.findByCitizenNumber(citizenNumber);
        if(existingCustomer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                 "Given citizen number not found " + citizenNumber
            );
        }
        return modelMapper.map(existingCustomer.get(), CustomerListDto.class);
    }
}
