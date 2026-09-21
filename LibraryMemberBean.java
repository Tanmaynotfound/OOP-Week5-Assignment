public class LibraryMemberBean {

    private String membershipId;
    private String name;
    private boolean premiumMember;
    private String securityAnswer;

    // No-arg constructor
    public LibraryMemberBean() {
        this(null, null);
    }

    // Name-only constructor
    public LibraryMemberBean(String name) {
        this(null, name);
    }

    // Main constructor
    public LibraryMemberBean(String membershipId, String name) {
        this.membershipId = membershipId;
        this.name = name;
        this.premiumMember = false;
    }

    // JavaBean getter
    public String getMembershipId() {
        return membershipId;
    }

    // Write-once setter
    public void setMembershipId(String id) {

        if (this.membershipId == null) {
            this.membershipId = id;
        }
    }

    // JavaBean getter for name
    public String getName() {
        return name;
    }

    // JavaBean setter for name
    public void setName(String name) {
        this.name = name;
    }

    // JavaBean boolean getter
    public boolean isPremiumMember() {
        return premiumMember;
    }

    // JavaBean setter
    public void setPremiumMember(boolean premium) {
        this.premiumMember = premium;
    }

    // Write-only security answer
    public void setSecurityAnswer(String answer) {

        if (answer == null) {
            this.securityAnswer = null;
        } else {
            // Deterministic one-way transformation
            this.securityAnswer = Integer.toHexString(answer.hashCode());
        }
    }

    public static void main(String[] args) {

        LibraryMemberBean m1 =
                new LibraryMemberBean("Priya Nair");

        System.out.println(m1.getMembershipId());

        LibraryMemberBean m2 =
                new LibraryMemberBean("LIB-8841", "Priya Nair");

        System.out.println(m2.getMembershipId());

        LibraryMemberBean m3 =
                new LibraryMemberBean();

        m3.setMembershipId("LIB-8841");
        m3.setMembershipId("FAKE-0000");

        System.out.println(m3.getMembershipId());

        m3.setPremiumMember(true);

        System.out.println(m3.isPremiumMember());

        m3.setSecurityAnswer("blue");

        System.out.println("Security answer set");
    }
}