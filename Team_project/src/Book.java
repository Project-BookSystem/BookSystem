import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Book {
	
	Scanner sc = new Scanner(System.in);
	
	public void control() {
		while (true) {
			System.out.println("[도서관리]");
			System.out.print("> 작업을 선택하십시오. [c:도서등록, u:도서정보수정, r:도서목록, d:도서삭제, x:종료] : ");
			String choice = sc.nextLine();
			
			if (choice.equals("x") || choice.equals("X")) {
				break;
			}
			switch (choice) {
			case "c","C":
				this.add();
				break;
			case "u","U":
				this.updated();
				break;
			case "r","R":
				System.out.println("> 도서 목록조회");
				System.out.print("> 작업을 선택하십시오 [a:전체도서목록, s:도서검색, x:종료] : ");
				String cho = sc.nextLine();

				if (cho.equals("x") || cho.equals("X")) {
					break;
				} else if (cho.equals("a") || cho.equals("A")) {
					this.readAll();
					break;
				} else if (cho.equals("s") || cho.equals("S")) {
					this.read();
					break;
				} else {
					System.out.println("> 잘못된 입력입니다.\n");
					break;
				}
						
			case "d","D":
				System.out.println("> 도서 삭제");
				this.delete();
				break;
			default:
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				break;
			}
		}
	}
	
	private void add() {
		while (true) {
			System.out.println("> 도서 등록 [x:취소]");
			System.out.print(">> 제목 : ");
			String name = sc.nextLine();
			if (name.equalsIgnoreCase("X")) return;
			if (name.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 저자 : ");
			String author = sc.nextLine();
			if (author.equalsIgnoreCase("X")) return;
			if (author.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 출판사 : ");
			String publisher = sc.nextLine();
			if (publisher.equalsIgnoreCase("X")) return;
			if (publisher.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 출간년도 : ");
			String year = sc.nextLine();
			if (year.equalsIgnoreCase("X")) return;
			if (year.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> isbn 코드 : ");
			String isbn = sc.nextLine();
			if (isbn.equalsIgnoreCase("X")) return;
			if (isbn.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 보유수량 : ");
			String qty = sc.nextLine();
			if (qty.equalsIgnoreCase("X")) return;
			if (qty.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			if (!Main.isNumber(qty)) {
				break;
			}
			int total_qty = Integer.parseInt(qty);
			
			if (total_qty < 1) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}

			DBConnection.setConnection();
			try {
				String sql = "INSERT INTO book(title,author,isbn,publisher,issue_year,total_qty) VALUES(?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
				pstmt.setString(1, name);
				pstmt.setString(2, author);
				pstmt.setString(3, isbn);
				pstmt.setString(4, publisher);
				pstmt.setString(5, year);
				pstmt.setInt(6, total_qty);

				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("> 도서 등록이 완료되었습니다.");
				}
				pstmt.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			DBConnection.disConnection();
			
			break;
		}
		
	}
	
	private void updated() {
		System.out.println("> 도서정보 수정");
		System.out.print("> 수정할 도서의 제목을 입력하십시오. [x:종료] : ");
		String name = sc.nextLine().toUpperCase();
		if (name.equalsIgnoreCase("X")) return;

		System.out.print("> 수정할 항목을 선택하십시오. [1.제목 2.저자 3.출판사 4.출간년도 5.ISBN 6.수량 x:종료] : ");
		String choice = sc.nextLine();
		if (choice.equals("X")) return;

		String column = null;
		String newValue = null;

		switch (choice) {
		case "1":
			column = "title";
			System.out.print(">> 새 제목 : ");
			newValue = sc.nextLine();
			break;
		case "2":
			column = "author";
			System.out.print(">> 새 저자 : ");
			newValue = sc.nextLine();
			break;
		case "3":
			column = "publisher";
			System.out.print(">> 새 출판사 : ");
			newValue = sc.nextLine();
			break;
		case "4":
			column = "issue_year";
			System.out.print(">> 새 출판년도 : ");
			newValue = sc.nextLine();
			break;
		case "5":
			column = "isbn";
			System.out.print(">> 새 ISBN 코드 : ");
			newValue = sc.nextLine();
			break;
		case "6":
			column = "total_qty";
			System.out.print(">> 새 도서 수량 : ");
			newValue = sc.nextLine();
			if (!Main.isNumber(newValue) && newValue=="0") {
				System.out.println("> 잘못된 입력입니다. 숫자를 입력하십시오.");
				return;
			}
			break;
		default:
			System.out.println("> 잘못된 선택입니다.");
			return;
		}
		DBConnection.setConnection();
		String sql = "UPDATE book SET " + column + " = ? WHERE title = ?";

		try (PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql)) {
			if (column.equals("total_qty")) {
				pstmt.setInt(1, Integer.parseInt(newValue));
			} else {
				pstmt.setString(1, newValue);
			}
			pstmt.setString(2, name);

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("> 도서 정보가 수정되었습니다.");
			} else {
				System.out.println("> 해당 도서를 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			System.out.println("데이터베이스 오류 발생: " + e.getMessage());
		}
		DBConnection.disConnection();
	}
	
	private void readAll() {
		System.out.println("> 전체 목록보기");
		DBConnection.setConnection();
		try {
			Statement st = DBConnection.conn.createStatement();
			String sql = "SELECT book_id,title,author,publisher,issue_year,total_qty,qty FROM book";

			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				System.out.println(rs.getInt("book_id")+". "+rs.getString("title")+"\t"+rs.getString("author")+"\t"+rs.getString("publisher")
				+"\t"+rs.getString("issue_year")+"\t"+rs.getString("total_qty")+"\t"+rs.getString("qty"));
			}
			System.out.println("");
			
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		DBConnection.disConnection();
	}
	
	private void read() {
	    System.out.println("> 도서정보 검색");
	    while (true) {
	        System.out.print(">> 검색할 항목을 선택하십시오. [1.제목, 2.저자, 3.출판사, x:종료] : ");
	        String choice = sc.nextLine().toUpperCase();
	        
	        if (choice.equals("X")) {
	            break;
	        }

	        String column = null;
	        String newValue = null;
	        
	        switch (choice) {
	            case "1": // 제목
	                column = "title";
	                System.out.print(">> 도서의 제목을 입력하십시오 : ");
	    			newValue = sc.nextLine();
	                break;
	            case "2": // 저자
	                column = "author";
	                System.out.print(">> 도서의 저자를 입력하십시오 : ");
	    			newValue = sc.nextLine();
	                break;
	            case "3": // 출판사
	                column = "publisher";
	                System.out.print(">> 도서의 출판사를 입력하십시오 : ");
	    			newValue = sc.nextLine();
	                break;
	            default:
	                System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
	                continue;
	        }

	        // DB 연결 및 SQL 실행
	        DBConnection.setConnection();
	        String sql = "SELECT book_id, title, author, publisher, issue_year, total_qty, qty FROM book WHERE " + column + "=?";
	        try (PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql)) {
	            pstmt.setString(1, newValue);
	            ResultSet rs = pstmt.executeQuery();
	            
	            boolean found = false;
	            while (rs.next()) {
	                found = true;
	                System.out.println("대여번호: " + rs.getInt("book_id") + 
	                                   ", 제목: " + rs.getString("title") + 
	                                   ", 저자: " + rs.getString("author") + 
	                                   ", 출판사: " + rs.getString("publisher") + 
	                                   ", 출판년도: " + rs.getString("issue_year") + 
	                                   ", 총 보유수량: " + rs.getString("total_qty") + 
	                                   ", 빌려간 수량: " + rs.getString("qty"));
	            }
	            
	            if (!found) {
	                System.out.println("> 해당 항목의 도서를 찾을 수 없습니다.\n");
	            }
	            
	            rs.close();
	            
	        } catch (SQLException e) {
	            System.out.println("데이터베이스 오류 발생: " + e.getMessage());
	        }
	        DBConnection.disConnection();

	        System.out.println(""); // 추가적인 공백 출력
	    }
	}
	
	private void delete() {
		while (true) {
			System.out.print(">> 삭제할 도서의 제목을 입력하십시오 [x:종료]: ");
			String title = sc.nextLine();
			if (title.equals("x") || title.equals("X")) {
				break;
			}
			if (title.isEmpty()) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주세요.\n");
				continue;
			}
			
			// 테이블에 존재하지 않는 제목일 경우 메세지 출력?

			DBConnection.setConnection();
			try {
				String sql = "DELETE FROM book WHERE title=?";
				PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
				pstmt.setString(1, title);

				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("> 도서 삭제가 완료되었습니다.");
				} else {
					System.out.println("> 해당 도서를 찾을 수 없습니다.");
				}
				pstmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			DBConnection.disConnection();
		}
	}
}
