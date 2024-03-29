import java.sql.*;
import java.util.*;
import java.sql.Date;

public class MarketingSystem {

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a number to select function");
        System.out.println("1. Add User");
        System.out.println("2. Verify User");
        System.out.println("3. Create New Company");
        System.out.println("4. Create New Social Media Account");
        System.out.println("5. Create Marketing Campaign");
        System.out.println("6. Create Ad");
        System.out.println("7. View Campaign Metrics");
        System.out.println("8. Get Employee by Employee Number");
        System.out.println("9. Add User to Company");
        System.out.println("10. List Users");
        System.out.println("11. List Companies");
        int input = scanner.nextInt();
        scanner.nextLine();
        switch (input) {
            case 1:
                System.out.println("Enter employee number:");
                String employeeNum = scanner.nextLine();
                System.out.println("Enter first name:");
                String firstName = scanner.nextLine();
                System.out.println("Enter last name:");
                String lastName = scanner.nextLine();
                System.out.println("Enter username:");
                String username = scanner.nextLine();
                System.out.println("Enter password:");
                String password = scanner.nextLine();
                addUser(employeeNum, firstName, lastName, username, password);
                break;
            case 2:
                System.out.println("Enter employee number:");
                String employeeNum2 = scanner.nextLine();
                System.out.println("Enter EIN:");
                String EIN = scanner.nextLine();
                verifyUser(employeeNum2, EIN);
                break;
            case 3:
                System.out.println("Enter company name:");
                String companyName = scanner.nextLine();
                System.out.println("Enter EIN:");
                String ein = scanner.nextLine();
                System.out.println("Enter email:");
                String email = scanner.nextLine();
                System.out.println("Enter phone:");
                String phone = scanner.nextLine();
                System.out.println("Enter employee number:");
                String empNum = scanner.nextLine();
                createCompany(companyName, ein, email, phone, empNum);
                break;
            case 4:
                System.out.println("Enter EIN:");
                EIN = scanner.nextLine();
                System.out.println("Enter platform name:");
                String platformName = scanner.nextLine();
                System.out.println("Enter account handle:");
                String accountHandle = scanner.nextLine();
                createSocialMediaAccount(EIN, platformName, accountHandle);
                break;
            case 5:
                System.out.println("Enter Social Media ID:");
                int socialMediaID = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Enter campaign name:");
                String campaignName = scanner.nextLine();
                System.out.println("Enter start date (YYYY-MM-DD):");
                String startDateString = scanner.nextLine();
                Date startDate = Date.valueOf(startDateString);
                System.out.println("Enter end date (YYYY-MM-DD):");
                String endDateString = scanner.nextLine();
                Date endDate = Date.valueOf(endDateString);
                System.out.println("Enter cost:");
                double cost = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                System.out.println("Enter strategy:");
                String strategy = scanner.nextLine();
                createMarketingCampaign(socialMediaID, campaignName, startDate, endDate, cost, strategy);
                break;
            case 6:
                System.out.println("Enter campaign ID:");
                int campaignId = scanner.nextInt();
                System.out.println("Enter start date (YYYY-MM-DD):");
                String adStartDateString = scanner.nextLine();
                Date adStartDate = Date.valueOf(adStartDateString);
                System.out.println("Enter end date (YYYY-MM-DD):");
                String adEndDateString = scanner.nextLine();
                Date adEndDate = Date.valueOf(adEndDateString);
                System.out.println("Enter type of ad:");
                String typeOfAd = scanner.nextLine();
                createAd(campaignId, adStartDate, adEndDate, typeOfAd);
                break;
            case 7:
                System.out.println("Enter campaign name:");
                String campaignNameForMetrics = scanner.nextLine();
                viewCampaignMetrics(campaignNameForMetrics);
                break;
            case 8:
                System.out.println("Enter employee number:");
                String employeeNumberForEmployee = scanner.nextLine();
                getEmployeeByEmployeeNumber(employeeNumberForEmployee);
                break;
            case 9:
                System.out.println("Enter EIN:");
                String einForUser = scanner.nextLine();
                System.out.println("Enter employee number:");
                String employeeNumForUser = scanner.nextLine();
                addUserToCompany(einForUser, employeeNumForUser);
                break;
            case 10:
                listUsers();
                break;
            case 11:
                listCompanies();
                break;
            default:
                System.out.println("Invalid input.");
        }
    }
    // Establish a connection to the database
    public static Connection connect() {
        try {
            String url = "jdbc:postgresql://localhost:5432/newmetamarket";
            String user = "postgres";
            String password = null;
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Add User
    public static void addUser(String employeeNum, String firstName, String lastName, String username, String password) {
        try (Connection conn = connect()) {
            assert conn != null;
            //Check for duplicate employee number
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Users WHERE EmployeeNum = ?")) {
                pstmt.setString(1, employeeNum);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("User with this employee number already exists.");
                    return;
                }
            }
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Users(EmployeeNum, FirstName, LastName, Username, Password) VALUES(?, ?, ?, ?, ?)")) {
                pstmt.setString(1, employeeNum);
                pstmt.setString(2, firstName);
                pstmt.setString(3, lastName);
                pstmt.setString(4, username);
                pstmt.setString(5, password);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Verify User
    public static void verifyUser(String employeeNum, String EIN) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM UserToCompany WHERE UserID = (SELECT ID FROM Users WHERE EmployeeNum = ?) AND EIN = ?")) {
                pstmt.setString(1, employeeNum);
                pstmt.setString(2, EIN);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                int count = rs.getInt(1);
                if (count > 0) {
                    System.out.println("User is associated with this company.");
                } else {
                    System.out.println("User is not associated with this company.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Create New Company
    public static void createCompany(String companyName, String EIN, String email, String phone, String employeeNum) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Company(EIN, Name, Email, PhoneNumber, EmployeeNum) VALUES(?, ?, ?, ?, ?)")) {
                pstmt.setString(1, EIN);
                pstmt.setString(2, companyName);
                pstmt.setString(3, email);
                pstmt.setString(4, phone);
                pstmt.setString(5, employeeNum);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Create New Social Media Account
    public static void createSocialMediaAccount(String EIN, String platformName, String accountHandle) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO SocialMediaAccounts(EIN, SocialMediaTypeID, AccountHandle) VALUES(?, ?, ?)")) {
                pstmt.setString(1, EIN);
                pstmt.setInt(2, getSocialMediaTypeID(platformName));
                pstmt.setString(3, accountHandle);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Create Marketing Campaign
    public static void createMarketingCampaign(int socialMediaID, String campaignName, Date startDate, Date endDate, double cost, String strategy) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO MarketingCampaign(SocialMediaID, CampaignName, StartDate, EndDate, Cost, Strategy) VALUES(?, ?, ?, ?, ?, ?)")) {
                pstmt.setInt(1, socialMediaID);
                pstmt.setString(2, campaignName);
                pstmt.setDate(3, startDate);
                pstmt.setDate(4, endDate);
                pstmt.setDouble(5, cost);
                pstmt.setString(6, strategy);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Helper method to get SocialMediaID by platformName
    private static int getSocialMediaTypeID(String platformName) throws SQLException {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT ID FROM SocialMediaType WHERE PlatformName = ?")) {
                pstmt.setString(1, platformName);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // Create Ad
    public static void createAd(int campaignId, Date startDate, Date endDate, String typeOfAd) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Ads(MarketingCampaignID, StartDate, EndDate, TypeOfAd) VALUES(?, ?, ?, ?)")) {
                pstmt.setInt(1, campaignId);
                pstmt.setDate(2, startDate);
                pstmt.setDate(3, endDate);
                pstmt.setString(4, typeOfAd);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // View Campaign Metrics
    public static void viewCampaignMetrics(String campaignName) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Metrics WHERE CampaignID = (SELECT ID FROM MarketingCampaign WHERE CampaignName = ?)")) {
                pstmt.setString(1, campaignName);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Campaign ID: " + rs.getInt("CampaignID"));
                    System.out.println("Impressions: " + rs.getInt("Impressions"));
                    System.out.println("Site Traffic: " + rs.getInt("siteTraffic"));
                    System.out.println("Conversions: " + rs.getInt("Conversions"));
                    System.out.println("Revenue: " + rs.getDouble("Revenue"));
                    System.out.println("-------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Get Employee by Employee Number
    public static void getEmployeeByEmployeeNumber(String employeeNum) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT FirstName, LastName, Username FROM Users WHERE EmployeeNum = ?")) {
                pstmt.setString(1, employeeNum);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("First Name: " + rs.getString("FirstName"));
                    System.out.println("Last Name: " + rs.getString("LastName"));
                    System.out.println("Username: " + rs.getString("Username"));
                } else {
                    System.out.println("No user found with this employee number.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addUserToCompany(String EIN, String employeeNum) {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO UserToCompany(UserID, EIN) VALUES((SELECT ID FROM Users WHERE EmployeeNum = ?), ?)")) {
                pstmt.setString(1, employeeNum);
                pstmt.setString(2, EIN);
                pstmt.executeUpdate();
                System.out.println("User added to company");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listUsers() {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users")) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("Employee Number: " + rs.getString("EmployeeNum"));
                    System.out.println("First Name: " + rs.getString("FirstName"));
                    System.out.println("Last Name: " + rs.getString("LastName"));
                    System.out.println("Username: " + rs.getString("Username"));
                    System.out.println("-------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void listCompanies() {
        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Company")) {
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    System.out.println("EIN: " + rs.getString("EIN"));
                    System.out.println("Name: " + rs.getString("Name"));
                    System.out.println("Email: " + rs.getString("Email"));
                    System.out.println("Phone Number: " + rs.getString("PhoneNumber"));
                    System.out.println("Employee Number: " + rs.getString("EmployeeNum"));
                    System.out.println("-------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
