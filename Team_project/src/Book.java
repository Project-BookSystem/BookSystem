import java.util.Scanner;

public class Book {
	
	Scanner ss = new Scanner(System.in);
	Scanner si = new Scanner(System.in);
	
	public void control() {
		System.out.println("[도서 관리]");
		while (true) {
			System.out.print("작업선택 [c:도서등록, u:도서정보수정, r:도서목록조회, d:도서삭제, x:종료] : ");
			String choice = ss.nextLine();
			
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
				this.read();
				break;
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
			System.out.print("추가하실 도서의 제목을 입력해주십시오 : ");
			String name = ss.nextLine();
			if (name.equals("")) {
				break;
			}
			System.out.print("추가하실 도서의 저자를 입력해주십시오 : ");
			String author = ss.nextLine();
			if (author.equals("")) {
				continue;
			}
			System.out.print("추가하실 도서의 출판사를 입력해주십시오 : ");
			String publisher = ss.nextLine();
			if (publisher.equals("")) {
				break;
			}
			System.out.print("추가하실 도서의 isbn코드를 입력해주십시오 : ");
			String isbn = ss.nextLine();
			if (isbn.equals("")) {
				break;
			}
			System.out.print("추가하실 도서의 보유수량을 입력해주십시오 : ");
			int qty = si.nextInt();
			if (qty < 1) {
				break;
			}
			
			// sql connection
			
		}
		
	}
	
	private void updated() {
		while (true) {
			System.out.print("수정할 도서의 id를 입력해십시오 : ");
			int bookID = si.nextInt();
			if (bookID < 1) {
				break;
			}
			// bookID에 해당하는 컬럼 불러와서 보여주기
			
			System.out.print("수정할 항목을 입력해주십시오 [t:제목, a:저자, i:isbn코드, p:출판사, y:출간년도, q:수량 x:종료] : ");
			String choice = ss.nextLine();
			
			if (choice.equals("x") || choice.equals("X")) {
				break;
			}
			switch (choice) {
			case "t","T":
				// 새 값 할당받는 코드
				break;
			case "a","A":
				
				break;
			case "i","I":
				
				break;
			case "p","P":
				
				break;
			case "y","Y":
				
				break;
			case "q","Q":
				
				break;
			default:
				System.out.println("> 잘못된 입력입니다.\n");
				break;
			}
			
		}
	}
	
	private void read() {
		
	}
	
	private void delete() {
		
	}
	
	
	
	
	
}
