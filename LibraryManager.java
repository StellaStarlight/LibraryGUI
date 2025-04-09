import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class LibraryManager {
    private List<Book> books;
    private List<Member> members;

    // 🔹 Constructor โหลดข้อมูลจากไฟล์เมื่อโปรแกรมเริ่มทำงาน
    public LibraryManager() throws IOException {
        this.books = Book.loadBooks();
        this.members = Member.loadMembers();
    }

    public List<Book> getBooks() {
        return books;
    }
    
    public List<Member> getMembers() {
        return members;
    }
    

    // 🔹 ลงทะเบียนหนังสือใหม่
    public void addBook(String title) throws IOException {
        String bookId = Book.generateBookId(books); // สร้างรหัสอัตโนมัติ
        books.add(new Book(bookId, title));
        Book.saveBooks(books); // บันทึกข้อมูลลงไฟล์
        System.out.println("✅ Book added successfully (ID: " + bookId + ")");
    }

    // 🔹 ลงทะเบียนสมาชิกใหม่
    public void addMember(String name) throws IOException {
        String memberId = Member.generateMemberId(members); // สร้างรหัสอัตโนมัติ
        members.add(new Member(memberId, name));
        Member.saveMembers(members); // บันทึกข้อมูลลงไฟล์
        System.out.println("✅ Member added successfully (ID: " + memberId + ")");
    }

    // 🔹 แสดงหนังสือทั้งหมด
    public void displayBooks() {
        System.out.println("\n📚 All Books:");
        for (Book b : books) {
            System.out.println(b.getBookId() + " - " + b.getTitle() + " [" + (b.isAvailable() ? "Available" : "Borrowed") + "]");
        }
    }

    // 🔹 แสดงสมาชิกทั้งหมด
    public void displayMembers() {
        System.out.println("\n👥 All Members:");
        for (Member m : members) {
            System.out.println(m.getMemberId() + " - " + m.getName() + " | Borrowed Books: " + m.getBorrowedBooks());
        }
    }

    // 🔹 ยืมหนังสือ
    public void borrowBook(String bookId, String memberId) throws IOException {
        Optional<Book> bookOpt = books.stream().filter(b -> b.getBookId().equals(bookId)).findFirst();
        Optional<Member> memberOpt = members.stream().filter(m -> m.getMemberId().equals(memberId)).findFirst();

        if (bookOpt.isEmpty()) {
            System.out.println("❌ Book ID not found.");
            return;
        }
        if (memberOpt.isEmpty()) {
            System.out.println("❌ Member ID not found.");
            return;
        }

        Book book = bookOpt.get();
        Member member = memberOpt.get();

        if (!book.isAvailable()) {
            System.out.println("❌ Book is already borrowed.");
            return;
        }

        book.borrowBook();
        member.borrowBook(bookId);
        Book.saveBooks(books);
        Member.saveMembers(members);
        System.out.println("✅ Book borrowed successfully.");
    }

    // 🔹 คืนหนังสือ
    public void returnBook(String bookId) throws IOException {
        Optional<Book> bookOpt = books.stream().filter(b -> b.getBookId().equals(bookId)).findFirst();

        if (bookOpt.isEmpty()) {
            System.out.println("❌ Book ID not found.");
            return;
        }

        Book book = bookOpt.get();
        if (book.isAvailable()) {
            System.out.println("❌ This book is not borrowed.");
            return;
        }

        book.returnBook();
        members.forEach(m -> m.returnBook(bookId));
        Book.saveBooks(books);
        Member.saveMembers(members);
        System.out.println("✅ Book returned successfully.");
    }
}
