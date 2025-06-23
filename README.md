# ExpenseTracker Desktop Application

A comprehensive, standalone expense management solution built with JavaFX. This application provides users with a simple, secure, and efficient way to track personal expenses locally on their desktop.

## Features

- **Multi-View Interface**: Dashboard, Expenses, and Income views accessible via tab navigation
- **Financial Overview**: Summary cards showing income, expenses, and balance
- **Visual Analytics**: 
  - Pie chart showing expense distribution by category
  - Pie chart showing income distribution by source
- **Expense Management**: Add, edit, and delete expenses with validation
- **Income Management**: Add, edit, and delete income entries with validation
- **Fixed Categories**: Predefined expense categories (Food, Transportation, Entertainment, etc.)
- **Payment Methods**: Support for Cash, UPI, and Card payments
- **Period Filtering**: View financial data by Month, Week, or Year
- **Material UI Design**: Modern, clean interface following Material Design principles

## Screenshots

### Dashboard
The main dashboard provides an overview of your finances with income, expenses, and balance cards, along with visual charts and recent expense listings.

## Requirements

- **Java**: JDK 11 or higher
- **Maven**: 3.6.0 or higher
- **Operating System**: Windows, macOS, or Linux

## Installation and Setup

### Method 1: Using Maven (Recommended)

1. **Clone or download the project**
   ```bash
   git clone <repository-url>
   cd PROJECT-JAVA
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Method 2: Using IDE

1. **Import the project** into your IDE (IntelliJ IDEA, Eclipse, VS Code)
2. **Ensure JDK 11+** is configured
3. **Run the main class**: `com.expensetracker.ExpenseTrackerApp`

### Method 3: Creating Executable JAR

1. **Build the JAR**
   ```bash
   mvn clean package
   ```

2. **Run the JAR** (requires JavaFX modules)
   ```bash
   java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml -jar target/expense-tracker-desktop-1.1.0.jar
   ```

## Project Structure

```
PROJECT-JAVA/
├── docs/                                    # Documentation
│   ├── Product-Requirements-Document.md
│   ├── software-requirements.md
│   ├── system-design-document.md
│   ├── Material UI Guidelines.md
│   └── User-Stories.md
├── src/main/
│   ├── java/com/expensetracker/
│   │   ├── ExpenseTrackerApp.java          # Main application class
│   │   ├── controller/                      # UI Controllers
│   │   │   ├── DashboardController.java    # Main dashboard logic
│   │   │   └── ExpenseModalController.java # Add/Edit expense modal
│   │   ├── model/                          # Data models
│   │   │   ├── Expense.java               # Expense entity
│   │   │   ├── ExpenseCategory.java       # Category enumeration
│   │   │   └── PaymentMethod.java         # Payment method enumeration
│   │   ├── service/                        # Business logic
│   │   │   ├── ExpenseService.java        # Service interface
│   │   │   └── ExpenseServiceImpl.java    # In-memory implementation
│   │   └── util/                          # Utility classes
│   │       └── ModalUtils.java            # Modal dialog utilities
│   └── resources/
│       ├── fxml/                          # FXML layout files
│       │   ├── dashboard.fxml             # Main dashboard layout
│       │   └── expense-modal.fxml         # Add/Edit expense modal
│       └── css/
│           └── styles.css                 # Material UI styling
├── pom.xml                                # Maven configuration
└── README.md                              # This file
```

## Usage Guide

### Adding an Expense

1. Click the **"Add Expense"** button in the top-right corner
2. Fill in the required fields:
   - **Amount**: Enter the expense amount
   - **Description**: Add a description for the expense
   - **Category**: Select from predefined categories
   - **Payment Method**: Choose Cash, UPI, or Card
   - **Date**: Select the expense date
3. Click **"Save"** to add the expense

### Editing an Expense

1. **Double-click** on any expense in the Recent Expenses table
2. **Or right-click** and select "Edit" from the context menu
3. Modify the fields as needed
4. Click **"Save"** to update the expense

### Deleting an Expense

1. **Right-click** on an expense in the Recent Expenses table
2. Select **"Delete"** from the context menu
3. Confirm the deletion in the dialog

### Viewing Different Time Periods

1. Use the **period selector** dropdown in the top-right
2. Choose from **Month**, **Week**, or **Year**
3. The dashboard will update to show data for the selected period

## Data Storage

- **Current Version**: Data is stored in memory and includes sample data for demonstration
- **Future Enhancement**: Local file storage (JSON/XML) can be implemented for data persistence

## Sample Data

The application comes with sample expense data to demonstrate its features:

- Lunch at restaurant ($25.50)
- Bus fare ($15.00)
- Grocery shopping ($120.00)
- Movie tickets ($50.00)
- Electric bill ($80.00)
- Clothes shopping ($200.00)
- Coffee with friends ($30.00)
- Taxi ride ($60.00)

## Technology Stack

- **Frontend**: JavaFX 17.0.2
- **Build Tool**: Maven 3.8+
- **Data Processing**: Jackson JSON (for future file storage)
- **Architecture**: MVC (Model-View-Controller)
- **Styling**: CSS with Material Design principles

## Customization

### Adding New Categories

1. Edit `ExpenseCategory.java` enum
2. Add new category constants
3. Rebuild the application

### Modifying UI Styles

1. Edit `styles.css` in the resources/css folder
2. Follow Material UI guidelines in the docs
3. The application will pick up changes on restart

### Extending Functionality

The application follows clean architecture principles:

- **Models**: Define data structures in the `model` package
- **Services**: Implement business logic in the `service` package
- **Controllers**: Handle UI interactions in the `controller` package
- **Views**: Define UI layouts in FXML files

## Troubleshooting

### Common Issues

1. **JavaFX Module Errors**
   - Ensure JavaFX is properly installed
   - Use `mvn javafx:run` instead of `java -jar`

2. **Build Failures**
   - Check Java version (requires JDK 11+)
   - Run `mvn clean` before building

3. **UI Not Loading**
   - Verify FXML and CSS files are in the correct resource directories
   - Check console for error messages

### Getting Help

1. Check the documentation in the `docs/` folder
2. Review the Product Requirements Document for feature specifications
3. Examine the System Design Document for architecture details

## Future Enhancements

- Data persistence to local files
- Import/Export functionality (CSV, PDF)
- Budgeting and goal setting
- Advanced reporting and analytics
- Multi-currency support
- Backup and restore capabilities

## License

This project is for educational and personal use. Refer to individual library licenses for third-party dependencies.

## Contributing

1. Follow the Material UI guidelines in the documentation
2. Maintain the single-dashboard architecture with modal dialogs
3. Write clean, documented code following the existing patterns
4. Test thoroughly on multiple platforms
