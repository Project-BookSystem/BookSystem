

import java.sql.*;
import java.util.Scanner;

public class Member {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("(멤버관리) [ M: 멤버가입 , U: 멤버정보수정 , D: 멤버탈퇴 , X: 종료 ]");
            String choice = scanner.nextLine().toUpperCase();

            switch (choice) {
                case "M":
                    registerMember();
                    break;
                case "U":
                    updateMember();
                    break;
                case "D":
                    deleteMember();
                    break;
                case "X":
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }
    }

    private static void registerMember() {
        while (true) {
            System.out.println("멤버 가입을 진행합니다. (X 입력 시 취소)");
            System.out.print("이름: ");
            String name = scanner.nextLine();
            if (name.equalsIgnoreCase("X")) return;
            if (name.isEmpty() || name.length() > 32) {
                System.out.println("이름 입력 실패: 1~32자 사이로 입력해야 합니다.");
                continue;
            }

            System.out.print("아이디: ");
            String userId = scanner.nextLine();
            if (userId.equalsIgnoreCase("X")) return;
            if (userId.isEmpty() || userId.length() > 32) {
                System.out.println("아이디 입력 실패: 1~32자 사이로 입력해야 합니다.");
                continue;
            }

            System.out.print("비밀번호: ");
            String password = scanner.nextLine();
            if (password.equalsIgnoreCase("X")) return;
            if (password.isEmpty() || password.length() > 24) {
                System.out.println("비밀번호 입력 실패: 1~24자 사이로 입력해야 합니다.");
                continue;
            }

            System.out.print("생년월일 (YYYYMMDD): ");
            String birthday = scanner.nextLine();
            if (birthday.equalsIgnoreCase("X")) return;
            if (!birthday.matches("\\d{8}")) {
                System.out.println("생년월일 입력 실패: YYYYMMDD 형식의 8자리 숫자로 입력해야 합니다.");
                continue;
            }

            System.out.print("성별 (M/F): ");
            String gender = scanner.nextLine().toUpperCase();
            if (gender.equalsIgnoreCase("X")) return;
            if (!gender.isEmpty() && !gender.equals("M") && !gender.equals("F")) {
                System.out.println("성별 입력 실패: M 또는 F로 입력해야 합니다.");
                continue;
            }

            System.out.print("전화번호: ");
            String mobile = scanner.nextLine();
            if (mobile.equalsIgnoreCase("X")) return;
            if (mobile.isEmpty() || mobile.length() > 16) {
                System.out.println("전화번호 입력 실패: 1~16자 사이로 입력해야 합니다.");
                continue;
            }

            String sql = "INSERT INTO member (name, user_id, password, birthday, gender, mobile, overdue) VALUES (?, ?, ?, ?, ?, ?, 'X')";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, userId);
                pstmt.setString(3, password);
                pstmt.setString(4, birthday);
                pstmt.setString(5, gender);
                pstmt.setString(6, mobile);

                int rowsInserted = pstmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("회원 가입이 완료되었습니다.");
                }
            } catch (SQLException e) {
                System.out.println("데이터베이스 오류 발생: " + e.getMessage());
            }
            break;
        }
    }

    private static void updateMember() {
        System.out.print("수정할 멤버의 아이디 (X 입력 시 취소): ");
        String userId = scanner.nextLine();
        if (userId.equalsIgnoreCase("X")) return;

        System.out.println("수정할 항목을 선택하세요:");
        System.out.println("1. 비밀번호");
        System.out.println("2. 전화번호");
        System.out.println("3. 연체 여부 (O/X)");
        System.out.println("X. 취소");
        String choice = scanner.nextLine().toUpperCase();
        if (choice.equals("X")) return;

        String column = null;
        String newValue = null;

        switch (choice) {
            case "1":
                column = "password";
                System.out.print("새 비밀번호: ");
                newValue = scanner.nextLine();
                break;
            case "2":
                column = "mobile";
                System.out.print("새 전화번호: ");
                newValue = scanner.nextLine();
                break;
            case "3":
                column = "overdue";
                System.out.print("연체 여부 (O/X): ");
                newValue = scanner.nextLine();
                break;
            default:
                System.out.println("잘못된 선택입니다.");
                return;
        }

        String sql = "UPDATE member SET " + column + " = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, userId);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("멤버 정보가 수정되었습니다.");
            } else {
                System.out.println("해당 아이디의 멤버를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("데이터베이스 오류 발생: " + e.getMessage());
        }
    }
private static void deleteMember() {
    System.out.print("탈퇴할 멤버의 아이디 (X 입력 시 취소) :");
    String userId = scanner.nextLine();
    if (userId.equalsIgnoreCase("X")) return;
    String sql = "DELETE FROM member WHERE user_id = ?";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, userId);

        int rowsDeleted = pstmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("멤버 탈퇴가 완료되었습니다.");
        } else {
            System.out.println("해당 아이디의 멤버를 찾을 수 없습니다.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}

