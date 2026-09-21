public class LoanReceipt {

    private static int totalReceiptsCreated;

    private final String memberId;
    private final String[] bookIds;

    // One-time shared state
    static {
        totalReceiptsCreated = 0;
    }

    public LoanReceipt(String memberId, String[] bookIds) {

        if (bookIds == null || bookIds.length > 20) {
            throw new IllegalArgumentException("Invalid bookIds");
        }

        for (String bookId : bookIds) {

            if (!isValidBookId(bookId)) {
                throw new IllegalArgumentException("Invalid book ID");
            }
        }

        this.memberId = memberId;

        // Defensive copy
        this.bookIds = bookIds.clone();

        totalReceiptsCreated++;
    }

    private static boolean isValidBookId(String bookId) {

        if (bookId == null || bookId.length() != 6) {
            return false;
        }

        if (!bookId.startsWith("BK-")) {
            return false;
        }

        for (int i = 3; i < 6; i++) {

            char ch = bookId.charAt(i);

            if (ch < '0' || ch > '9') {
                return false;
            }
        }

        return true;
    }

    public String[] getBookIds() {

        // Defensive copy on the way out
        return bookIds.clone();
    }

    public LoanReceipt withCorrectedBookId(int index, String newId) {

        if (index < 0 || index >= bookIds.length) {
            throw new IllegalArgumentException("Invalid index");
        }

        if (!isValidBookId(newId)) {
            throw new IllegalArgumentException("Invalid book ID");
        }

        String[] correctedIds = bookIds.clone();

        correctedIds[index] = newId;

        // Return a brand-new object
        return new LoanReceipt(memberId, correctedIds);
    }

    public static String processNightlyCirculation(LoanReceipt[] receipts) {

        int processed = 0;
        int nullSkipped = 0;
        int referenceOnly = 0;
        int regular = 0;

        if (receipts == null) {
            return "0 processed | 0 null skipped | 0 reference-only | 0 regular";
        }

        for (LoanReceipt receipt : receipts) {

            if (receipt == null) {
                nullSkipped++;
                continue;
            }

            processed++;

            if (receipt instanceof ReferenceOnlyLoanReceipt) {
                referenceOnly++;
            } else {
                regular++;
            }
        }

        return processed + " processed | "
                + nullSkipped + " null skipped | "
                + referenceOnly + " reference-only | "
                + regular + " regular";
    }

    public static void main(String[] args) {

        // Invalid construction
        try {
            new LoanReceipt(
                "LIB-8841",
                new String[]{"BK-100", "bad"}
            );

            System.out.println("construction succeeded");

        } catch (IllegalArgumentException e) {
            System.out.println("construction rejected");
        }

        // Defensive copying test
        LoanReceipt r = new LoanReceipt(
            "LIB-8841",
            new String[]{"BK-100", "BK-101"}
        );

        String[] ids = r.getBookIds();

        ids[0] = "HACKED";

        System.out.println(r.getBookIds()[0]);

        // Wither test
        LoanReceipt corrected =
                r.withCorrectedBookId(0, "BK-999");

        System.out.println(r.getBookIds()[0]);
        System.out.println(corrected.getBookIds()[0]);

        // Nightly processing test
        LoanReceipt[] receipts = {
            new ReferenceOnlyLoanReceipt(
                "LIB-001",
                new String[]{"BK-200"},
                "Reading Room 3"
            ),
            null,
            new LoanReceipt(
                "LIB-002",
                new String[]{"BK-201"}
            )
        };

        System.out.println(
            LoanReceipt.processNightlyCirculation(receipts)
        );
    }
}


// Reference-only variant
class ReferenceOnlyLoanReceipt extends LoanReceipt {

    private final String roomNumber;

    public ReferenceOnlyLoanReceipt(
            String memberId,
            String[] bookIds,
            String roomNumber) {

        super(memberId, bookIds);

        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}