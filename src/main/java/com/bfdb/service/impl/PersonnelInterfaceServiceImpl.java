package com.bfdb.service.impl;

import com.bfdb.entity.PersonnelInterface;
import com.bfdb.mapper.PersonnelInterfaceMapper;
import com.bfdb.service.PersonnelInterfaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonnelInterfaceServiceImpl implements PersonnelInterfaceService {

    @Autowired
    PersonnelInterfaceMapper personnelInterfaceMapper;

    @Override
    public List<PersonnelInterface> selectPersonnelInterfaceAll() {
        return personnelInterfaceMapper.selectPersonnelInterfaceAll();
    }
}
