import java.io.*;
import java.util.*;

public class Member {
    private String memberId;
    private String name;
    private List<String> borrowedBooks;  // รายการหนังสือที่ถูกยืม

    // 🔹 Constructor ใช้สร้างอ็อบเจ็กต์สมาชิกใหม่
    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        this.borrowedBooks = new ArrayList<>();
    }

    // 🔹 Getter และ Setter
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }
    
    // 🔹 ยืมหนังสือ (เพิ่มหนังสือในรายการยืม)
    public void borrowBook(String bookId) { borrowedBooks.add(bookId); }

    // 🔹 คืนหนังสือ (ลบหนังสือออกจากรายการยืม)
    public void returnBook(String bookId) { borrowedBooks.remove(bookId); }

    // 🔹 แปลงข้อมูลสมาชิกเป็นข้อความเพื่อบันทึกลงไฟล์
    public String toString() {
        return memberId + "," + name + "," + String.join(";", borrowedBooks);
    }

    // 🔹 โหลดข้อมูลสมาชิกจากไฟล์
    public static List<Member> loadMembers() throws IOException {
        List<Member> members = new ArrayList<>();
        File file = new File("members.txt");
        if (!file.exists()) return members;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            Member member = new Member(data[0], data[1]);
            if (data.length > 2 && !data[2].isEmpty()) {
                member.borrowedBooks = Arrays.asList(data[2].split(";"));
            }
            members.add(member);
        }
        reader.close();
        return members;
    }

    // 🔹 บันทึกข้อมูลสมาชิกลงไฟล์
    public static void saveMembers(List<Member> members) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("members.txt"));
        for (Member member : members) {
            writer.write(member.toString());
            writer.newLine();
        }
        writer.close();
    }

    // 🔹 สร้างรหัสนิสิตอัตโนมัติ
    public static String generateMemberId(List<Member> members) {
        int newId = members.size() + 1;
        return String.format("%04d", newId);
    }
}
