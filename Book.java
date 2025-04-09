import java.io.*;
import java.util.*;

public class Book {
    // attributes
    private String bookId;  // รหัสหนังสือ
    private String title;   // ชื่อหนังสือ
    private boolean isAvailable;  // สถานะหนังสือ (true = พร้อมให้ยืม, false = ถูกยืม)

    // 🔹 Constructor ใช้สร้างอ็อบเจ็กต์หนังสือใหม่
    public Book(String bookId, String title) {
        this.bookId = bookId;
        this.title = title;
        this.isAvailable = true;  // ค่าเริ่มต้นคือสามารถยืมได้
    }

    // 🔹 Getter และ Setter
    public String getBookId() { return bookId; }
    public String getTitle() { return title; }
    public boolean isAvailable() { return isAvailable; }
    public void borrowBook() { isAvailable = false; }  // ยืมหนังสือ
    public void returnBook() { isAvailable = true; }   // คืนหนังสือ

    // 🔹 แปลงข้อมูลหนังสือเป็นข้อความเพื่อบันทึกลงไฟล์
    public String toString() {
        return bookId + "," + title + "," + isAvailable;
    }

    // 🔹 โหลดข้อมูลหนังสือจากไฟล์ books.txt
    public static List<Book> loadBooks() throws IOException {
        List<Book> books = new ArrayList<>();
        File file = new File("books.txt");
        if (!file.exists()) return books;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            Book book = new Book(data[0], data[1]);
            if (data[2].equals("false")) book.borrowBook();  // กำหนดสถานะตามไฟล์
            books.add(book);
        }
        reader.close();
        return books;
    }

    // 🔹 บันทึกข้อมูลหนังสือลงไฟล์
    public static void saveBooks(List<Book> books) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("books.txt"));
        for (Book book : books) {
            writer.write(book.toString());
            writer.newLine();
        }
        writer.close();
    }

    // 🔹 สร้างรหัสหนังสืออัตโนมัติ
    public static String generateBookId(List<Book> books) {
        int newId = books.size() + 1;
        return String.format("%04d", newId);
    }
}
