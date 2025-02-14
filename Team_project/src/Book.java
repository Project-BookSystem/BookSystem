import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Book {
	
	Scanner sc = new Scanner(System.in);
	
	public void control() {
		while (true) {
			System.out.println("[도서 관리]");
			System.out.print("> 작업선택 [c:도서등록, u:도서정보수정, r:도서목록, d:도서삭제, x:종료] : ");
			String choice = sc.nextLine();
			
			if (choice.equals("x") || choice.equals("X")) {
				break;
			}
			switch (choice) {
			case "c","C":
				System.out.println("> 도서 등록");
				this.add();
				break;
			case "u","U":
				System.out.println("> 도서 정보수정");
				this.updated();
				break;
			case "r","R":
				System.out.println("> 도서 목록조회");
				System.out.print("> 작업선택 [a:전체도서목록, s:도서검색, x:종료] : ");
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
					System.out.println("> 잘못된 입력입니다.");
					break;
				}
						
			case "d","D":
				System.out.println("> 도서 삭제");
				this.delete();
				break;
			default:
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.");
				break;
			}
		}
	}
	
	private void add() {
		while (true) {
			System.out.print(">> 추가하실 도서의 제목을 입력해주십시오 [x:종료] : ");
			String name = sc.nextLine();
			if (name.equalsIgnoreCase("X")) return;
			if (name.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 추가하실 도서의 저자를 입력해주십시오 [x:종료] : ");
			String author = sc.nextLine();
			if (author.equalsIgnoreCase("X")) return;
			if (author.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 추가하실 도서의 출판사를 입력해주십시오 [x:종료] : ");
			String publisher = sc.nextLine();
			if (publisher.equalsIgnoreCase("X")) return;
			if (publisher.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 추가하실 도서의 출간년도를 입력해주십시오 [x:종료] : ");
			String year = sc.nextLine();
			if (year.equalsIgnoreCase("X")) return;
			if (year.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 추가하실 도서의 isbn코드를 입력해주십시오 [x:종료] : ");
			String isbn = sc.nextLine();
			if (isbn.equalsIgnoreCase("X")) return;
			if (isbn.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print(">> 추가하실 도서의 보유수량을 입력해주십시오 [x:종료] : ");
			String qty = sc.nextLine();
			if (qty.equalsIgnoreCase("X")) return;
			if (qty.equals("")) {
				System.out.println("> 잘못된 입력입니다.\n");
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
		while (true) {
			System.out.print("수정할 도서의 제목을 입력하십시오 [x:종료] : ");
			String name = sc.nextLine();
			if (name.equals("x") || name.equals("X")) {
				break;
			}
			if (name.equals("")) {
				continue;
			}
			
			// 제목이 일치하는 로우 불러와서 보여주기
			System.out.print("수정할 항목 선택 [t:제목, a:저자, p:출판사, y:출간년도, i:isbn코드, q:수량 x:종료] : ");
			String choice = sc.nextLine();
			
			if (choice.equals("x") || choice.equals("X")) {
				break;
			}
			switch (choice) {
			case "t","T":
				// 새 값 할당받는 코드
				System.out.println("변경할 제목을 입력하십시오 : ");
				String title = sc.nextLine();
				
				DBConnection.setConnection();
				try {
					String sql = "UPDATE book SET title=? WHERE title=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, title);
					pstmt.setString(2, name);
					
					int rowsInserted = pstmt.executeUpdate();
					if (rowsInserted > 0) {
						System.out.println("> 제목 수정이 완료되었습니다.");
					}
					pstmt.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
				
			case "a","A":
				System.out.println("변경할 저자를 입력하십시오 : ");
				String author = sc.nextLine();
				
				DBConnection.setConnection();
				try {
					String sql = "UPDATE book SET author=? WHERE title=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, author);
					pstmt.setString(2, name);
					
					int rowsInserted = pstmt.executeUpdate();
					if (rowsInserted > 0) {
						System.out.println("> 저자 수정이 완료되었습니다.");
					}
					pstmt.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
			
			case "p","P":
				System.out.println("변경할 출판사를 입력하십시오 : ");
				String publisher = sc.nextLine();
				
				DBConnection.setConnection();
				try {
					String sql = "UPDATE book SET publisher=? WHERE title=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, publisher);
					pstmt.setString(2, name);
					
					int rowsInserted = pstmt.executeUpdate();
					if (rowsInserted > 0) {
						System.out.println("> 출판사 수정이 완료되었습니다.");
					}
					pstmt.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
				
			case "y","Y":
				System.out.println("변경할 출판년도를 입력하십시오 : ");
				String year = sc.nextLine();
				
				DBConnection.setConnection();
				try {
					String sql = "UPDATE book SET issue_year=? WHERE title=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, year);
					pstmt.setString(2, name);
					
					int rowsInserted = pstmt.executeUpdate();
					if (rowsInserted > 0) {
						System.out.println("> 출판년도 수정이 완료되었습니다.");
					}
					pstmt.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
			
			case "i","I":
				System.out.println("변경할 isbn코드를 입력하십시오 : ");
				String isbn = sc.nextLine();
				
				DBConnection.setConnection();
				try {
					String sql = "UPDATE book SET isbn=? WHERE title=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, isbn);
					pstmt.setString(2, name);
					
					int rowsInserted = pstmt.executeUpdate();
					if (rowsInserted > 0) {
						System.out.println("> isbn코드 수정이 완료되었습니다.");
					}
					pstmt.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
				
			case "q","Q":
				System.out.println("변경할 도서 수량을 입력하십시오 : ");
				String qty = sc.nextLine();
				// 유효성 검사
				if (qty.equalsIgnoreCase("X")) return;
				if (qty.equals("")) {
					System.out.println("> 잘못된 입력입니다.\n");
					continue;
				}
				if (!Main.isNumber(qty)) {
					break;
				}
				int total_qty = Integer.parseInt(qty);
				
				DBConnection.setConnection();
				try {
					String sql = "UPDATE book SET total_qty=? WHERE title=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setInt(1, total_qty);
					pstmt.setString(2, name);
					
					int rowsInserted = pstmt.executeUpdate();
					if (rowsInserted > 0) {
						System.out.println("> 도서수량 수정이 완료되었습니다.");
					}
					pstmt.close();
				
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
			
			default:
				System.out.println("> 잘못된 입력입니다.\n");
				break;
			}
		}
	}
	
	private void readAll() {
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
		while (true) {
			System.out.print(">> 검색할 항목 선택 [t:제목, a:저자, p:출판사, x:종료] : ");
			String choice = sc.nextLine();
			if (choice.equals("x") || choice.equals("X")) {
				break;
			}
			if (choice.equals("")) {
				System.out.println("> 잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			switch (choice) {
			case "t","T":
				System.out.print(">> 도서의 제목을 입력하십시오 : ");
				String title = sc.nextLine();
				
				// 제목으로 책 정보 가져오는 sql작성
				DBConnection.setConnection();
				try {
					String sql = "SELECT book_id,title,author,publisher,issue_year,total_qty,qty FROM book WHERE title=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, title);
					
					ResultSet rs = pstmt.executeQuery();
					
					while (rs.next()) {
						System.out.println(rs.getInt("book_id")+". "+rs.getString("title")+"\t"+rs.getString("author")+"\t"+rs.getString("publisher")
						+"\t"+rs.getString("issue_year")+"\t"+rs.getString("total_qty")+"\t"+rs.getString("qty"));
					}
					System.out.println("");
					
					rs.close();
					pstmt.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
				
			case "a","A":
				System.out.print(">> 도서의 저자를 입력하십시오 : ");
				String author = sc.nextLine();
			
				// 저자로 책 정보 가져오는 sql작성
				DBConnection.setConnection();
				try {
					String sql = "SELECT book_id,title,author,publisher,issue_year,total_qty,qty FROM book WHERE author=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, author);
					ResultSet rs = pstmt.executeQuery();
					
					while (rs.next()) {
						System.out.println(rs.getInt("book_id")+". "+rs.getString("title")+"\t"+rs.getString("author")+"\t"+rs.getString("publisher")
						+"\t"+rs.getString("issue_year")+"\t"+rs.getString("total_qty")+"\t"+rs.getString("qty"));
					}
					System.out.println("");
					
					rs.close();
					pstmt.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;
				
			case "p","P":
				System.out.print(">> 도서의 출판사를 입력하십시오 : ");
				String publisher = sc.nextLine();
			
				// 출판사로 책 정보 가져오는 sql작성
				DBConnection.setConnection();
				try {
					String sql = "SELECT book_id,title,author,publisher,issue_year,total_qty,qty FROM book WHERE publisher=?";
					PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
					pstmt.setString(1, publisher);
					ResultSet rs = pstmt.executeQuery();
					
					while (rs.next()) {
						System.out.println(rs.getInt("book_id")+". "+rs.getString("title")+"\t"+rs.getString("author")+"\t"+rs.getString("publisher")
						+"\t"+rs.getString("issue_year")+"\t"+rs.getString("total_qty")+"\t"+rs.getString("qty"));
					}
					System.out.println("");
					
					rs.close();
					pstmt.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				DBConnection.disConnection();
				break;

			default:
				System.out.println("> 잘못된 입력입니다.\n");
				break;
			}
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
				}
				pstmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			DBConnection.disConnection();
		}
	}
	
	
	
	
	
}
