package com.example.myspringdemo.service.serviceImpl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.myspringdemo.entity.AddressBook;
import com.example.myspringdemo.entity.Employee;
import com.example.myspringdemo.mapper.AddressBookMapper;
import com.example.myspringdemo.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
