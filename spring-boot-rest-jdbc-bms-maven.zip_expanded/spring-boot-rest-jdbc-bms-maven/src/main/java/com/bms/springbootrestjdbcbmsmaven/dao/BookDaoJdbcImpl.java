package com.bms.springbootrestjdbcbmsmaven.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bms.springbootrestjdbcbmsmaven.exception.ApplicationException;
import com.bms.springbootrestjdbcbmsmaven.pojo.BookPojo;

@Repository
public class BookDaoJdbcImpl implements BookDao {

	// Logging Step 1 - obtain an instance of Logger
	private static final Logger logger = LoggerFactory.getLogger(BookDaoJdbcImpl.class);

	// next week once we have session on Collections we will change the return type
	// of this method to a collection
	@Override
	public List<BookPojo> getAllBooks() throws ApplicationException {
		// Logging Step 2 - use the logger and log the information in each method
		logger.info("Entered getAllBooks() of dao layer...");

		Connection connection = DBUtil.makeConnection();
		List<BookPojo> allBooks = new ArrayList<BookPojo>();
		try {

			Statement stmt = connection.createStatement();
			String query = "SELECT * FROM book_details";
			ResultSet rs = stmt.executeQuery(query);

			while (rs.next()) {
				// creating a book pojo object and copying each record from the result set into
				// the book pojo
				BookPojo bookPojo = new BookPojo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getInt(5), rs.getString(6));
				// adding the book pojo to the collection
				allBooks.add(bookPojo);

			}

			// commented because collections is used

//			Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//			String query = "SELECT * FROM book_details";
//			ResultSet rs = stmt.executeQuery(query);
//			
//			// create an array whose size is the same as the number of record available in the rs
//			
//			// first let us find out the number of records in the rs
//			int counter = 0;
//			while(rs.next()) {
//				counter++;
//			}
//			// now create the BookPojo array
//			fetchedBooks = new BookPojo[counter];
//			
//			// iterating through the rs and copying it into the array
//			int i = 0;
//			rs.beforeFirst();
//			while(rs.next()) {
//				fetchedBooks[i] = new BookPojo();
//				fetchedBooks[i].setBookId(rs.getInt(1));
//				fetchedBooks[i].setBookTitle(rs.getString(2));
//				fetchedBooks[i].setBookAuthor(rs.getString(3));
//				fetchedBooks[i].setBookGenre(rs.getString(4));
//				fetchedBooks[i].setBookCost(rs.getInt(5));
//				fetchedBooks[i].setBookImageUrl(rs.getString(6));	
//				i++;
//			}
		} catch (SQLException e) {
			throw new ApplicationException();
		}
		// return the array of book pojo objects
		// return fetchedBooks;

		// return the collection of book pojo
		return allBooks;
	}

	@Override
	public BookPojo addBook(BookPojo bookPojo) throws ApplicationException {
		// Logging Step 2 - use the logger and log the information in each method
		logger.info("Entered addBook() of dao layer...");

		Connection connection = DBUtil.makeConnection(); // step 1 and 2 is done in this

		// String query = "INSERT INTO book_details(book_title, book_author, book_genre,
		// book_cost, book_image_url) VALUES(?, ?, ?, ?, ?)";
		String query = "INSERT INTO book_details(book_title, book_author, book_genre, book_cost, book_image_url) VALUES('"
				+ bookPojo.getBookTitle() + "','" + bookPojo.getBookAuthor() + "','" + bookPojo.getBookGenre() + "',"
				+ bookPojo.getBookCost() + ",'" + bookPojo.getBookImageUrl() + "') RETURNING book_id";
		try {
			// PreparedStatement pstmt = connection.prepareStatement(query);
			Statement stmt = connection.createStatement();

			// pstmt.executeUpdate();

			// what is pending is , get the auto generated book id and set it into the
			// bookPojo and return the book pojo - completed
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			int autoGeneratedBookId = rs.getInt(1);
			bookPojo.setBookId(autoGeneratedBookId);
		} catch (SQLException e) {
			throw new ApplicationException();
		}
		return bookPojo;
	}

	@Override
	public BookPojo updateBook(BookPojo bookPojo) throws ApplicationException {

		logger.info("Entered updateBook() of dao layer...");

		Connection connection = DBUtil.makeConnection(); // step 1 and 2 is done in this

		String query = "UPDATE book_details SET book_cost=? WHERE book_id=?";
		try {
			PreparedStatement pstmt = connection.prepareStatement(query);

			pstmt.setInt(1, bookPojo.getBookCost());
			pstmt.setInt(2, bookPojo.getBookId());

			pstmt.executeUpdate();
		} catch (SQLException e) { // we have caught SQLException
			throw new ApplicationException(); // and we are rethrowing it as ApplicationException
		}
		return bookPojo;
	}

	@Override
	public void deleteBook(int bookId) throws ApplicationException {
		logger.info("Entered deleteBook() of dao layer...");

		Connection connection = DBUtil.makeConnection();
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			String query = "DELETE FROM book_details WHERE book_id=" + bookId;
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			throw new ApplicationException();
		}

	}

	@Override
	public BookPojo getABook(int bookId) throws ApplicationException {
		logger.info("Entered getABook() of dao layer...");

		Connection connection = DBUtil.makeConnection();
		Statement stmt = null;
		BookPojo bookPojo = null;
		try {
			stmt = connection.createStatement();
			String query = "SELECT * FROM book_details WHERE book_id=" + bookId;
			ResultSet rs = stmt.executeQuery(query);
			// traverse the rs
			// as i traverse i would copy the contents into a book pojo object
			while (rs.next()) {
				bookPojo = new BookPojo();
				bookPojo.setBookId(rs.getInt(1));
				bookPojo.setBookTitle(rs.getString(2));
				bookPojo.setBookAuthor(rs.getString(3));
				bookPojo.setBookGenre(rs.getString(4));
				bookPojo.setBookCost(rs.getInt(5));
				bookPojo.setBookImageUrl(rs.getString(6));
			}
		} catch (SQLException e) {
			throw new ApplicationException();
		}
		return bookPojo;
	}

}