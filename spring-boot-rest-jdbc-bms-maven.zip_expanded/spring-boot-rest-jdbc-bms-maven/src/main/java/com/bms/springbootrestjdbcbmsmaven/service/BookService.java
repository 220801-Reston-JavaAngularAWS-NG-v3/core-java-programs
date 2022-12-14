package com.bms.springbootrestjdbcbmsmaven.service;

import java.util.List;

import com.bms.springbootrestjdbcbmsmaven.exception.ApplicationException;
import com.bms.springbootrestjdbcbmsmaven.pojo.BookPojo;

public interface BookService {
	// C - Create
	// R - Read
	// U - Update
	// D - Delete
	
	// fetches all the books - Read
	List<BookPojo> getAllBooks()throws ApplicationException;
	
	// adds a new book - Create
	BookPojo addBook(BookPojo bookPojo)throws ApplicationException;
	
	// updates an existing book - Update
	BookPojo updateBook(BookPojo bookPojo)throws ApplicationException;
	
	// deletes an existing book - Delete
	void deleteBook(int bookId)throws ApplicationException;
	
	// fetches a book - Read
	BookPojo getABook(int bookId)throws ApplicationException;
	
}
