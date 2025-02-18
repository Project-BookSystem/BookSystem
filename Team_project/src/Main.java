import java.io.IOException;
import java.util.Scanner;

public class Main {
	
	static Scanner s = new Scanner(System.in);
	
	public static void main(String[] args) {
		Book book= new Book();
		Member member = new Member();
		Rent rent = new Rent();
		
		while(true) {
			System.out.println("[시스템 관리]");
			System.out.print("> 작업을 선택해주십시요. [b:도서관리, m:회원관리, r:대여관리, x:프로그램 종료] : ");
			String admin = s.nextLine();
			
			if (admin.equals("x") || admin.equals("X")) break;
			
			switch(admin) {
			case "b" , "B" :
				book.control();
				break;
			case "m" , "M" :
				member.control();
				break;
			case "r" , "R" :
				rent.control();
			default:
				System.out.println("잘못된 입력입니다.\n");
			}
		}
		System.out.println("\n> 프로그램을 종료합니다.");
	}
	
	public static boolean isNumber(String str) {
		// 정규식을 사용하여 숫자와 소수점만 있는지 확인
		return str.matches("-?\\d*(\\.\\d+)?"); // 정규표현식
	}
	
	public static void clear() {
		try {
			new ProcessBuilder("cmd","/c","cls").inheritIO().start().waitFor();
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
	}

}
