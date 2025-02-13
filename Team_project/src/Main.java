import java.util.Scanner;

public class Main {
	
	static Scanner s = new Scanner(System.in);
	

	public static void main(String[] args) {
		Book book= new Book();
		Member member = new Member();
		Rent rent = new Rent();
		
		while(true) {
			System.out.println("작업을 선택해 주세요. [B: 도서 관리, M: 회원 관리, R: 대여 관리, X: 프로그램 종료]");
			String admin = s.nextLine();
			
			if (admin.equals("x") || admin.equals("X")) break;
			switch(admin) {
			case "b" , "B" :
				//book.control();
				break;
			case "m" , "M" :
				//member.control();
				break;
			case "r" , "R" :
				//rent.control();
			}
		}
	}

}
