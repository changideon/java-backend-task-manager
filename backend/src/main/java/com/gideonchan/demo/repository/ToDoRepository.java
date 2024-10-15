package com.gideonchan.demo.repository;

import com.gideonchan.demo.data.ToDo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ToDoRepository extends MongoRepository<ToDo, String> {
}