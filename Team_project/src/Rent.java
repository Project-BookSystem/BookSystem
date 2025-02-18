import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class Rent {

	public static boolean isNumber(String str) {
		return str.matches("-?\\d*(\\_\\d+)?");
	}

	Scanner ss = new Scanner(System.in);
	Scanner si = new Scanner(System.in);

	public void control () {
		while(true) {
			System.out.println("[대여 관리]");
			System.out.print("> 작업 선택 [c:대여등록, r:대여현황, u:대여연장, d:도서 반납, x:대여관리종료] : ");
			String adm = ss.nextLine();
			if (adm.equals("x") || adm.equals("X")) {
				System.out.println("> 대여관리종료");
				break;
			}
			switch (adm) {
			case "c" , "C" :
				System.out.println("> 대여등록");
				System.out.println("회원 ID를 입력해 주세요.");
				String memId = ss.nextLine();
				this.add(memId); break;
			case "r" , "R" :
				System.out.println("> 대여현황");
				System.out.println("회원 ID를 입력해 주세요.");
				memId = ss.nextLine();
				if(memId.equals("admin")) {
						this.readAll(); break;
					} else {
						this.read(memId); break;
					}
			case "u" , "U" :
				System.out.println("> 대여연장");
				System.out.println("회원 ID를 입력해 주세요.");
				memId = ss.nextLine();
				this.extend(memId); break;
			case "d" , "D" :
				System.out.println("> 도서반납");
				System.out.println("회원 ID를 입력해 주세요.");
				memId = ss.nextLine();
				this.returned(memId); break;
			}
		}
	}
	public void add(String memId) { 																											
	
		while(true) { //SQL 데이터 저장 > insert into 회원 ID, 도서 ID, 대여일, 반납일
			
			System.out.println("도서의 번호를 입력해 주세요. 종료 희망 시:Enter"); 	
			String bookId = ss.nextLine();				
			if(bookId.equals("")) break;
			if(!isNumber(bookId)) break;
			int BookId = Integer.parseInt(bookId);//like %%로 title 검색해서 목록 보여주기

			DBConnection.setConnection();
			String sql = "insert into rent(member_id,book_id) values(?, ?)";
			try (PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql)) {
				pstmt.setString(1, memId);
				pstmt.setString(2, bookId);
				
				Statement st = DBConnection.conn.createStatement();
				String sql2 = "update book set qty = qty + 1 where book_id ="+bookId; 						//where 구문 고민 필요
				st.executeUpdate(sql2);																		//출력 안 할 것이라 2줄만 썼는데 괜찮은 것인 지 확인 필
				
				int rowsInserted = pstmt.executeUpdate();
				if (rowsInserted > 0) {
					System.out.println("대여가 완료되었습니다.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			DBConnection.disConnection();		
			this.read(memId);	
		}
	}

	public void read(String memId) { //사용자의 대출목록 조회용
		// SQL 데이터 읽어오기 > select 멤버아이디, 책 이름>from book, 대출일, 반납예정일, 연장여부, 연장횟수, 연체여부 반복문으로

		DBConnection.setConnection();
		try {
			Statement st = DBConnection.conn.createStatement();
			String sql = "select r.member_id,b.title,r.rent,r.expected,IF(r.extension = 1, 'O', 'X') extension,"
					+ "r.extension_count,IF(r.overdue = 1, 'O', 'X') overdue from rent r, book b "
					+ "where r.member_id ='"+memId+"' and r.book_id = b.book_id and r.returned is null";
			
			ResultSet rs = st.executeQuery(sql);
			
			if (rs.next()) {
				do { 
					System.out.println("회원 ID: "+rs.getString("member_id")+", 책 제목: "+rs.getString("title")+
						", 대여일: "+rs.getString("rent")+", 반납 예정일: "+rs.getString("expected")+", 연장 여부: "+rs.getString("extension")+
						", 연장 횟수: "+rs.getInt("extension_count")+", 연체 정보: "+rs.getString("overdue"));
				} while (rs.next());
			} else {
			System.out.println("대여한 내역이 없습니다.");
			}
			
			rs.close(); st.close();

		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		DBConnection.disConnection();	
	}

	public void readAll () { //관리자의 대출목록 조회용
		// SQL 데이터 읽어오기 > select * from rent

		DBConnection.setConnection();
		try {
			Statement st = DBConnection.conn.createStatement();
			String sql = "select * from rent";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next())
			{
				System.out.println("대여 번호: "+rs.getInt("rent_id")+", 회원 ID: "+rs.getString("member_id")+", 도서 ID: "+rs.getString("book_id")+
						", 대여일: "+rs.getString("rent")+", 반납 예정일:"+rs.getString("expected")+", 반납일: "+rs.getString("returned")+
						", 연장 여부: "+rs.getString("extension")+", 연장 횟수: "+rs.getInt("extension_count")+", 연체 정보: "+rs.getString("overdue"));
			}
			rs.close(); st.close();

		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		DBConnection.disConnection();	

	}
	public void extend(String memId) {
		// SQL 데이터 읽어오기 > select 멤버아이디, 책 이름>from book, 대출일, 반납예정일, 연장여부, 연장횟수, 연체여부 반복문 + 반납일(returned) is null인 값만 show

		DBConnection.setConnection();
		try {
			Statement st = DBConnection.conn.createStatement();
			String sql = "select r.rent_id,r.member_id,b.title,r.rent,r.expected,IF(r.extension = 1, 'O', 'X') extension,"
					+ "r.extension_count,IF(r.overdue = 1, 'O', 'X') overdue from rent r,book b "
					+ "where r.member_id='"+memId+"'and r.book_id=b.book_id and r.returned is null";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next())
			{
				System.out.println("대여 번호: "+rs.getInt("rent_id")+", 회원 ID: "+rs.getString("member_id")+", 책 제목: "+rs.getString("title")+
						", 대여일: "+rs.getString("rent")+", 반납 예정일: "+rs.getString("expected")+", 연장 여부: "+rs.getString("extension")+
						", 연장 횟수: "+rs.getInt("extension_count")+", 연체 정보: "+rs.getString("overdue"));
			}

			st.close(); rs.close();

			while(true) {
				System.out.println("연장하실 도서의 대여 번호를 입력해 주세요. 종료 희망 시:Enter"); // rent_id 입력 받고 연장(update)하는 sql문
				String sRentId = ss.nextLine();				
				if(sRentId.equals("")) break;
				if(!isNumber(sRentId)) break;
				int RentId = Integer.parseInt(sRentId);

				st = DBConnection.conn.createStatement();
				sql = "update rent set extension = true, extension_count = extension_count + 1, expected = date_add(expected, INTERVAL 1 WEEK) where rent_id ="+RentId;
				st.executeUpdate(sql);
				String sql2 = "select expected from rent where rent_id ="+RentId;
				ResultSet rs2 = st.executeQuery(sql2);
				
				while(rs2.next()) {
				System.out.println("연장이 완료되었습니다. 반납 기한: "+rs2.getString("expected"));
				}
				
				st.close(); rs2.close();
			}

		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		DBConnection.disConnection();

	}
	public void returned(String memId) {
		// SQL 데이터 읽어오기 > select 멤버아이디, 책 이름>from book, 대출일, 반납예정일, 연장여부, 연장횟수, 연체여부를 반납일(returned) is null인 값만 show
		
		DBConnection.setConnection();
		try {
			Statement st = DBConnection.conn.createStatement();
			String sql = "select r.rent_id,r.member_id,b.book_id,b.title,r.rent,r.expected,IF(r.extension = 1, 'O', 'X') extension,"
					+ "r.extension_count,IF(r.overdue = 1, 'O', 'X') overdue from rent r,book b "
					+ "where r.member_id='"+memId+"'and r.book_id=b.book_id and r.returned is null"; 
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next())
			{
				System.out.println("대여 번호: "+rs.getInt("rent_id")+", 회원 ID: "+rs.getString("member_id")+", 도서 번호: "+rs.getInt("book_id")+", 도서 제목: "+rs.getString("title")+
						", 대여일: "+rs.getString("rent")+", 반납 예정일: "+rs.getString("expected")+", 연장 여부: "+rs.getString("extension")+
						", 연장 횟수: "+rs.getInt("extension_count")+", 연체 정보: "+rs.getString("overdue"));
			}

			st.close(); rs.close();

		while(true) {
			// SQL rent> returned insert, overdue check(?)>penalty, Book qty update(외부로 반출되는 수량)
			System.out.println("반납하실 도서의 대여 번호를 입력해 주세요. 종료 희망 시:Enter"); // rent_id 입력 받고 연장(update)하는 sql문
			String sRentId = ss.nextLine();				
			if(sRentId.equals("")) break;
			if(!isNumber(sRentId)) break;
			int RentId = Integer.parseInt(sRentId);
			
			st = DBConnection.conn.createStatement();
			sql = "update rent set returned = current_timestamp where rent_id ="+RentId;
			st.executeUpdate(sql);
//			String sql2 = "select expected from rent where rent_id ="+RentId;
//			st.executeUpdate(sql2);
			String sql3 = "update book b, rent r on r.book_id = b.book_id set qty=qty-1 ";	//where r.book_id =b.book_id\"where rent id~book id=title? > title만 가지고는 어려울 것 같아서 book id 추가 했으나, 이 where로 적용 될 지 의문
			st.executeUpdate(sql3);
			
			System.out.println("반납이 완료되었습니다.");
			st.close();
		}

		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		DBConnection.disConnection();
	}
}
