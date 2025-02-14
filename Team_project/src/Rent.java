import java.util.Scanner;

public class Rent {

	private Member member;
	private Book book;
	
	Rent() {
	}

	Rent(Member m,Book b) {
		this.member = m;
		this.book = b;
	}
	
	Scanner s = new Scanner(System.in);
	Scanner i = new Scanner(System.in);
	
	public void control () {
		while(true) {
			System.out.println("작업 선택 [RC: 대여 등록, RR: 대여 현황, RU: 대여 연장, RX: 대여 관리 종료");
			String adm = s.nextLine();
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
				this.read(); break;
			case "ru" , "RU" :
				System.out.println("대여 연장");
				this.extend(); break;
			}
		}
	}
	public void add() {
		
		while(true) {
			System.out.println("회원 ID를 입력해 주세요.");
			String memId = s.nextLine();
			if (memId.equals("")) break;
			
			System.out.println("도서 ID를 입력해 주세요.");
			int bookId = i.nextInt();
			if (bookId<0) break;
			
			
		}
	}
	
	public void read() {
		
	}
	
	public void extend() {
		
	}
}
