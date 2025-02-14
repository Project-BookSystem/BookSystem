import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Book {
	//ㅁ
	Scanner sc = new Scanner(System.in);
	
	public void control() {
		System.out.println("[도서 관리]");
		while (true) {
			System.out.print("작업선택 [c:도서등록, u:도서정보수정, r:도서목록, d:도서삭제, x:종료] : ");
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
//				this.updated();
				break;
			case "r","R":
				System.out.println("> 도서 목록조회");
				System.out.print("작업 선택 [a:전체도서목록, s:도서검색] : ");
				String cho = sc.nextLine();
				if (cho.equals("a")) {
					this.readAll();
				} if (cho.equals("s")) {
//					this.read();
				} else {
					System.out.println("> 잘못된 입력입니다.");
					continue;
//					break;
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
			System.out.print("추가하실 도서의 제목을 입력해주십시오 [x:종료] : ");
			String name = sc.nextLine();
			if (name.equalsIgnoreCase("X")) return;
			if (name.equals("")) {
				System.out.println("잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print("추가하실 도서의 저자를 입력해주십시오 [x:종료] : ");
			String author = sc.nextLine();
			if (author.equalsIgnoreCase("X")) return;
			if (author.equals("")) {
				System.out.println("잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print("추가하실 도서의 출판사를 입력해주십시오 [x:종료] : ");
			String publisher = sc.nextLine();
			if (publisher.equalsIgnoreCase("X")) return;
			if (publisher.equals("")) {
				System.out.println("잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print("추가하실 도서의 출간년도를 입력해주십시오 [x:종료] : ");
			String year = sc.nextLine();
			if (year.equalsIgnoreCase("X")) return;
			if (year.equals("")) {
				System.out.println("잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print("추가하실 도서의 isbn코드를 입력해주십시오 [x:종료] : ");
			String isbn = sc.nextLine();
			if (isbn.equalsIgnoreCase("X")) return;
			if (isbn.equals("")) {
				System.out.println("잘못된 입력입니다. 다시 입력해주십시오.\n");
				continue;
			}
			System.out.print("추가하실 도서의 보유수량을 입력해주십시오 [x:종료] : ");
			String qty = sc.nextLine();
			if (qty.equalsIgnoreCase("X")) return;
			if (qty.equals("")) {
				System.out.println(">> 잘못된 입력입니다.\n");
				continue;
			}
			if (!Main.isNumber(qty)) {
				break;
			}
			int total_qty = Integer.parseInt(qty);
			
			if (total_qty < 1) {
				System.out.println("잘못된 입력입니다. 다시 입력해주십시오.\n");
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
					System.out.println("도서 등록이 완료되었습니다.");
				}
				pstmt.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			DBConnection.disConnnection();
		}
		
	}
	
//	private void updated() {
//		while (true) {
//			System.out.print("수정할 도서의 id를 입력해십시오 : ");
//			int bookID = si.nextInt();
//			if (bookID < 1) {
//				break;
//			}
//			// bookID에 해당하는 컬럼 불러와서 보여주기
//			
//			System.out.print("수정할 항목을 입력해주십시오 [t:제목, a:저자, i:isbn코드, p:출판사, y:출간년도, q:수량 x:종료] : ");
//			String choice = ss.nextLine();
//			
//			if (choice.equals("x") || choice.equals("X")) {
//				break;
//			}
//			switch (choice) {
//			case "t","T":
//				// 새 값 할당받는 코드
//				break;
//			case "a","A":
//				
//				break;
//			case "i","I":
//				
//				break;
//			case "p","P":
//				
//				break;
//			case "y","Y":
//				
//				break;
//			case "q","Q":
//				
//				break;
//			default:
//				System.out.println("> 잘못된 입력입니다.\n");
//				break;
//			}
//			
//		}
//	}
	
	private void readAll() {
		DBConnection.setConnection();
		try {
			Statement st = DBConnection.conn.createStatement();
			String sql = "SELECT book_id,title,author,isbn,publisher,issue_year,total_qty,qty FROM book";

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
		DBConnection.disConnnection();
	}
	
	private void read() {
		
	}
	
	private void delete() {
		while (true) {
			System.out.print("삭제할 도서의 제목을 입력하십시오 : ");
			String title = sc.nextLine();
			if (title.isEmpty()) {
				System.out.println("> 잘못된 입력입니다. 다시 입력하십시오.\n");
				continue;
			}

			DBConnection.setConnection();
			try {
				String sql = "DELETE FROM book WHERE title=?";
				PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql);
				pstmt.setString(1, title);

				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("도서 삭제가 완료되었습니다.");
				}
				pstmt.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
			DBConnection.disConnnection();
		}
	}
	
	
	
	
	
}
