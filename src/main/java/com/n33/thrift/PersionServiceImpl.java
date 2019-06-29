package com.n33.thrift;

import com.n33.thrift.generated.DataException;
import com.n33.thrift.generated.Person;
import com.n33.thrift.generated.PersonService;
import org.apache.thrift.TException;
/**
 * 实现Iface
 *
 * @author N33
 * @date 2019/6/30
 */
public class PersionServiceImpl implements PersonService.Iface {

    @Override
    public Person getPersonByUsername(String username) throws DataException, TException {
        System.out.println("Got Client Param: " + username);

        /**
         * 可以链式编程
         */
        Person person = new Person();
        person.setUsername(username);
        person.setAge(20);
        person.setMarried(false);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DataException, TException {
        System.out.println("Got Client Param: ");

        System.out.println(person.getUsername());
        System.out.println(person.getAge());
        System.out.println(person.isMarried());

    }
}
