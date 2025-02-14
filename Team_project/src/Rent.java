import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Rent {

	public static boolean isNumber(String str) {
		return str.matches("-?\\d*(\\_\\d+)?");
	}

	private Member member;
	private Book book;

	Rent() {
	}

	Rent(Member m,Book b) {
		this.member = m;
		this.book = b;
	}

	Scanner ss = new Scanner(System.in);
	Scanner si = new Scanner(System.in);

	public void control () {
		while(true) {
			System.out.println("작업 선택 [RC: 대여 등록, RR: 대여 현황, RU: 대여 연장, RE: 도서 반납, RX: 대여 관리 종료");
			String adm = ss.nextLine();
			if (adm.equals("rx") || adm.equals("RX")) {
				System.out.println("대여 관리 종료");
				break;
			}
			switch (adm) {
			case "rc" , "RC" :
				System.out.println("대여 등록");
			this.add(); break;
			case "rr" , "RR" :
				System.out.println("대여 현황");
				System.out.println("회원 ID를 입력해 주세요.");
				String memId = ss.nextLine();
				if(memId.equals("admin")) {
						this.readAll();
					} else {
						this.read(memId); break;
					}
			case "ru" , "RU" :
				System.out.println("대여 연장");
				System.out.println("회원 ID를 입력해 주세요.");
				memId = ss.nextLine();
				this.extend(memId); break;
			case "re" , "RE" :
				System.out.println("도서 반납");
				System.out.println("회원 ID를 입력해 주세요.");
				memId = ss.nextLine();
				this.returned(memId); break;
			}
		}
	}
	public void add() { 																												//insert into qty 수량 추가하기

		System.out.println("회원 ID를 입력해 주세요.");
		String memId = ss.nextLine();
		if (memId.equals("")) {
			System.out.println("잘못된 입력입니다. 다시 입력해 주세요.");
			memId = ss.nextLine();
			if (memId.equals("")) return; }

		System.out.println("도서 번호를 입력해 주세요."); 																						//like %%로 title 검색해서 목록 보여주기
		int bookId = si.nextInt();
		if (bookId<0) return;

		while(true) {
			//SQL 데이터 저장 > insert into 회원 ID, 도서 ID, 대여일, 반납일  
			DBConnection.setConnection();
			String sql = "insert into rent(member_id,book_id) values(?, ?)";
			try (PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql)) {
				pstmt.setString(1, memId);
				pstmt.setInt(2, bookId);

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
			String sql = "select r.member_id,r.title,r.rent,r.expected,IF(r.extension = 1, 'O', 'X') AS extension,"
					+ "r.extension_count,IF(r.overdue = 1, 'O', 'X') AS overdue from rent r,book b "
					+ "where r.member_id="+memId+"and r.book_id=b.book id";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next())
			{
				System.out.println("회원 ID : "+rs.getString("member_id")+" 책 제목: "+rs.getString("title")+
						"대여일 : "+rs.getInt("rent")+"반납 예정일 :"+rs.getInt("expected")+"연장 여부: "+rs.getString("extension")+
						"연장 횟수: "+rs.getInt("extension_count")+"연체 정보: "+rs.getString("overdue"));
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
				System.out.println("회원 ID : "+rs.getString("member_id")+" 도서 ID: "+rs.getString("book_id")+
						"대여일 : "+rs.getInt("rent")+"반납 예정일 :"+rs.getInt("expected")+"반납일 : "+rs.getInt("returned")+
						"연장 여부: "+rs.getString("extension")+"연장 횟수: "+rs.getInt("extension_count")+"연체 정보: "+rs.getString("overdue"));
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
			String sql = "select r.rent_id,r.member_id,r.title,r.rent,r.expected,IF(r.extension = 1, 'O', 'X') AS extension,"
					+ "r.extension_count,IF(r.overdue = 1, 'O', 'X') AS overdue from rent r,book b "
					+ "where r.member_id="+memId+"and r.book_id=b.book id and r.returned is null";
			ResultSet rs = st.executeQuery(sql);

			while (rs.next())
			{
				System.out.println("대여 번호: "+rs.getInt("rent_id")+"회원 ID : "+rs.getString("member_id")+" 책 제목: "+rs.getString("title")+
						"대여일: "+rs.getInt("rent")+"반납 예정일 :"+rs.getInt("expected")+"연장 여부: "+rs.getString("extension")+
						"연장 횟수: "+rs.getInt("extension_count")+"연체 정보: "+rs.getString("overdue"));
			}

			st.close(); rs.close();

			while(true) {
				System.out.println("연장하실 도서의 대여 번호를 입력해 주세요. 종료 희망 시:Enter"); // rent_id 입력 받고 연장(update)하는 sql문
				String sRentId = ss.nextLine();				
				if(sRentId.equals("")) break;
				if(!isNumber(sRentId)) break;
				int RentId = Integer.parseInt(sRentId);

				st = DBConnection.conn.createStatement();
				sql = "update rent set extension = true, extension_count = extension_count + 1, expected = date add(expected, INTERVAL 1 WEEK) where rent_id ="+RentId;
				st.executeUpdate(sql);
				String sql2 = "select expected from rent where rent_id ="+RentId;
				ResultSet rs2 = st.executeQuery(sql2);
				rs2.next();
				System.out.println("연장이 완료되었습니다. 반납 기한:"+rs2.getInt("expected"));
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
			String sql = "select r.rent_id,r.member_id,r.title,r.rent,r.expected,IF(r.extension = 1, 'O', 'X') AS extension,"
					+ "r.extension_count,IF(r.overdue = 1, 'O', 'X') AS overdue from rent r,book b "
					+ "where r.member_id="+memId+"and r.book_id=b.book id and r.returned is null";
			ResultSet rs = st.executeQuery(sql);
			
			while (rs.next())
			{
				System.out.println("대여 번호: "+rs.getInt("rent_id")+"회원 ID : "+rs.getString("member_id")+" 책 제목: "+rs.getString("title")+
						"대여일: "+rs.getInt("rent")+"반납 예정일 :"+rs.getInt("expected")+"연장 여부: "+rs.getString("extension")+
						"연장 횟수: "+rs.getInt("extension_count")+"연체 정보: "+rs.getString("overdue"));
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
			sql = "insert into rent (returned) values (current_timestamp) where rent_id ="+RentId;
			st.executeUpdate(sql);
			String sql2 = "select expected from rent where rent_id ="+RentId;
//			String sql3 = "update book set qty = qty - 1 where book_id ="+																	//where rent id~book id=title? 
			System.out.println("반납이 완료되었습니다.");
			st.close();
		}

		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		DBConnection.disConnection();
	}
}
