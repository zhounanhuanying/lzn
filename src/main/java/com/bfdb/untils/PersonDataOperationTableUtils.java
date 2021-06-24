package com.bfdb.untils;

import com.bfdb.entity.PersonDataOperationTable;
import com.bfdb.entity.PersonTable;

import java.util.Date;

/**
 * 人员数据操作记录公共方法
 */
public class PersonDataOperationTableUtils {


    /**
     * 人员信息post添加
     * @param personTable
     * @return
     */
    public static PersonDataOperationTable psotPersonDataOperationTable(PersonTable personTable) {
        PersonDataOperationTable personDataOperationTable=new PersonDataOperationTable();
        personDataOperationTable.setPersoncode( personTable.getPersonCode() );
        personDataOperationTable.setOperationaction( "POST" );
        personDataOperationTable.setOperationtime( personTable.getCreateTime() );
        return personDataOperationTable;
    }

    /**
     * 修改人员信息put
     * @param personTable
     * @return
     */
    public static PersonDataOperationTable putPersonDataOperationTable(PersonTable personTable) {
        PersonDataOperationTable personDataOperationTable=new PersonDataOperationTable();
        personDataOperationTable.setPersoncode( personTable.getPersonCode() );
        personDataOperationTable.setOperationaction( "PUT" );
        personDataOperationTable.setOperationtime( personTable.getModifyTime() );
        return personDataOperationTable;
    }

    /**
     * 删除人员信息delete
     * @param personTable
     * @return
     */
    public static PersonDataOperationTable deletePersonDataOperationTable(PersonTable personTable) {
        PersonDataOperationTable personDataOperationTable=new PersonDataOperationTable();
        personDataOperationTable.setPersoncode( personTable.getPersonCode() );
        personDataOperationTable.setOperationaction( "DELETE" );
        personDataOperationTable.setOperationtime(new Date(  ) );
        return personDataOperationTable;
    }
}
