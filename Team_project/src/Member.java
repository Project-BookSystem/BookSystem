import java.sql.*;
import java.util.Scanner;

public class Member {
	
	private static Scanner sc = new Scanner(System.in);

	public void control() {
		while (true) {
			System.out.println("[회원관리]");
			System.out.print("> 작업을 선책하십시오. [c:회원등록, u:회원정보수정, d:회원탈퇴, r:회원조회, x:종료] : ");
			String choice = sc.nextLine().toUpperCase();  // toUpperCase란 소문자를 입력하여도 실행가능하게 해주는 코드
			
			if (choice.equals("X")) {
				break;
			}
			
			switch (choice) {
			case "C":
				registerMember();
				break;
			case "U":
				updateMember();
				break;
			case "D":
				deleteMember();
				break;
			case "R":
				researchMember();
				break;
			default:
				System.out.println("잘못된 입력입니다. 다시 입력해주십시오.\n");
			}
		}
	}

	private void registerMember() {
		while (true) {
			System.out.println("> 회원등록 [x:취소]");
			System.out.print(">> 이름: ");
			String name = sc.nextLine();
			if (name.equalsIgnoreCase("X")) return;
			if (name.isEmpty() || name.length() > 32) {  //이름이 빈 문자열이거나 32자리를 넘는다면 아래를 실행
				System.out.println("> 이름 입력 실패: 1~32자 사이로 입력해야 합니다.");
				continue; // while(true)의 루프 처음으로 이동하게끔하는 코드
			}

			System.out.print(">> 아이디: ");
			String userId = sc.nextLine();
			if (userId.equalsIgnoreCase("X")) return;
			if (userId.isEmpty() || userId.length() > 32) {
				System.out.println("> 아이디 입력 실패: 1~32자 사이로 입력해야 합니다.");
				continue;
			}

			System.out.print(">> 비밀번호: ");
			String password = sc.nextLine();
			if (password.equalsIgnoreCase("X")) return;
			if (password.isEmpty() || password.length() > 24) {
				System.out.println("> 비밀번호 입력 실패: 1~24자 사이로 입력해야 합니다.");
				continue;
			}

			System.out.print(">> 생년월일(YYYYMMDD): ");
			String birthday = sc.nextLine();
			if (birthday.equalsIgnoreCase("X")) return;
			if (!birthday.matches("\\d{8}")) { // .matches() 뒤에조건이 일치하면 true 불일치하면 false로 출력하는 코드, \\d = 숫자0~9인지 확인 {x} = 앞의 내용을 x번 반복하는 식
				System.out.println("> 생년월일 입력 실패: YYYYMMDD 형식의 8자리 숫자로 입력해야 합니다.");
				continue;
			}

			System.out.print(">> 성별(M/F): ");
			String gender = sc.nextLine().toUpperCase();
			if (gender.equalsIgnoreCase("X")) return;
			if (!gender.isEmpty() && !gender.equals("M") && !gender.equals("F")) { //빈문자열 M F 로만 입력하고 실행됨
				System.out.println("> 성별 입력 실패: M 또는 F로 입력해야 합니다.");
				continue;
			}

			System.out.print(">> 전화번호: ");
			String mobile = sc.nextLine();
			if (mobile.equalsIgnoreCase("X")) return;
			if (mobile.isEmpty() || mobile.length() > 16) {
				System.out.println("> 전화번호 입력 실패: 1~16자 사이로 입력해야 합니다.");
				continue;
			}
			DBConnection.setConnection();
			String sql = "INSERT INTO member (name, user_id, password, birthday, gender, mobile, overdue) VALUES (?, ?, ?, ?, ?, ?, 'X')";
			try (PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql)) { // ? 사용하여 입력값을 설정할 수 있는 객체 배열에 얽매이지 않음.
				pstmt.setString(1, name); // 첫번째 ?에 입력받은 name값을 설정 입력
				pstmt.setString(2, userId);
				pstmt.setString(3, password);
				pstmt.setString(4, birthday);
				pstmt.setString(5, gender);
				pstmt.setString(6, mobile);

				int rowsInserted = pstmt.executeUpdate(); //executeUpdate()란 sql 작업후 영향을받은 행의 개수를 반환함
				if (rowsInserted == 1) { // 성공적으로 작업을 1개 완료했으면 가입완료 다중가입을 하기위해선 rowsInserted > 0 으로 코드수정 필요
					System.out.println("> 회원가입이 완료되었습니다.");
				}
				pstmt.close();
			} catch (SQLException e) { // SQLException sql 에 오류가 발생했는지 확인하는 구문
				System.out.println("데이터베이스 오류 발생: " + e.getMessage()); // e.getMessage() 오류 상세메시지 출력
			}
			DBConnection.disConnection();break;
		}
	}

	private void updateMember() { // 수정) 아이디 admin 입력 시 연체여부 관리할 수 있게 수정
		System.out.print("> 수정할 회원의 아이디를 입력하십시오. [x:취소] : ");
		String userId = sc.nextLine();
		if (userId.equalsIgnoreCase("X")) return;

		System.out.print("> 수정할 항목을 선택하십시오. [1.이름 2.비밀번호 3.전화번호 4.연체여부(o/x) x:취소] :");
		String choice = sc.nextLine().toUpperCase();
		if (choice.equals("X")) return;

		String column = null;
		String newValue = null;

		switch (choice) {
		case "1":
			column = "name";
			System.out.print(">> 새 이름 : ");
			newValue = sc.nextLine();
			break;
		case "2":
			column = "password";
			System.out.print(">> 새 비밀번호: ");
			newValue = sc.nextLine();
			break;
		case "3":
			column = "mobile";
			System.out.print(">> 새 전화번호: ");
			newValue = sc.nextLine();
			break;
		case "4":
			column = "overdue";
			System.out.print(">> 연체 여부 (O/X): ");
			newValue = sc.nextLine();
			break;
		default:
			System.out.println("> 잘못된 선택입니다.");
			return;
		}
		DBConnection.setConnection();
		String sql = "UPDATE member SET " + column + " = ? WHERE user_id = ?";
		try (PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql)) {
			pstmt.setString(1, newValue);
			pstmt.setString(2, userId);

			int rowsUpdated = pstmt.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("> 회원정보가 수정되었습니다.");
			} else {
				System.out.println("> 해당 아이디의 회원을 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			System.out.println("데이터베이스 오류 발생: " + e.getMessage());
		}
		DBConnection.disConnection();
	}
	
	private void deleteMember() { // 수정) 정말 탈퇴할 것인지 한번더 물어보기
		System.out.print("> 탈퇴할 회원의 아이디를 입력하십시오. [x:취소] :");
		String userId = sc.nextLine();
		if (userId.equalsIgnoreCase("X")) return;
		String sql = "DELETE FROM member WHERE user_id = ?";

		DBConnection.setConnection();
		try (PreparedStatement pstmt = DBConnection.conn.prepareStatement(sql)) {
			pstmt.setString(1, userId);

			int rowsDeleted = pstmt.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("> 회원탈퇴가 완료되었습니다.");
			} else {
				System.out.println("> 해당 아이디의 회원을 찾을 수 없습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace(); //e.printStackTrace() 는 System.out.println(e.getMessage()) 과 비슷함
			//e.printStackTrace()는 모든 오류내용들을 다 출력하지만 e.getMessage()는 해당 예외 메세지만 출력
		}
		DBConnection.disConnection();
	}
	
	private void researchMember() {
		String sql = "SELECT * FROM member";

		DBConnection.setConnection();
		try (Statement stmt = DBConnection.conn.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)) {
			//Statement stmt = conn.createStatement()를 통해 데이터베이스와 연결 SELECT, INSERT, UPDATE, DELETE 같은 SQL 명령을 실행할 수 있음
			//ResultSet 데이터베이스에서 가져온 데이터를 이클립스에서 읽을 수 있게해주는 코드 
			//stmt.executeQuery(sql)는 SQL SELECT 쿼리를 실행하고 결과 집합을 반환함.
			System.out.println("> 전체 회원보기");
			while (rs.next()) {
				//rs.next()는 ResultSet에서 다음 행(row)으로 이동함 다음 데이터가 존재하면 true, 없으면 false 그래서 데이터가 존재할때까지 출력가능케함
				System.out.println("ID: " + rs.getString("user_id")+
								   ", 이름: " + rs.getString("name")+
								   ", 전화번호: " + rs.getString("mobile")+
								   ", 연체 여부: " + rs.getString("overdue"));
			}
		} catch (SQLException e) {
			System.out.println("데이터베이스 오류 발생: " + e.getMessage());
		}
		DBConnection.disConnection();
	}
}
